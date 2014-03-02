package com.luzi82.hikari.client.test;

import java.util.List;

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

		Assert.assertEquals(10,
				Resource.value(client, "gold", System.currentTimeMillis()));
	}

	@Test
	public void testConvertList() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		List<String> convertList = Resource.getConvertList(client);
		Assert.assertTrue(convertList.size() >= 1);
		Assert.assertEquals("coin_to_gold", convertList.get(0));

		if (convertList.size() > 1) {
			for (int i = 1; i < convertList.size(); ++i) {
				Assert.assertNotEquals("coin_to_gold", convertList.get(i));
			}
		}
	}

}
