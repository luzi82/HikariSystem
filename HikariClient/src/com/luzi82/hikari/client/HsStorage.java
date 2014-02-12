package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;

public interface HsStorage {

	public Future<Void> put(String key, String value,
			FutureCallback<Void> callback);

	public Future<String> get(String key, FutureCallback<String> callback);

}
