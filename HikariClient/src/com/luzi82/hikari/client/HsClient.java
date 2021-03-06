package com.luzi82.hikari.client;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.apache.commons.lang.ObjectUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.concurrent.RetryableFuture;
import com.luzi82.hikari.client.endpoint.HsCmdManager;
import com.luzi82.hikari.client.protocol.gen.out.ClientInit;
import com.luzi82.lang.GuriObservable;

@SuppressWarnings({"unchecked","rawtypes"})
public class HsClient implements HsCmdManager {

	String server;
	String serverHost;
	final HsStorage storage;
	final public Executor executor;
	HsHttpClient jsonClient;
	ObjectMapper mObjectMapper = new ObjectMapper();
	List<DataLoad> dataLoadList = new LinkedList<HsClient.DataLoad>();
	final Map<String, Object> tmpMap;
	final Map<String, List> dataMap;
	final Map<String, GuriObservable> statusObservableMap;
	final Map<String, Class> statusClassMap;
	long serverTimeOffset;
	final Map<String, String> staticDataParam;

	public HsClient(String aServer, HsStorage storage, Executor executor,
			HsHttpClient jsonClient) throws URISyntaxException {
		this.server = aServer;
		this.storage = storage;
		this.executor = executor;
		this.jsonClient = jsonClient;
		mObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		this.dataMap = new HashMap<String, List>();
		this.tmpMap = new HashMap<String, Object>();
		this.statusObservableMap = new HashMap<String, GuriObservable>();
		this.statusClassMap = new HashMap<String, Class>();
		this.staticDataParam = new HashMap<String, String>();

		serverHost = new URI(server).getHost();

		ClientInit.initClient(this);

	}

	public Future<Void> put(String appName, String key, String value,
			FutureCallback<Void> callback) {
		String k = appName + "__" + key;
		return storage.put(k, value, callback);
	}

	public Future<String> get(String appName, String key,
			FutureCallback<String> callback) {
		String k = appName + "__" + key;
		return storage.get(k, callback);
	}

	public static class Response {
		public boolean success;
		public JsonNode data;
		public Map<String, JsonNode> status_update_dict;
		public long time;
	}

	private class SendRequestFuture<Result> extends GuriFuture<Result>
			implements RetryableFuture<Result> {

		final String appName;
		final String string;
		final Object request;
		final Class<Result> class1;
		final String seqId;

		public SendRequestFuture(final String appName, final String string,
				final Object request, final Class<Result> class1,
				FutureCallback<Result> callback) {
			super(true, callback, HsClient.this.executor);
			this.appName = appName;
			this.string = string;
			this.request = request;
			this.class1 = class1;
			this.seqId = getCookie("seqid");
		}

		@Override
		public void fire() {
			new Step0().start();
		}

		Future<String> f0;

		class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				String url = String.format("%s/ajax/%s/%s.json", server,
						appName, string);

				String json = null;
				json = mObjectMapper.writeValueAsString(request);

				f0 = jsonClient.sendRequest(url, json, new Callback<String>(
						new Step1()));
				setFuture(f0);
			}

		}

		class Step1 extends Step {

			@Override
			public void _run() throws Exception {
				String v = f0.get();
				Response res = mObjectMapper.readValue(v, Response.class);
				if (res.status_update_dict != null) {
					for (Map.Entry<String, JsonNode> status_update : res.status_update_dict
							.entrySet()) {
						String appName = status_update.getKey();
						Object status = mObjectMapper.convertValue(
								status_update.getValue(),
								statusClassMap.get(appName));
						statusObservableMap.get(appName).setNotify(status);
					}
				}
				serverTimeOffset = res.time - System.currentTimeMillis();

				Result result = mObjectMapper.convertValue(res.data, class1);
				completed(result);
			}

		}

		@Override
		public boolean isRetryable() {
			if (!ObjectUtils.equals(seqId, getCookie("seqid"))) {
				return false;
			}
			return super.isRetryable();
		}

	}

	public <Result> RetryableFuture<Result> sendRequest(final String appName,
			final String string, final Object request,
			final Class<Result> class1, final FutureCallback<Result> callback) {

		SendRequestFuture<Result> ret = new SendRequestFuture<Result>(appName,
				string, request, class1, callback);
		ret.start();
		return ret;
	}

	@Override
	public <Data> List<Data> getDataList(String appName, String dataName,
			Class<Data> dataClass) {
		String key = appName + "__" + dataName;
		return dataMap.get(key);
	}

	@Override
	public void addDataLoad(String appName, String dataName, Class dataClass) {
		DataLoad dataLoad = new DataLoad();
		dataLoad.appName = appName;
		dataLoad.dataName = dataName;
		dataLoad.dataClass = dataClass;
		dataLoadList.add(dataLoad);
	}

	public class DataLoad {
		public String appName;
		public String dataName;
		public Class dataClass;
	}

	public Future<Void> syncData(final FutureCallback<Void> callback) {
		SyncDataFuture ret = new SyncDataFuture(callback);
		ret.start();
		return ret;
	}

	private class SyncDataFuture extends GuriFuture<Void> {

		public SyncDataFuture(FutureCallback<Void> callback) {
			super(false, callback, HsClient.this.executor);
		}

		public void fire() {
			new Step0().start();
		}

		class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				dataLoadItr = dataLoadList.iterator();
				new Step1().start();
			}
		}

		Iterator<DataLoad> dataLoadItr;

		class Step1 extends Step {
			@Override
			public void _run() throws Exception {
				if (!dataLoadItr.hasNext()) {
					completed(null);
					return;
				}
				currentDataLoad = dataLoadItr.next();

				// System.err.println("currentDataLoad.dataName "
				// + currentDataLoad.dataName);

				StringBuffer param = new StringBuffer();
				try {
					Field paramField = currentDataLoad.dataClass
							.getField("PARAM");
					String[] paramList = (String[]) paramField.get(null);
					for (String p : paramList) {
						String pp = staticDataParam.get(p);
						if (pp == null)
							pp = "_";
						param.append("-");
						param.append(pp);
					}
				} catch (NoSuchFieldException e) {
				}

				String url = server + "/static/csv/" + currentDataLoad.dataName
						+ param.toString() + ".csv";
				f1 = jsonClient.get(url, new Callback<String>(new Step2()));
				setFuture(f1);
			}
		}

		DataLoad currentDataLoad;
		Future<String> f1;

		class Step2 extends Step {
			@Override
			public void _run() throws Exception {
				String csv = f1.get();

				// save data to dataMap
				List dataList = CsvParser
						.toList(csv, currentDataLoad.dataClass);
				dataMap.put(currentDataLoad.appName + "__"
						+ currentDataLoad.dataName, dataList);

				// save data to storage
				String key = "hsclient__data__" + currentDataLoad.appName
						+ "__" + currentDataLoad.dataName;
				currentDataLoad = null;
				f1 = null;
				setFuture(storage
						.put(key, csv, new Callback<Void>(new Step1())));
			}
		}

	}

	public String getCookie(String string) {
		return jsonClient.getCookie(serverHost, "/", string);
	}

	public void setCookie(String string, String seqId) {
		jsonClient.setCookie(serverHost, "/", string, seqId);
	}

	public void putTmp(String appName, String key, Object value) {
		String k = appName + "__" + key;
		tmpMap.put(k, value);
	}

	public Object getTmp(String appName, String key) {
		String k = appName + "__" + key;
		return tmpMap.get(k);
	}

	private String statusKey(String appName, String statusName) {
		return statusName;
	}

	@Override
	public <Status> void addStatus(String appName, String statusName,
			Class<Status> statusClass) {
		String key = statusKey(appName, statusName);
		GuriObservable<Status> observable = new GuriObservable<Status>();
		statusObservableMap.put(key, observable);
		statusClassMap.put(key, statusClass);
	}

	public long getServerTimeOffset() {
		return serverTimeOffset;
	}

	public long getServerTime(long clientTime) {
		return clientTime + getServerTimeOffset();
	}

	@Override
	public <Status> GuriObservable<Status> getStatusObservable(String appName,
			String statusName, Class<Status> class1) {
		String key = statusKey(appName, statusName);
		return statusObservableMap.get(key);
	}

	public void setStaticDataParam(String key, String value) {
		staticDataParam.put(key, value);
	}

	@Override
	public ObjectMapper getObjectMapper() {
		return mObjectMapper;
	}

	// TODO change to load data from disk to memory
	// public <Data> Future<List<Data>> getDataList(String appName,
	// String dataName, Class<Data> dataClass,
	// FutureCallback<List<Data>> futureCallback) {
	// GetDataListFuture<Data> ret = new GetDataListFuture<Data>(appName,
	// dataName, dataClass, futureCallback);
	// ret.start();
	// return ret;
	// }
	//
	// private class GetDataListFuture<Data> extends GuriFuture<List<Data>> {
	//
	// final String appName;
	// final String dataName;
	// final Class<Data> dataClass;
	//
	// public GetDataListFuture(String appName, String dataName,
	// Class<Data> dataClass, FutureCallback<List<Data>> callback) {
	// super(false, callback, HsClient.this.executor);
	// this.appName = appName;
	// this.dataName = dataName;
	// this.dataClass = dataClass;
	// }
	//
	// public void fire() {
	// new Step0().start();
	// }
	//
	// class Step0 extends Step {
	// @Override
	// public void _run() throws Exception {
	// String key = "hsclient__data__" + appName + "__" + dataName;
	// f0 = storage.get(key, new Callback<String>(new Step1()));
	// setFuture(f0);
	// }
	// }
	//
	// Future<String> f0;
	//
	// class Step1 extends Step {
	// @Override
	// public void _run() throws Exception {
	// String dataString = f0.get();
	// StringReader sr = new StringReader(dataString);
	//
	// CSVReader cr = new CSVReader(sr);
	// List<String[]> csvDataList = cr.readAll();
	// cr.close();
	// Iterator<String[]> csvDataItr = csvDataList.iterator();
	//
	// Map<String, Integer> colNameToIdx = new HashMap<String, Integer>();
	// String[] keyRow = csvDataItr.next();
	// for (int i = 0; i < keyRow.length; ++i) {
	// String cellValue = keyRow[i];
	// if (cellValue == null)
	// continue;
	// if (cellValue.length() <= 0)
	// continue;
	// colNameToIdx.put(cellValue, i);
	// }
	//
	// Field[] dataClassFieldAry = dataClass.getFields();
	//
	// LinkedList<Data> ret = new LinkedList<Data>();
	// while (csvDataItr.hasNext()) {
	// String[] dataRow = csvDataItr.next();
	// Data data = dataClass.newInstance();
	// for (Field dataClassField : dataClassFieldAry) {
	// String fieldname = dataClassField.getName();
	// int colIdx = colNameToIdx.get(fieldname);
	// Class dataClassFieldType = dataClassField.getType();
	// if (dataClassFieldType == String.class) {
	// dataClassField.set(data, dataRow[colIdx]);
	// } else if (dataClassFieldType == Integer.class) {
	// dataClassField.set(data,
	// Integer.parseInt(dataRow[colIdx]));
	// } else if (dataClassFieldType == Integer.TYPE) {
	// dataClassField.set(data,
	// Integer.parseInt(dataRow[colIdx]));
	// } else {
	// throw new NotImplementedException("RHDTI3GN: "
	// + dataClassFieldType.getName());
	// }
	// }
	// ret.add(data);
	// }
	//
	// completed(ret);
	// }
	//
	// }
	//
	// }

}
