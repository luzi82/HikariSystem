package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import org.apache.http.concurrent.FutureCallback;

import com.luzi82.hikari.client.protocol.HsUserProtocol;
import com.luzi82.hikari.client.protocol.HsUserProtocolDef;

public class HsUser extends HsUserProtocol {

	public static final String APP_NAME = HsUserProtocol.APP_NAME;

	public static Future<HsUserProtocolDef.CreateUserCmd.Result> createUser(
			final HsClient client,
			final String device_id,
			final FutureCallback<HsUserProtocolDef.CreateUserCmd.Result> futureCallback) {

		final HsStepListFuture<HsUserProtocolDef.CreateUserCmd.Result> slf = new HsStepListFuture<HsUserProtocolDef.CreateUserCmd.Result>(
				futureCallback);
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("0Iffc95z");
				slf.setCurrentFuture(HsUserProtocol.createUser(
						client,
						device_id,
						slf.createFutureCallback(HsUserProtocolDef.CreateUserCmd.Result.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("kogGqQzW");
				HsUserProtocolDef.CreateUserCmd.Result cur = (HsUserProtocolDef.CreateUserCmd.Result) slf.resultAry[0];
				slf.setResult(cur);
				slf.setCurrentFuture(client.put(APP_NAME, "username",
						cur.username, slf.createFutureCallback(Void.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				HsUserProtocolDef.CreateUserCmd.Result cur = (HsUserProtocolDef.CreateUserCmd.Result) slf.resultAry[0];
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

	public static Future<LoginCmd.Result> login(final HsClient client,
			final FutureCallback<HsUserProtocolDef.LoginCmd.Result> futureCallback) {

		final HsStepListFuture<LoginCmd.Result> slf = new HsStepListFuture<LoginCmd.Result>(
				futureCallback);

		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("yjSiLWd8");
				slf.setCurrentFuture(getUsername(client,
						slf.createFutureCallback(String.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("03gx0S99");
				slf.setCurrentFuture(getPassword(client,
						slf.createFutureCallback(String.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("IJB19rab");
				String username = (String) slf.resultAry[0];
				String password = (String) slf.resultAry[1];
				slf.setCurrentFuture(login(client, username, password,
						slf.createFutureCallback(LoginCmd.Result.class)));
			}
		});
		slf.addRunnable(new Runnable() {
			@Override
			public void run() {
				System.err.println("vUtP0tj3");
				slf.setResult((LoginCmd.Result) slf.resultAry[2]);
				slf.runNext();
			}
		});
		slf.start();

		return slf;
	}

}
