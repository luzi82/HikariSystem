package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;

public class HsMemStorage implements HsStorage {

	HashMap<String, String> map = new HashMap<String, String>();

	ExecutorService executorService;

	public HsMemStorage(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public Future<Void> put(final String key, final String value,
			final FutureCallback<Void> callback) {
		System.err.println("0fjWviop " + key + " " + value);
		return executorService.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				System.err.println("v1eWJ7E4 " + key + " " + value);
				map.put(key, value);
				if (callback != null)
					callback.completed(null);
				return null;
			}
		});
	}

	@Override
	public Future<String> get(final String key,
			final FutureCallback<String> callback) {
		return executorService.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				String ret = map.get(key);
				if (callback != null)
					callback.completed(ret);
				return ret;
			}
		});
	}

}
