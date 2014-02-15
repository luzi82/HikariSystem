package com.luzi82.hikari.client.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.binary.Hex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzi82.hikari.client.Admin;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.HsMemStorage;
import com.luzi82.hikari.client.apache.HsClientApache;
import com.luzi82.hikari.client.protocol.SystemProtocol;

public class AbstractTest {

	public static Random RANDOM = new Random();

	public static String SERVER = "http://192.168.1.50";
	public static String TEST_DEV = "test_dev";
	public static String BACKDOOR_FILENAME = "../HikariServer/backdoor.json";
	public static long BACKDOOR_PERIOD = 10000; // 10 sec backdoor open

	public static HsClient createClient() {
		ExecutorService executor = Executors.newCachedThreadPool();
		return new HsClient(SERVER, new HsMemStorage(executor), executor,
				new HsClientApache(executor));
	}

	public static void createAdminUser(HsClient client) throws Exception {
		File backdoorFile = new File(BACKDOOR_FILENAME);
		backdoorFile.deleteOnExit();

		byte[] secretByte = new byte[32];
		RANDOM.nextBytes(secretByte);
		String secret = Hex.encodeHexString(secretByte);

		long serverNow = SystemProtocol.getTime(client, null).get().time;

		Map<String, Object> backdoorData = new HashMap<String, Object>();
		backdoorData.put("secret", secret);
		backdoorData.put("deadline", serverNow + BACKDOOR_PERIOD);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(backdoorFile, backdoorData);

		Admin.createAdminUser(client, secret, null).get();

		backdoorFile.delete();
	}

}
