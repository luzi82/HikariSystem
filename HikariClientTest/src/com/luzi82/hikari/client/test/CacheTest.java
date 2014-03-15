package com.luzi82.hikari.client.test;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.concurrent.RecordFutureCallback;
import com.luzi82.concurrent.RetryableFuture;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.protocol.HikariDevProtocol;
import com.luzi82.hikari.client.protocol.HikariDevProtocolDef.PassTimeCmd;
import com.luzi82.hikari.client.protocol.HikariProtocol;

public class CacheTest extends AbstractTest {

	@Test
	public void testGetTime() throws Exception {
		HsClient client = createClient();

		createLogin(client);

		String seqId = client.getCookie("seqid");
		Assert.assertNotNull(seqId);

		long systemTime0 = HikariProtocol.getTime(client, null).get().time;
		Assert.assertTrue(systemTime0 != 0);
		String seqId2 = client.getCookie("seqid");

		client.setCookie("seqid", seqId);

		long systemTime1 = HikariProtocol.getTime(client, null).get().time;
		Assert.assertEquals(systemTime0, systemTime1);

		Assert.assertEquals(seqId2, client.getCookie("seqid"));

		client.setCookie("seqid", seqId);

		systemTime1 = HikariProtocol.getTime(client, null).get().time;
		Assert.assertEquals(systemTime0, systemTime1);

		Assert.assertEquals(seqId2, client.getCookie("seqid"));
	}

	@Test
	public void testRetry() throws Exception {
		HsClient client = createClient();

		createLogin(client);

		long serverTime = HikariProtocol.getTime(client, null).get().time;
		long clientTime = System.currentTimeMillis();

		RecordFutureCallback<PassTimeCmd.Result> rfc = new RecordFutureCallback<PassTimeCmd.Result>(
				null);
		RetryableFuture<PassTimeCmd.Result> retryableFuture = HikariDevProtocol
				.passTime(client, serverTime + 1000, -1, rfc);

		try {
			retryableFuture.get();
			Assert.fail();
		} catch (ExecutionException ee) {
		}

		Assert.assertFalse(rfc.completed);
		Assert.assertTrue(rfc.failed);
		Assert.assertFalse(rfc.cancelled);
		Assert.assertNull(rfc.result);
		Assert.assertNotNull(rfc.ex);

		PassTimeCmd.Result result = null;
		while (System.currentTimeMillis() < clientTime + 2000) {
			rfc.clear();
			try {
				retryableFuture.retry();
				result = retryableFuture.get();
				break;
			} catch (ExecutionException ee) {
			}
		}

		Assert.assertTrue(rfc.completed);
		Assert.assertFalse(rfc.failed);
		Assert.assertFalse(rfc.cancelled);
		Assert.assertSame(result, rfc.result);
		Assert.assertNull(rfc.ex);
	}

	@Test
	public void testBadRetry() throws Exception {
		HsClient client = createClient();

		createLogin(client);

		long serverTime = HikariProtocol.getTime(client, null).get().time;

		RecordFutureCallback<PassTimeCmd.Result> rfc = new RecordFutureCallback<PassTimeCmd.Result>(
				null);
		RetryableFuture<PassTimeCmd.Result> retryableFuture = HikariDevProtocol
				.passTime(client, serverTime + 1000, -1, rfc);

		try {
			retryableFuture.get();
			Assert.fail();
		} catch (ExecutionException ee) {
		}

		Assert.assertFalse(rfc.completed);
		Assert.assertTrue(rfc.failed);
		Assert.assertFalse(rfc.cancelled);
		Assert.assertNull(rfc.result);
		Assert.assertNotNull(rfc.ex);

		// call appear in between
		HikariProtocol.getTime(client, null).get();

		rfc.clear();
		try {
			retryableFuture.retry();
			retryableFuture.get();
			Assert.fail();
		} catch (IllegalStateException ise) {
		}

	}

}
