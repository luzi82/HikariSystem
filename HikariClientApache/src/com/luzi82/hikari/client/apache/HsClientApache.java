package com.luzi82.hikari.client.apache;

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
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.HsHttpClient;

public class HsClientApache implements HsHttpClient {

	Executor executor;
	CloseableHttpAsyncClient httpclient;

	public HsClientApache(Executor executor) {
		this.executor = executor;
		httpclient = HttpAsyncClients.createDefault();
		httpclient.start();
	}

	public static class StatusCodeException extends Exception {
		private static final long serialVersionUID = -8124311270798055897L;
		public int code;

		public StatusCodeException(int code) {
			super("code=" + code);
			this.code = code;
		}
	}

	private class SendJsonRequestFuture extends GuriFuture<String> {

		final String url;
		final String json;

		public SendJsonRequestFuture(final String url, final String json,
				FutureCallback<String> callback) {
			super(callback, HsClientApache.this.executor);
			this.url = url;
			this.json = json;
		}

		public void start() {
			new Step0().start();
		}

		Future<HttpResponse> f0;

		class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				List<NameValuePair> nvpList = new LinkedList<NameValuePair>();
				nvpList.add(new BasicNameValuePair("arg", json));
				HttpPost post = new HttpPost(url);
				post.setEntity(new UrlEncodedFormEntity(nvpList));

				f0 = httpclient.execute(post,
						new FutureCallbackWrapper<HttpResponse>(
								new Callback<HttpResponse>(new Step1())));
				setFuture(f0);
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
				completed(v);
			}

		}

	}

	@Override
	public Future<String> sendRequest(String url, String json,
			FutureCallback<String> callback) {
		SendJsonRequestFuture ret = new SendJsonRequestFuture(url, json,
				callback);
		ret.start();
		return ret;
	}

}
