package com.luzi82.hikari.client.test;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.Admin;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.AdminProtocol;

public class AdminTest extends AbstractTest {

	@Test
	public void testCreateAdminUser() throws Exception {
		HsClient client = createClient();
		createAdminUser(client);
		User.login(client, null).get();
		Admin.checkAdmin(client, null).get();
	}

	@Test
	public void testCreateAdminUserWithoutBackdoor() throws Exception {
		HsClient client = createClient();

		try {
			AdminProtocol.createAdminUser(client, "asdf", null).get();
			Assert.fail();
		} catch (ExecutionException e) {
		}
		try {
			AdminProtocol.createAdminUser(client, null, null).get();
			Assert.fail();
		} catch (ExecutionException e) {
		}
		try {
			AdminProtocol.createAdminUser(client, null, null).get();
			Assert.fail();
		} catch (ExecutionException e) {
		}
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
