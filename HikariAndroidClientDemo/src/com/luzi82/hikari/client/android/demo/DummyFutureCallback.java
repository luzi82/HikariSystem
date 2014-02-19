package com.luzi82.hikari.client.android.demo;

import com.luzi82.concurrent.FutureCallback;

public class DummyFutureCallback<T> implements FutureCallback<T> {

	protected final FutureCallback<T> callback;

	public DummyFutureCallback(FutureCallback<T> callback) {
		this.callback = callback;
	}

	@Override
	public void completed(T result) {
		if (callback != null) {
			callback.completed(result);
		}
	}

	@Override
	public void failed(Exception ex) {
		if (callback != null) {
			callback.failed(ex);
		}
	}

	@Override
	public void cancelled() {
		if (callback != null) {
			callback.cancelled();
		}
	}

}
