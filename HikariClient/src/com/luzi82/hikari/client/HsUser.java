package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import org.apache.http.concurrent.FutureCallback;

import com.luzi82.hikari.client.endpoint.HsUserEp;
import com.luzi82.hikari.client.protocol.HsUserProtocol;

public class HsUser {

	public static final String APP_NAME = HsUserEp.APP_NAME;

	public static Future<HsUserProtocol.CreateUser.Result> createUser(
			final HsClient client,
			final String device_id,
			final FutureCallback<HsUserProtocol.CreateUser.Result> futureCallback) {

		final HsStepListFuture<HsUserProtocol.CreateUser.Result> slf = new HsStepListFuture<HsUserProtocol.CreateUser.Result>(
				futureCallback);
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("0Iffc95z");
				slf.setCurrentFuture(HsUserEp.createUser(
						client,
						device_id,
						slf.createFutureCallback(HsUserProtocol.CreateUser.Result.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("kogGqQzW");
				HsUserProtocol.CreateUser.Result cur = (HsUserProtocol.CreateUser.Result) slf.resultAry[0];
				slf.setResult(cur);
				slf.setCurrentFuture(client.put(APP_NAME, "username",
						cur.username, slf.createFutureCallback(Void.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				HsUserProtocol.CreateUser.Result cur = (HsUserProtocol.CreateUser.Result) slf.resultAry[0];
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
