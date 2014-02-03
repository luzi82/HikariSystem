package com.luzi82.hikari.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzi82.hikari.client.endpoint.HsCmdManager;

public class HsClient implements HsCmdManager {

	ObjectMapper mObjectMapper = new ObjectMapper();
	CloseableHttpAsyncClient httpclient;
	String server;
	HsStorage storage;

	public HsClient(String aServer, HsStorage storage) {
		server = aServer;
		httpclient = HttpAsyncClients.createDefault();
		httpclient.start();
		this.storage = storage;
	}

	public static class Response {
		public boolean success;
		public JsonNode data;
	}

	public <Result> Future<Result> sendRequest(final String appName,
			final String string, final Object request,
			final Class<Result> class1, final FutureCallback<Result> callback) {

		HsConvert<Result, HttpResponse> convert = new HsConvert<Result, HttpResponse>() {
			boolean done = false;
			Result result = null;

			@Override
			public synchronized Result convert(HttpResponse hr)
					throws IOException {
				if (done) {
					return result;
				}
				done = true;
				// System.err.println(hr.getStatusLine().getStatusCode());
				HttpEntity he = hr.getEntity();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				he.writeTo(baos);
				String v = new String(baos.toByteArray(), "utf-8");
				// System.err.println(v + " start");
				Response res = mObjectMapper.readValue(v, Response.class);
				// System.err.println(v + " end");
				result = mObjectMapper.convertValue(res.data, class1);
				return result;
			}
		};

		FutureCallback<HttpResponse> fc = new HsConvertFutureCallback<Result, HttpResponse>(
				callback, convert);

		String url = server + "/ajax/" + appName + "/" + string + ".json";
		System.err.println(url);
		HttpPost post = new HttpPost(url);

		Future<HttpResponse> f = httpclient.execute(post, fc);

		return new HsConvertFuture<Result, HttpResponse>(f, convert);
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

}
