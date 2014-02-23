package com.luzi82.hikari.client.endpoint;

import java.util.List;
import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.RetryableFuture;
import com.luzi82.homuvalue.Value;

public interface HsCmdManager {

	public <Result> RetryableFuture<Result> sendRequest(String appName,
			String string, Object request, Class<Result> class1,
			FutureCallback<Result> futureCallback);

	public <Status> Value<Status> getStatusValue(String appName, Class<Status> class1);

	public <Data> Future<List<Data>> getDataList(String appName, String string,
			Class<Data> class1, FutureCallback<List<Data>> futureCallback);

	public void addDataLoad(String appName, String dataName);

	public <Status> void addStatus(String appName, Class<Status> statusClass);

}
