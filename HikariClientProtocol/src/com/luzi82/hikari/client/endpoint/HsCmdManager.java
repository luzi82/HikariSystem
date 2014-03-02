package com.luzi82.hikari.client.endpoint;

import java.util.List;
import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.RetryableFuture;
import com.luzi82.homuvalue.Value;
import com.luzi82.lang.GuriObservable;

public interface HsCmdManager {

	public <Result> RetryableFuture<Result> sendRequest(String appName,
			String string, Object request, Class<Result> class1,
			FutureCallback<Result> futureCallback);

	public <Status> GuriObservable<Status> getStatusObservable(String appName,
			String string, Class<Status> class1);

	public <Data> List<Data> getDataList(String appName, String dataName,
			Class<Data> class1);

	public void addDataLoad(String appName, String dataName, Class dataClass);

	public <Status> void addStatus(String appName, String statusName,
			Class<Status> statusClass);

}
