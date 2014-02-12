package com.luzi82.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public class GuriFuture<T> extends BasicFuture<T> {

	public Executor executor;

	public GuriFuture(FutureCallback<T> callback, Executor executor) {
		super(callback);
		this.executor = executor;
	}

	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		if (isCancelled()) {
			return super.cancel();
		} else if (currentStep != null) {
			boolean ret = currentStep.cancel(mayInterruptIfRunning);
			if (ret) {
				currentStep = null;
				super.cancel();
			}
			return ret;
		} else {
			return super.cancel();
		}
	}

	Step currentStep = null;

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
					s.start();
				} catch (Exception e) {
					GuriFuture.this.failed(e);
				}
			}
		}

	}

}
