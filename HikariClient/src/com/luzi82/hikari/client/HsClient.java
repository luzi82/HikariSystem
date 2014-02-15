package com.luzi82.hikari.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.endpoint.HsCmdManager;

public class HsClient implements HsCmdManager {

	// final public String server;
	String server;
	final HsStorage storage;
	final public Executor executor;
	HsHttpClient jsonClient;
	ObjectMapper mObjectMapper = new ObjectMapper();

	// final HsCmdManager cmdManager;

	public HsClient(String aServer, HsStorage storage, Executor executor,
			HsHttpClient jsonClient) {
		this.server = aServer;
		this.storage = storage;
		this.executor = executor;
		this.jsonClient = jsonClient;
		mObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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
//				System.err.println(url);

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

}
