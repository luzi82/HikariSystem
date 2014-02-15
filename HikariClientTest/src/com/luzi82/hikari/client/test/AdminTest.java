package com.luzi82.hikari.client.test;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.Admin;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.User;

public class AdminTest extends AbstractTest {

	@Test
	public void testCheckAdmin() throws Exception {
		HsClient client = createClient();
		client.put(User.APP_NAME, User.DB_USERNAME, "admin", null).get();
		client.put(User.APP_NAME, User.DB_PASSWORD, "password", null).get();

		User.login(client, null).get();
		Admin.checkAdmin(client, null).get();
	}

	@Test
	public void testNonAdminCheckAdmin() throws Exception {
		HsClient client = createClient();
		User.createUser(client, TEST_DEV, null).get();

		try {
			Admin.checkAdmin(client, null).get();
			Assert.fail();
		} catch (ExecutionException e) {
		}

		User.login(client, null).get();

		try {
			Admin.checkAdmin(client, null).get();
			Assert.fail();
		} catch (ExecutionException e) {
		}
	}

}
