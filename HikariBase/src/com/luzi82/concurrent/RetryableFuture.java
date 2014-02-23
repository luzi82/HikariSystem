package com.luzi82.concurrent;

import java.util.concurrent.Future;

public interface RetryableFuture<V> extends Future<V> {

	public void retry();
	
	public boolean isRetryable();

}
