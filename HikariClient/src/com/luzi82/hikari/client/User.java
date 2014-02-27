package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.protocol.HikariUserProtocol;
import com.luzi82.hikari.client.protocol.HikariUserProtocolDef;
import com.luzi82.lang.GuriObservable;

public class User extends HikariUserProtocol {

	public static final String DB_USERNAME = "username";
	public static final String DB_PASSWORD = "password";

	public static class CreateUserFuture extends
			GuriFuture<CreateUserCmd.Result> {

		final HsClient client;
		final String device_model;

		public CreateUserFuture(final HsClient client,
				final String device_model,
				final FutureCallback<CreateUserCmd.Result> futureCallback) {
			super(false, futureCallback, client.executor);
			this.client = client;
			this.device_model = device_model;
		}

		public void fire() {
			new Step0().start();
		}

		Future<CreateUserCmd.Result> f0;

		class Step0 extends Step {

			@Override
			public void _run() throws Exception {
				f0 = HikariUserProtocol.createUser(client, device_model,
						new Callback<CreateUserCmd.Result>(new Step1()));
				setFuture(f0);
			}

		}

		class Step1 extends Step {

			@Override
			public void _run() throws Exception {
				CreateUserCmd.Result cur = f0.get();
				setFuture(client.put(APP_NAME, DB_USERNAME, cur.username,
						new Callback<Void>(new Step2())));
			}

		}

		class Step2 extends Step {

			@Override
			public void _run() throws Exception {
				CreateUserCmd.Result cur = f0.get();
				setFuture(client.put(APP_NAME, DB_PASSWORD, cur.password,
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

	public static Future<HikariUserProtocolDef.CreateUserCmd.Result> createUser(
			final HsClient client,
			final String device_model,
			final FutureCallback<HikariUserProtocolDef.CreateUserCmd.Result> futureCallback) {

		CreateUserFuture cuf = new CreateUserFuture(client, device_model,
				futureCallback);
		cuf.start();
		return cuf;

	}

	public static class LoginFuture extends GuriFuture<LoginCmd.Result> {

		final HsClient client;

		public LoginFuture(final HsClient client,
				final FutureCallback<LoginCmd.Result> futureCallback) {
			super(false, futureCallback, client.executor);
			this.client = client;
		}

		public void fire() {
			new Step0().start();
		}

		Future<String> usernameFuture;
		Future<String> passwordFuture;
		Future<LoginCmd.Result> f2;

		class Step0 extends Step {

			@Override
			public void _run() throws Exception {
				usernameFuture = client.get(APP_NAME, DB_USERNAME,
						new Callback<String>(new Step1()));
				setFuture(usernameFuture);
			}

		}

		class Step1 extends Step {

			@Override
			public void _run() throws Exception {
				passwordFuture = client.get(APP_NAME, DB_PASSWORD,
						new Callback<String>(new Step2()));
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
				loginDoneObservable(client).setNotify(true);
				completed(f2.get());
			}

		}

	}

	public static Future<LoginCmd.Result> login(
			final HsClient client,
			final FutureCallback<HikariUserProtocolDef.LoginCmd.Result> futureCallback) {

		LoginFuture ret = new LoginFuture(client, futureCallback);
		ret.start();
		return ret;
	}

	public static String LOGIN_DONE_KEY = "LOGIN_DONE";

	public static GuriObservable<Boolean> loginDoneObservable(HsClient client) {
		GuriObservable<Boolean> ret = (GuriObservable<Boolean>) client.getTmp(
				APP_NAME, LOGIN_DONE_KEY);
		if (ret == null) {
			ret = new GuriObservable<Boolean>();
			ret.set(false);
			client.putTmp(APP_NAME, LOGIN_DONE_KEY, ret);
		}
		return ret;
	}

}
