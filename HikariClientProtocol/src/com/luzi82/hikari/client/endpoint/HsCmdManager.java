package com.luzi82.hikari.client.endpoint;

import java.util.List;
import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.RetryableFuture;

public interface HsCmdManager {

	public <Result> RetryableFuture<Result> sendRequest(String appName, String string,
			Object request, Class<Result> class1,
			FutureCallback<Result> futureCallback);

	public <Data> Future<List<Data>> getDataList(String appName, String string,
			Class<Data> class1, FutureCallback<List<Data>> futureCallback);

	public void addDataLoad(String appName, String dataName);

}
