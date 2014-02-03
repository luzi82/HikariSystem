package com.luzi82.hikari.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.concurrent.FutureCallback;

public class HsStepListFuture<T> implements Future<T> {

	public Object[] resultAry;
	int resultOffset = 0;

	public LinkedList<Runnable> runnableList = new LinkedList<Runnable>();

	public Iterator<Runnable> runItr;

	FutureCallback<T> callback;

	public HsStepListFuture(FutureCallback<T> callback) {
		this.callback = callback;
	}

	public void addRunnable(Runnable runnable) {
		runnableList.add(runnable);
	}

	public <U> FutureCallback<U> createFutureCallback(Class<U> t) {
		return new FutureCallback<U>() {
			@Override
			public void cancelled() {
				System.err.println("VloLuWTs");
				if (callback != null)
					callback.cancelled();
			}

			@Override
			public void completed(U arg0) {
				System.err.println("bIm1Mpsx");
				synchronized (HsStepListFuture.this) {
					System.err.println("U8VaZLLj");
					currentFuture = null;
					resultAry[resultOffset] = arg0;
					++resultOffset;
					runNext();
				}
			}

			@Override
			public void failed(Exception arg0) {
				System.err.println("xc0nFclK");
				if (callback != null)
					callback.failed(arg0);
			}
		};
	}

	boolean done = false;
	T result;
	Exception resultException;

	public void setResult(T cur) {
		this.result = cur;
	}

	public void start() {
		runItr = runnableList.iterator();
		resultAry = new Object[runnableList.size()];
		runNext();
	}

	Future<?> currentFuture = null;
	boolean canceled = false;
	boolean cancelMayInterruptRunning = false;

	@Override
	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		if (currentFuture != null) {
			boolean ret = currentFuture.cancel(mayInterruptIfRunning);
			if (!ret)
				return ret;
		}
		canceled = true;
		cancelMayInterruptRunning = mayInterruptIfRunning;
		return true;
	}

	@Override
	public synchronized T get() throws InterruptedException, ExecutionException {
		while (true) {
			if (done) {
				if (resultException != null) {
					throw new ExecutionException(resultException);
				}
				return result;
			}
			this.wait();
		}
	}

	@Override
	public synchronized T get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		long timeLen = TimeUnit.MILLISECONDS.convert(timeout, unit);
		long deadLine = System.currentTimeMillis() + timeLen;
		// System.err.println("fGHhrhS5 " + timeLen);
		while (true) {
			// System.err.println("dKkQgBjl");
			if (done) {
				if (resultException != null) {
					throw new ExecutionException(resultException);
				}
				return result;
			}
			long t = deadLine - System.currentTimeMillis();
			if (t <= 0) {
				throw new TimeoutException();
			}
			this.wait(t);
		}
	}

	@Override
	public boolean isCancelled() {
		return canceled;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	public synchronized void setCurrentFuture(Future<?> future) {
		this.currentFuture = future;
	}

	public synchronized void runNext() {
		if (runItr.hasNext()) {
			Runnable n = runItr.next();
			n.run();
		} else {
			done = true;
			if (callback != null) {
				callback.completed(result);
			}
			this.notify();
		}
	}

}
