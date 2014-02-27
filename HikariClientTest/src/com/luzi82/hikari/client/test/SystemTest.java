package com.luzi82.hikari.client.test;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.protocol.HikariProtocol;

public class SystemTest extends AbstractTest {

	@Test
	public void testGetTime() throws Exception {
		HsClient client = createClient();

		long systemTime = HikariProtocol.getTime(client, null).get().time;
		Assert.assertTrue(systemTime != 0);
	}

}
