package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;

public interface HsHttpClient {

	public Future<String> sendRequest(final String url, final String request,
			final FutureCallback<String> callback);

}
