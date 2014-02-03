package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import org.apache.http.concurrent.FutureCallback;

public class HsUser {

	public static final String APP_NAME = "hs_user";

	public static class CreateUserRequest {
		public String device_id;
	}

	public static class CreateUserResult {
		public String username;
		public String password;
	}

	public static Future<CreateUserResult> createUser(final HsClient client,
			String device_id,
			final FutureCallback<CreateUserResult> futureCallback) {
		final CreateUserRequest request = new CreateUserRequest();
		request.device_id = device_id;
		// return client.sendRequest(APP_NAME, "create_user", request,
		// CreateUserResult.class,
		// new HsPassFutureCallback<CreateUserResult>(futureCallback) {
		// @Override
		// public void completed(CreateUserResult arg0) {
		// client.put(APP_NAME, "username", arg0.username);
		// client.put(APP_NAME, "password", arg0.password);
		// super.completed(arg0);
		// }
		// });

		final HsStepListFuture<CreateUserResult> slf = new HsStepListFuture<CreateUserResult>(
				futureCallback);
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("0Iffc95z");
				slf.setCurrentFuture(client.sendRequest(APP_NAME,
						"create_user", request, CreateUserResult.class,
						slf.createFutureCallback(CreateUserResult.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("kogGqQzW");
				CreateUserResult cur = (CreateUserResult) slf.resultAry[0];
				slf.setResult(cur);
				slf.setCurrentFuture(client.put(APP_NAME, "username",
						cur.username, slf.createFutureCallback(Void.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				CreateUserResult cur = (CreateUserResult) slf.resultAry[0];
				slf.setCurrentFuture(client.put(APP_NAME, "password",
						cur.password, slf.createFutureCallback(Void.class)));
			}
		});
		slf.start();

		return slf;

	}

	public static Future<String> getUsername(HsClient client,
			FutureCallback<String> callback) {
		return client.get(APP_NAME, "username", callback);
	}

	public static Future<String> getPassword(HsClient client,
			FutureCallback<String> callback) {
		return client.get(APP_NAME, "password", callback);
	}

}
