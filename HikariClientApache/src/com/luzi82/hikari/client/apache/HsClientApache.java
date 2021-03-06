package com.luzi82.hikari.client.apache;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.HsHttpClient;

public class HsClientApache implements HsHttpClient {

	Executor executor;
	CloseableHttpAsyncClient httpclient;
	CookieStore cookieStore;

	public HsClientApache(Executor executor) {
		this.executor = executor;
		// httpclient = HttpAsyncClients.createDefault();
		cookieStore = new BasicCookieStore();
		HttpAsyncClientBuilder hacb = HttpAsyncClients.custom();
		hacb.setDefaultCookieStore(cookieStore);
		httpclient = hacb.build();
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
			super(false, callback, HsClientApache.this.executor);
			this.url = url;
			this.json = json;
		}

		protected void fire() {
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
				System.err.println("P9wc1jhk code: " + code);
				if (code != 200) {
					String msg = IOUtils.toString(hr.getEntity().getContent(),
							"utf-8");
					System.err.println("PuMzYp8X code: " + msg);
					throw new StatusCodeException(code);
				}
				HttpEntity he = hr.getEntity();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				he.writeTo(baos);
				String v = new String(baos.toByteArray(), "utf-8");
				System.err.println("u0HN2JGy result: " + v);
				completed(v);
			}

		}

	}

	@Override
	public Future<String> sendRequest(String url, String json,
			FutureCallback<String> callback) {
		System.err.println("gZcbMSW4 url: " + url);
		System.err.println("v6soQcnb json: " + json);
		SendJsonRequestFuture ret = new SendJsonRequestFuture(url, json,
				callback);
		ret.start();
		return ret;
	}

	@Override
	public Future<String> get(String url, FutureCallback<String> callback) {
		GetFuture ret = new GetFuture(url, callback);
		ret.start();
		return ret;
	}

	private class GetFuture extends GuriFuture<String> {
		final String url;

		public GetFuture(final String url, FutureCallback<String> callback) {
			super(false, callback, HsClientApache.this.executor);
			this.url = url;
			System.err.println("z7U004fe url: " + url);
		}

		public void fire() {
			new Step0().start();
		}

		Future<HttpResponse> f0;

		class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				HttpGet get = new HttpGet(url);

				f0 = httpclient.execute(get,
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
				System.err.println("jqPm4LTA code: " + code);
				if (code != 200) {
					throw new StatusCodeException(code);
				}
				HttpEntity he = hr.getEntity();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				he.writeTo(baos);
				String v = new String(baos.toByteArray(), "utf-8");
				System.err.println("Yi1QNFun result: " + v);
				completed(v);
			}

		}
	}

	@Override
	public String getCookie(String domain, String path, String name) {
		List<Cookie> cookieList = cookieStore.getCookies();
		for (Cookie cookie : cookieList) {
			// System.err.println(cookie.getDomain());
			// System.err.println(cookie.getPath());
			// System.err.println(cookie.getName());
			if (!cookie.getName().equals(name)) {
				continue;
			}
			if (!cookie.getDomain().equals(domain)) {
				continue;
			}
			if (!cookie.getPath().equals(path)) {
				continue;
			}
			return cookie.getValue();
		}
		return null;
	}

	@Override
	public void setCookie(String domain, String path, String name, String value) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookieStore.addCookie(cookie);
	}

}
