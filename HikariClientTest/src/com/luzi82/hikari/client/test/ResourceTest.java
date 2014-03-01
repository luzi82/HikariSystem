package com.luzi82.hikari.client.test;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.Resource;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.HikariProtocol;

public class ResourceTest extends AbstractTest {

	@Test
	public void testConvert() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Resource.setUserResourceCount(admin, clientUsername, "coin", 10000,
				null).get();

		HikariProtocol.syncStatus(client, null).get();

		Resource.convert(client, "coin_to_gold", 1, null).get();

		Resource.Mgr rMgr = new Resource.Mgr(client);
		Assert.assertEquals(10, rMgr.value("gold", System.currentTimeMillis()));
	}

}
