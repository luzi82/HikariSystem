package com.luzi82.hikari.client.apache;

import org.apache.http.concurrent.FutureCallback;

public class FutureCallbackWrapper<T> implements FutureCallback<T> {

	final public com.luzi82.concurrent.FutureCallback<T> callback;

	public FutureCallbackWrapper(
			com.luzi82.concurrent.FutureCallback<T> callback) {
		this.callback = callback;
	}

	@Override
	public void cancelled() {
		callback.cancelled();
	}

	@Override
	public void completed(T arg0) {
		callback.completed(arg0);
	}

	@Override
	public void failed(Exception arg0) {
		callback.failed(arg0);
	}

}
