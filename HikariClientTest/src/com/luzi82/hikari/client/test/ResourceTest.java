package com.luzi82.hikari.client.test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.Resource;
import com.luzi82.hikari.client.Resource.ConvertEntry;
import com.luzi82.hikari.client.apache.HsClientApache.StatusCodeException;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.HikariProtocol;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ConvertHistory;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ResourceStatus;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ResourceValue;

public class ResourceTest extends AbstractTest {

	@Test
	public void testStatus() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		ResourceStatus resourceStatus = Resource.getResourceStatusObservable(
				client).get();
		Assert.assertTrue(resourceStatus.size() > 0);

		for (ResourceValue resourceValue : resourceStatus.values()) {
			Assert.assertNotNull(resourceValue.resource_key);
		}
	}

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

		Assert.assertEquals(10000,
				Resource.value(client, "coin", System.currentTimeMillis()));
		Assert.assertEquals(0,
				Resource.value(client, "gold", System.currentTimeMillis()));

		Resource.convert(client, "coin_to_gold", 1, null).get();

		Assert.assertEquals(0,
				Resource.value(client, "coin", System.currentTimeMillis()));
		Assert.assertEquals(10,
				Resource.value(client, "gold", System.currentTimeMillis()));
	}

	@Test
	public void testNegativeConvert() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Resource.setUserResourceCount(admin, clientUsername, "coin", 10000,
				null).get();

		HikariProtocol.syncStatus(client, null).get();

		Assert.assertEquals(10000,
				Resource.value(client, "coin", System.currentTimeMillis()));
		Assert.assertEquals(0,
				Resource.value(client, "gold", System.currentTimeMillis()));

		Resource.convert(client, "coin_to_gold", 1, null).get();

		Assert.assertEquals(0,
				Resource.value(client, "coin", System.currentTimeMillis()));
		Assert.assertEquals(10,
				Resource.value(client, "gold", System.currentTimeMillis()));

		StatusCodeException sce = null;
		try {
			Resource.convert(client, "coin_to_gold", -1, null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(400, sce.code);
	}

	@Test
	public void testConvertEntryMap() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();

		Map<String, ConvertEntry> convertEntryMap = Resource
				.getConvertEntryMap(client);
		Assert.assertTrue(convertEntryMap.containsKey("coin_to_gold"));
	}

	@Test
	public void testConvertHistory() throws Exception {
		HsClient client = createClient();

		List<ConvertHistory> convertHistoryList = Resource
				.getConvertHistoryList(client, 10, null).get();
		Assert.assertEquals(0, convertHistoryList.size());

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Resource.setUserResourceCount(admin, clientUsername, "coin", 10000,
				null).get();

		long serverTime0 = client.getServerTime(System.currentTimeMillis());

		Resource.convert(client, "coin_to_gold", 1, null).get();

		long serverTime1 = client.getServerTime(System.currentTimeMillis());

		convertHistoryList = Resource.getConvertHistoryList(client, 10, null)
				.get();
		Assert.assertEquals(1, convertHistoryList.size());
		Assert.assertTrue(convertHistoryList.get(0).time >= serverTime0 - 1000);
		Assert.assertTrue(convertHistoryList.get(0).time <= serverTime1 + 1000);
		Assert.assertEquals("coin_to_gold",
				convertHistoryList.get(0).resource_convert_key);
		Assert.assertEquals("count", convertHistoryList.get(0).count);
	}

}
