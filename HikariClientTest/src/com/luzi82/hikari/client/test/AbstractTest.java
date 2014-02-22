package com.luzi82.hikari.client.test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.HsMemStorage;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.apache.HsClientApache;

public class AbstractTest {

	public static Random RANDOM = new Random();

	public static String SERVER = "http://192.168.1.50";
	public static String TEST_DEV = "test_dev";
	public static String BACKDOOR_FILENAME = "../HikariServer/backdoor.json";
	public static long BACKDOOR_PERIOD = 10000; // 10 sec backdoor open

	public static HsClient createClient() throws Exception {
		ExecutorService executor = Executors.newCachedThreadPool();
		return new HsClient(SERVER, new HsMemStorage(executor), executor,
				new HsClientApache(executor));
	}

	public static void createLogin(HsClient client) throws Exception {
		User.createUser(client, TEST_DEV, null).get(5, TimeUnit.SECONDS);
		User.login(client, null).get(5, TimeUnit.SECONDS);
	}

}
