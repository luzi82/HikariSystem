package com.luzi82.hikari.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HsConvertFuture<O, I> implements Future<O> {

	Future<I> input;
	HsConvert<O, I> convert;

	public HsConvertFuture(Future<I> input, HsConvert<O, I> convert) {
		this.input = input;
		this.convert = convert;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return input.cancel(mayInterruptIfRunning);
	}

	@Override
	public O get() throws InterruptedException, ExecutionException {
		try {
			return convert.convert(input.get());
		} catch (InterruptedException e) {
			throw e;
		} catch (ExecutionException e) {
			throw e;
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public O get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		try {
			return convert.convert(input.get(timeout, unit));
		} catch (InterruptedException e) {
			throw e;
		} catch (ExecutionException e) {
			throw e;
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	@Override
	public boolean isCancelled() {
		return input.isCancelled();
	}

	@Override
	public boolean isDone() {
		return input.isDone();
	}

}
