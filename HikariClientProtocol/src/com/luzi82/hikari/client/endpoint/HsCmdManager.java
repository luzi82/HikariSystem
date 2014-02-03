package com.luzi82.hikari.client.endpoint;

import java.util.concurrent.Future;

import org.apache.http.concurrent.FutureCallback;

public interface HsCmdManager {

	public <Result> Future<Result> sendRequest(String appName,
			String string, Object request, Class<Result> class1,
			FutureCallback<Result> futureCallback);

}
