package com.luzi82.hikari.client.test;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.protocol.SystemProtocol;

public class CacheTest extends AbstractTest {

	@Test
	public void testGetTime() throws Exception {
		HsClient client = createClient();

		createLogin(client);

		String seqId = client.getCookie("seqid");
		Assert.assertNotNull(seqId);

		long systemTime0 = SystemProtocol.getTime(client, null).get().time;
		Assert.assertTrue(systemTime0 != 0);
		String seqId2 = client.getCookie("seqid");

		client.setCookie("seqid", seqId);

		long systemTime1 = SystemProtocol.getTime(client, null).get().time;
		Assert.assertEquals(systemTime0, systemTime1);

		Assert.assertEquals(seqId2, client.getCookie("seqid"));

		client.setCookie("seqid", seqId);

		systemTime1 = SystemProtocol.getTime(client, null).get().time;
		Assert.assertEquals(systemTime0, systemTime1);

		Assert.assertEquals(seqId2, client.getCookie("seqid"));
	}
}
