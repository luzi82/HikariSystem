package com.luzi82.concurrent;

public class RecordFutureCallback<T> extends DummyFutureCallback<T> {

	public RecordFutureCallback(FutureCallback<T> callback) {
		super(callback);
	}

	public boolean completed = false;
	public boolean failed = false;
	public boolean cancelled = false;
	public T result;
	public Exception ex;

	@Override
	public void completed(T result) {
		this.completed = true;
		this.result = result;
		super.completed(result);
	}

	@Override
	public void failed(Exception ex) {
		this.failed = true;
		this.ex = ex;
		super.failed(ex);
	}

	@Override
	public void cancelled() {
		this.cancelled = true;
		super.cancelled();
	}

	public void clear() {
		completed = false;
		failed = false;
		cancelled = false;
		result = null;
		ex = null;
	}

}
