package com.luzi82.hikari.client.endpoint;

import java.util.concurrent.Future;

import org.apache.http.concurrent.FutureCallback;

import com.luzi82.hikari.client.protocol.HsUserProtocol.CreateUser;

public class HsUserEp {
	
	public static final String APP_NAME = "hs_user";

	public static Future<CreateUser.Result> createUser(
			final HsCmdManager cmdManager, String device_id,
			final FutureCallback<CreateUser.Result> futureCallback) {
		final CreateUser.Request request = new CreateUser.Request();
		request.device_id = device_id;

		return cmdManager.sendRequest(APP_NAME, "create_user", request,
				CreateUser.Result.class, futureCallback);
	}

}
