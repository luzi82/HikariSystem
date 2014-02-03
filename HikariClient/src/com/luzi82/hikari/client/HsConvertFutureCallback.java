package com.luzi82.hikari.client;

import org.apache.http.concurrent.FutureCallback;

public class HsConvertFutureCallback<O, I> implements FutureCallback<I> {

	FutureCallback<O> output;
	HsConvert<O, I> convert;

	public HsConvertFutureCallback(FutureCallback<O> output,
			HsConvert<O, I> convert) {
		this.output = output;
		this.convert = convert;
	}

	@Override
	public void cancelled() {
		output.cancelled();
	}

	@Override
	public void completed(I arg0) {
		try {
			output.completed(convert.convert(arg0));
		} catch (Exception e) {
			failed(e);
		}
	}

	@Override
	public void failed(Exception arg0) {
		output.failed(arg0);
	}

}
