package com.luzi82.hikari.client.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.HsMemStorage;
import com.luzi82.hikari.client.apache.HsClientApache;
import com.luzi82.hikari.client.protocol.SystemProtocol;

public class SystemTest {

	public static String SERVER = "http://192.168.1.50";

	public static String TEST_DEV = "test_dev";

	@Test
	public void testGetTime() throws Exception {
		HsClient client = createClient();

		long systemTime = SystemProtocol.getTime(client, null).get().time;
		Assert.assertTrue(systemTime != 0);
	}

	public static HsClient createClient() {
		ExecutorService executor = Executors.newCachedThreadPool();
		return new HsClient(SERVER, new HsMemStorage(executor), executor,
				new HsClientApache(executor));
	}

}
