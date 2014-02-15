package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.protocol.UserProtocol;
import com.luzi82.hikari.client.protocol.UserProtocolDef;

public class User extends UserProtocol {

	public static final String APP_NAME = UserProtocol.APP_NAME;

	public static class CreateUserFuture extends
			GuriFuture<CreateUserCmd.Result> {

		final HsClient client;
		final String device_model;

		public CreateUserFuture(final HsClient client, final String device_model,
				final FutureCallback<CreateUserCmd.Result> futureCallback) {
			super(futureCallback, client.executor);
			this.client = client;
			this.device_model = device_model;
		}

		public void start() {
			new Step0().start();
		}

		Future<CreateUserCmd.Result> f0;

		class Step0 extends Step {

			@Override
			public void _run() throws Exception {
				f0 = UserProtocol.createUser(client, device_model,
						new Callback<CreateUserCmd.Result>(new Step1()));
				setFuture(f0);
			}

		}

		class Step1 extends Step {

			@Override
			public void _run() throws Exception {
				CreateUserCmd.Result cur = f0.get();
				setFuture(client.put(APP_NAME, "username", cur.username,
						new Callback<Void>(new Step2())));
			}

		}

		class Step2 extends Step {

			@Override
			public void _run() throws Exception {
				CreateUserCmd.Result cur = f0.get();
				setFuture(client.put(APP_NAME, "password", cur.password,
						new Callback<Void>(new Step3())));
			}

		}

		class Step3 extends Step {

			@Override
			public void _run() throws Exception {
				CreateUserFuture.this.completed(f0.get());
			}

		}

	}

	public static Future<UserProtocolDef.CreateUserCmd.Result> createUser(
			final HsClient client,
			final String device_model,
			final FutureCallback<UserProtocolDef.CreateUserCmd.Result> futureCallback) {

		CreateUserFuture cuf = new CreateUserFuture(client, device_model,
				futureCallback);
		cuf.start();
		return cuf;

	}

	public static Future<String> getUsername(HsClient client,
			FutureCallback<String> callback) {
		return client.get(APP_NAME, "username", callback);
	}

	public static Future<String> getPassword(HsClient client,
			FutureCallback<String> callback) {
		return client.get(APP_NAME, "password", callback);
	}

	public static class LoginFuture extends GuriFuture<LoginCmd.Result> {

		final HsClient client;

		public LoginFuture(final HsClient client,
				final FutureCallback<LoginCmd.Result> futureCallback) {
			super(futureCallback, client.executor);
			this.client = client;
		}

		public void start() {
			new Step0().start();
		}

		Future<String> usernameFuture;
		Future<String> passwordFuture;
		Future<LoginCmd.Result> f2;

		class Step0 extends Step {

			@Override
			public void _run() throws Exception {
				usernameFuture = getUsername(client, new Callback<String>(
						new Step1()));
				setFuture(usernameFuture);
			}

		}

		class Step1 extends Step {

			@Override
			public void _run() throws Exception {
				passwordFuture = getPassword(client, new Callback<String>(
						new Step2()));
				setFuture(passwordFuture);
			}

		}

		class Step2 extends Step {

			@Override
			public void _run() throws Exception {
				String username = usernameFuture.get();
				String password = passwordFuture.get();
				f2 = login(client, username, password,
						new Callback<LoginCmd.Result>(new Step3()));
				setFuture(f2);
			}

		}

		class Step3 extends Step {

			@Override
			public void _run() throws Exception {
				completed(f2.get());
			}

		}

	}

	public static Future<LoginCmd.Result> login(
			final HsClient client,
			final FutureCallback<UserProtocolDef.LoginCmd.Result> futureCallback) {

		LoginFuture ret = new LoginFuture(client, futureCallback);
		ret.start();
		return ret;
	}

}