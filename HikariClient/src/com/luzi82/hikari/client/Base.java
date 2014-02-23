package com.luzi82.hikari.client;

import java.util.concurrent.Future;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.protocol.SystemProtocol;

public class Base extends SystemProtocol {

	public static final String TMPKEY_TIMEOFFSET = "timeoffset";

	public static Future<Void> syncTime(HsClient client,
			FutureCallback<Void> callback) {
		SyncTimeFuture ret = new SyncTimeFuture(client, callback);
		ret.start();
		return ret;
	}

	public static class SyncTimeFuture extends GuriFuture<Void> {

		final HsClient client;

		public SyncTimeFuture(HsClient client, FutureCallback<Void> callback) {
			super(false, callback, client.executor);
			this.client = client;
		}

		@Override
		protected void fire() {
			new Step0().start();
		}

		public class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				setFuture(getTime(client, new Callback<GetTimeCmd.Result>(
						new Step1())));
			}
		}

		public class Step1 extends Step {
			@Override
			public void _run() throws Exception {
				GetTimeCmd.Result res = (GetTimeCmd.Result) lastFutureResult;
				long now = System.currentTimeMillis();
				client.putTmp(APP_NAME, TMPKEY_TIMEOFFSET, new Long(res.time
						- now));
				completed(null);
			}
		}

	}

	public static long getServerTime(HsClient client) {
		Long offset = (Long) client.getTmp(APP_NAME, TMPKEY_TIMEOFFSET);
		if (offset == null) {
			throw new IllegalStateException();
		}
		return System.currentTimeMillis() + offset;
	}

}
