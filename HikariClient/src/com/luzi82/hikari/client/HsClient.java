package com.luzi82.hikari.client;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.endpoint.HsCmdManager;

public class HsClient implements HsCmdManager {

	ObjectMapper mObjectMapper = new ObjectMapper();
	CloseableHttpAsyncClient httpclient;
	final String server;
	final HsStorage storage;
	final Executor executor;

	public HsClient(String aServer, HsStorage storage, Executor executor) {
		this.server = aServer;
		this.storage = storage;
		this.executor = executor;
		httpclient = HttpAsyncClients.createDefault();
		httpclient.start();
		mObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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
			super(callback, executor);
			this.appName = appName;
			this.string = string;
			this.request = request;
			this.class1 = class1;
		}

		public void start() {
			new Step0().start();
		}

		Future<HttpResponse> f0;

		class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				String url = server + "/ajax/" + appName + "/" + string
						+ ".json";
				System.err.println(url);

				HttpPost post = new HttpPost(url);

				String json = null;
				json = mObjectMapper.writeValueAsString(request);
				List<NameValuePair> nvpList = new LinkedList<NameValuePair>();
				nvpList.add(new BasicNameValuePair("arg", json));
				post.setEntity(new UrlEncodedFormEntity(nvpList));

				f0 = httpclient.execute(post, new Callback<HttpResponse>(
						new Step1()));
			}

			@Override
			public synchronized boolean cancel(boolean mayInterruptIfRunning) {
				if (f0 != null) {
					return f0.cancel(mayInterruptIfRunning);
				}
				return true;
			}

		}

		class Step1 extends Step {

			@Override
			public void _run() throws Exception {
				HttpResponse hr = f0.get();
				int code = hr.getStatusLine().getStatusCode();
				System.err.println(code);
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
