package com.luzi82.hikari.client;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
		mObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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
			Exception exception = null;

			@Override
			public synchronized Result convert(HttpResponse hr)
					throws Exception {
				if (done) {
					if (exception != null)
						throw exception;
					return result;
				}
				done = true;
				try {
					// System.err.println(hr.getStatusLine().getStatusCode());
					int code = hr.getStatusLine().getStatusCode();
					if (code != 200) {
						throw new StatusCodeException(code);
					}
					HttpEntity he = hr.getEntity();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					he.writeTo(baos);
					String v = new String(baos.toByteArray(), "utf-8");
					// System.err.println(v + " start");
					Response res = mObjectMapper.readValue(v, Response.class);
					// System.err.println(v + " end");
					result = mObjectMapper.convertValue(res.data, class1);
				} catch (Exception e) {
					exception = e;
				}
				if (exception != null)
					throw exception;
				return result;
			}
		};

		FutureCallback<HttpResponse> fc = new HsConvertFutureCallback<Result, HttpResponse>(
				callback, convert);

		String url = server + "/ajax/" + appName + "/" + string + ".json";
		System.err.println(url);

		HttpPost post = new HttpPost(url);

		String json = null;
		try {
			json = mObjectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new Error(e);
		}
		List<NameValuePair> nvpList = new LinkedList<NameValuePair>();
		nvpList.add(new BasicNameValuePair("arg", json));
		try {
			post.setEntity(new UrlEncodedFormEntity(nvpList));
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}

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

	static class StatusCodeException extends Exception {
		private static final long serialVersionUID = 9200724531954807040L;
		public int code;

		public StatusCodeException(int code) {
			super("code=" + code);
			this.code = code;
		}
	}

}
