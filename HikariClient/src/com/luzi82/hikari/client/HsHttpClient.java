package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;

public interface HsHttpClient {

	public Future<String> sendRequest(final String url, final String request,
			final FutureCallback<String> callback);

	public Future<String> get(final String url,
			final FutureCallback<String> callback);

	public String getCookie(String domain, String path, String name);

	public void setCookie(String domain, String path, String name, String value);

}
