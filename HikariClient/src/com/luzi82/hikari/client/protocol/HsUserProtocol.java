package com.luzi82.hikari.client.protocol;

import java.util.concurrent.Future;

import org.apache.http.concurrent.FutureCallback;

import com.luzi82.hikari.client.HsClient;

public class HsUserProtocol {

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

		return client.sendRequest(APP_NAME, "create_user", request,
				CreateUserResult.class, futureCallback);
	}

}
