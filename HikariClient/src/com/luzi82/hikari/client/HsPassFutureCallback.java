package com.luzi82.hikari.client;

import org.apache.http.concurrent.FutureCallback;

public class HsPassFutureCallback<V> implements FutureCallback<V> {

	FutureCallback<V> output;

	public HsPassFutureCallback(FutureCallback<V> output) {
		this.output = output;
	}

	@Override
	public void cancelled() {
		if (output != null)
			output.cancelled();
	}

	@Override
	public void completed(V arg0) {
		if (output != null)
			output.completed(arg0);
	}

	@Override
	public void failed(Exception arg0) {
		if (output != null)
			output.failed(arg0);
	}

}
