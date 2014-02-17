package com.luzi82.hikari.client;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import au.com.bytecode.opencsv.CSVReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.endpoint.HsCmdManager;
import com.luzi82.hikari.client.protocol.gen.out.ClientInit;

public class HsClient implements HsCmdManager {

	// final public String server;
	String server;
	final HsStorage storage;
	final public Executor executor;
	HsHttpClient jsonClient;
	ObjectMapper mObjectMapper = new ObjectMapper();
	List<DataLoad> dataLoadList = new LinkedList<HsClient.DataLoad>();

	// final HsCmdManager cmdManager;

	public HsClient(String aServer, HsStorage storage, Executor executor,
			HsHttpClient jsonClient) {
		this.server = aServer;
		this.storage = storage;
		this.executor = executor;
		this.jsonClient = jsonClient;
		mObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

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
	}

	private class SendRequestFuture<Result> extends GuriFuture<Result> {

		final String appName;
		final String string;
		final Object request;
		final Class<Result> class1;

		public SendRequestFuture(final String appName, final String string,
				final Object request, final Class<Result> class1,
				FutureCallback<Result> callback) {
			super(callback, HsClient.this.executor);
			this.appName = appName;
			this.string = string;
			this.request = request;
			this.class1 = class1;
		}

		public void start() {
			new Step0().start();
		}

		Future<String> f0;

		class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				String url = server + "/ajax/hikari/" + appName + "__" + string
						+ ".json";
				// System.err.println(url);

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
				// System.err.println(v + " start");
				Response res = mObjectMapper.readValue(v, Response.class);
				// System.err.println(v + " end");
				Result result = mObjectMapper.convertValue(res.data, class1);
				completed(result);
			}

		}

	}

	public <Result> Future<Result> sendRequest(final String appName,
			final String string, final Object request,
			final Class<Result> class1, final FutureCallback<Result> callback) {

		SendRequestFuture<Result> ret = new SendRequestFuture<Result>(appName,
				string, request, class1, callback);
		ret.start();
		return ret;
	}

	@Override
	public <Data> Future<List<Data>> getDataList(String appName,
			String dataName, Class<Data> dataClass,
			FutureCallback<List<Data>> futureCallback) {
		GetDataListFuture<Data> ret = new GetDataListFuture<Data>(appName,
				dataName, dataClass, futureCallback);
		ret.start();
		return ret;
	}

	private class GetDataListFuture<Data> extends GuriFuture<List<Data>> {

		final String appName;
		final String dataName;
		final Class<Data> dataClass;

		public GetDataListFuture(String appName, String dataName,
				Class<Data> dataClass, FutureCallback<List<Data>> callback) {
			super(callback, HsClient.this.executor);
			this.appName = appName;
			this.dataName = dataName;
			this.dataClass = dataClass;
		}

		public void start() {
			new Step0().start();
		}

		class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				String key = "hsclient__data__" + appName + "__" + dataName;
				f0 = storage.get(key, new Callback<String>(new Step1()));
				setFuture(f0);
			}
		}

		Future<String> f0;

		class Step1 extends Step {
			@Override
			public void _run() throws Exception {
				String dataString = f0.get();
				StringReader sr = new StringReader(dataString);

				CSVReader cr = new CSVReader(sr);
				List<String[]> csvDataList = cr.readAll();
				cr.close();
				Iterator<String[]> csvDataItr = csvDataList.iterator();

				Map<String, Integer> colNameToIdx = new HashMap<String, Integer>();
				String[] keyRow = csvDataItr.next();
				for (int i = 0; i < keyRow.length; ++i) {
					String cellValue = keyRow[i];
					if (cellValue == null)
						continue;
					if (cellValue.length() <= 0)
						continue;
					colNameToIdx.put(cellValue, i);
				}

				Field[] dataClassFieldAry = dataClass.getFields();

				LinkedList<Data> ret = new LinkedList<Data>();
				while (csvDataItr.hasNext()) {
					String[] dataRow = csvDataItr.next();
					Data data = dataClass.newInstance();
					for (Field dataClassField : dataClassFieldAry) {
						String fieldname = dataClassField.getName();
						int colIdx = colNameToIdx.get(fieldname);
						dataClassField.set(data, dataRow[colIdx]);
					}
					ret.add(data);
				}

				completed(ret);
			}

		}

	}

	@Override
	public void addDataLoad(String appName, String dataName) {
		DataLoad dataLoad = new DataLoad();
		dataLoad.appName = appName;
		dataLoad.dataName = dataName;
		dataLoadList.add(dataLoad);
	}

	public class DataLoad {
		public String appName;
		public String dataName;
	}

	public Future<Void> syncData(final FutureCallback<Void> callback) {
		SyncDataFuture ret = new SyncDataFuture(callback);
		ret.start();
		return ret;
	}

	private class SyncDataFuture extends GuriFuture<Void> {

		public SyncDataFuture(FutureCallback<Void> callback) {
			super(callback, HsClient.this.executor);
		}

		public void start() {
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

				String url = server + "/static/csv/" + currentDataLoad.dataName
						+ ".csv";
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
				String key = "hsclient__data__" + currentDataLoad.appName
						+ "__" + currentDataLoad.dataName;
				currentDataLoad = null;
				f1 = null;
				setFuture(storage
						.put(key, csv, new Callback<Void>(new Step1())));
			}
		}

	}

}
