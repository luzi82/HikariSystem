package com.luzi82.hikari.client.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.HsMemStorage;
import com.luzi82.hikari.client.HsUser;
import com.luzi82.hikari.client.HsProfile;

public class HsProfileTest {

	public static String SERVER = "http://192.168.1.50";

	public static String TEST_DEV = "test_dev";

	@Test
	public void testSetName() throws InterruptedException, ExecutionException,
			TimeoutException {
		HsClient client = new HsClient(SERVER, new HsMemStorage(
				Executors.newCachedThreadPool()));

		HsUser.createUser(client, TEST_DEV, null).get(5, TimeUnit.SECONDS);
		HsUser.login(client, null).get(5, TimeUnit.SECONDS);

		HsProfile.setName(client, "test_name", null).get(5,
				TimeUnit.SECONDS);

		HsProfile.GetProfileCmd.Result getProfileResult = HsProfile.getProfile(client, null,
				null).get(5, TimeUnit.SECONDS);
		Assert.assertEquals("test_name", getProfileResult.profile.name);
	}

}
