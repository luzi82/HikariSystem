package com.luzi82.hikari.client.test;

import org.junit.Test;

import com.luzi82.hikari.client.HsClient;

public class ClientTest extends AbstractTest {

	public static String TEST_DEV = "test_dev";

	@Test
	public void testDoubleSync() throws Exception {
		HsClient client = createClient();

		client.syncData(null).get();
		client.syncData(null).get();
	}

}
