package com.luzi82.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class GuriFuture<T> extends BasicRetryableFuture<T> {

	public Executor executor;

	public final boolean reallyRetryable;
	public boolean firstFireDone = false;

	public GuriFuture(boolean reallyRetryable, FutureCallback<T> callback,
			Executor executor) {
		super(callback);
		this.executor = executor;
		this.reallyRetryable = reallyRetryable;
	}

	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		if (isCancelled()) {
			return super.cancel(mayInterruptIfRunning);
		} else if (currentStep != null) {
			boolean ret = currentStep.cancel(mayInterruptIfRunning);
			if (ret) {
				currentStep = null;
				super.cancel(mayInterruptIfRunning);
			}
			return ret;
		} else {
			return super.cancel(mayInterruptIfRunning);
		}
	}

	Step currentStep = null;
	protected Object lastFutureResult = null;

	public abstract class Step implements Runnable {

		Future<?> future;

		public void start() {
			executor.execute(this);
		}

		@Override
		public void run() {
			synchronized (GuriFuture.this) {
				try {
					if (GuriFuture.this.isCancelled()) {
						return;
					}
					currentStep = this;
					_run();
				} catch (Exception e) {
					GuriFuture.this.failed(e);
				}
			}
		}

		public void setFuture(Future<?> future) {
			synchronized (GuriFuture.this) {
				this.future = future;
			}
		}

		public boolean cancel(boolean mayInterruptIfRunning) {
			synchronized (GuriFuture.this) {
				if (future != null) {
					return future.cancel(mayInterruptIfRunning);
				}
				return true;
			}
		}

		abstract public void _run() throws Exception;

	}

	public class Callback<U> implements FutureCallback<U> {

		Step s;

		public Callback(Step s) {
			this.s = s;
		}

		@Override
		public void cancelled() {
			System.err.println("MPDBbKHF - GuriFuture.Callback.cancelled");
			synchronized (GuriFuture.this) {
				GuriFuture.this.cancelled();
			}
		}

		@Override
		public void failed(Exception arg0) {
			synchronized (GuriFuture.this) {
				GuriFuture.this.failed(arg0);
			}
		}

		@Override
		public void completed(U arg0) {
			synchronized (GuriFuture.this) {
				try {
					if (GuriFuture.this.isCancelled()) {
						return;
					}
					lastFutureResult = arg0;
					s.start();
				} catch (Exception e) {
					GuriFuture.this.failed(e);
				}
			}
		}

	}

	protected boolean busy;

	@Override
	public boolean completed(T result) {
		synchronized (this) {
			busy = false;
		}
		return super.completed(result);
	}

	@Override
	public synchronized boolean failed(Exception exception) {
		synchronized (this) {
			busy = false;
		}
		return super.failed(exception);
	}

	protected synchronized void cancelled() {
		synchronized (this) {
			busy = false;
		}
	}

	public final void start() {
		retry();
	}

	@Override
	public final synchronized void retry() {
		if (busy)
			return;
		super.retry();
		firstFireDone = true;
		fire();
	}

	protected abstract void fire();

	@Override
	public boolean isRetryable() {
		if ((firstFireDone) && (!reallyRetryable))
			return false;
		return super.isRetryable();
	}

}
