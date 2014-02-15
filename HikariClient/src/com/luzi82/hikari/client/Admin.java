package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.protocol.AdminProtocol;
import com.luzi82.hikari.client.protocol.AdminProtocolDef;

public class Admin extends AdminProtocol {

	public static class CreateAdminUserFuture extends
			GuriFuture<CreateAdminUserCmd.Result> {

		final HsClient client;
		final String secret;

		public CreateAdminUserFuture(final HsClient client,
				final String secret,
				final FutureCallback<CreateAdminUserCmd.Result> futureCallback) {
			super(futureCallback, client.executor);
			this.client = client;
			this.secret = secret;
		}

		public void start() {
			new Step0().start();
		}

		Future<CreateAdminUserCmd.Result> f0;

		class Step0 extends Step {

			@Override
			public void _run() throws Exception {
				f0 = AdminProtocol.createAdminUser(client, secret,
						new Callback<CreateAdminUserCmd.Result>(new Step1()));
				setFuture(f0);
			}

		}

		class Step1 extends Step {

			@Override
			public void _run() throws Exception {
				CreateAdminUserCmd.Result cur = f0.get();
				setFuture(client.put(User.APP_NAME, "username", cur.username,
						new Callback<Void>(new Step2())));
			}

		}

		class Step2 extends Step {

			@Override
			public void _run() throws Exception {
				CreateAdminUserCmd.Result cur = f0.get();
				setFuture(client.put(User.APP_NAME, "password", cur.password,
						new Callback<Void>(new Step3())));
			}

		}

		class Step3 extends Step {

			@Override
			public void _run() throws Exception {
				CreateAdminUserFuture.this.completed(f0.get());
			}

		}

	}

	public static Future<AdminProtocolDef.CreateAdminUserCmd.Result> createAdminUser(
			final HsClient client,
			final String secret,
			final FutureCallback<AdminProtocolDef.CreateAdminUserCmd.Result> futureCallback) {

		CreateAdminUserFuture cuf = new CreateAdminUserFuture(client, secret,
				futureCallback);
		cuf.start();
		return cuf;

	}

}
