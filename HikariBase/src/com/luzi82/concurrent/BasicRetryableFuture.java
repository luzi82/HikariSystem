package com.luzi82.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Basic implementation of the {@link Future} interface. <tt>BasicFuture<tt>
 * can be put into a completed state by invoking any of the following methods:
 * {@link #cancel()}, {@link #failed(Exception)}, or {@link #completed(Object)}.
 * 
 * @param <T>
 *            the future result type of an asynchronous operation.
 * @since 4.2
 */
public class BasicRetryableFuture<T> implements RetryableFuture<T>, Cancellable {

	private final FutureCallback<T> callback;

	private volatile boolean retryable; // cannot use result=null to detect,
										// completed result can be null
	private volatile boolean completed;
	private volatile boolean cancelled;
	private volatile T result;
	private volatile Exception ex;

	public BasicRetryableFuture(final FutureCallback<T> callback) {
		super();
		this.callback = callback;
		this.retryable = true;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public boolean isDone() {
		return this.completed;
	}

	private T getResult() throws ExecutionException {
		if (this.ex != null) {
			throw new ExecutionException(this.ex);
		}
		return this.result;
	}

	public synchronized T get() throws InterruptedException, ExecutionException {
		while (!this.completed) {
			wait();
		}
		return getResult();
	}

	public synchronized T get(final long timeout, final TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		Args.notNull(unit, "Time unit");
		final long msecs = unit.toMillis(timeout);
		final long startTime = (msecs <= 0) ? 0 : System.currentTimeMillis();
		long waitTime = msecs;
		if (this.completed) {
			return getResult();
		} else if (waitTime <= 0) {
			throw new TimeoutException();
		} else {
			for (;;) {
				wait(waitTime);
				if (this.completed) {
					return getResult();
				} else {
					waitTime = msecs - (System.currentTimeMillis() - startTime);
					if (waitTime <= 0) {
						throw new TimeoutException();
					}
				}
			}
		}
	}

	public boolean completed(final T result) {
		synchronized (this) {
			if (this.completed) {
				return false;
			}
			this.retryable = false;
			this.completed = true;
			this.result = result;
			notifyAll();
		}
		if (this.callback != null) {
			this.callback.completed(result);
		}
		return true;
	}

	public boolean failed(final Exception exception) {
		synchronized (this) {
			if (this.completed) {
				return false;
			}
			this.completed = true;
			this.ex = exception;
			notifyAll();
		}
		if (this.callback != null) {
			this.callback.failed(exception);
		}
		return true;
	}

	public boolean cancel(final boolean mayInterruptIfRunning) {
		synchronized (this) {
			if (this.completed) {
				return false;
			}
			this.retryable = false;
			this.completed = true;
			this.cancelled = true;
			notifyAll();
		}
		if (this.callback != null) {
			this.callback.cancelled();
		}
		return true;
	}

	public boolean cancel() {
		return cancel(true);
	}

	@Override
	public synchronized void retry() {
		if (!isRetryable()) {
			throw new IllegalStateException();
		}
		completed = false;
		ex = null;
	}

	@Override
	public boolean isRetryable() {
		return retryable;
	}

}
