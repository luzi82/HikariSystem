package com.luzi82.hikari.client.test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.MailBox;
import com.luzi82.hikari.client.Resource;
import com.luzi82.hikari.client.Resource.ConvertEntry;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.apache.HsClientApache.StatusCodeException;
import com.luzi82.hikari.client.protocol.HikariMailProtocolDef.Mail;
import com.luzi82.hikari.client.protocol.HikariProtocol;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ChangeHistory;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ConvertHistory;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ResourceStatus;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ResourceValue;
import com.luzi82.hikari.client.protocol.Item;

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
		createLogin(client);

		List<ConvertHistory> convertHistoryList = Resource
				.getConvertHistoryList(client, 0, 10, null).get();
		Assert.assertEquals(0, convertHistoryList.size());

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Resource.setUserResourceCount(admin, clientUsername, "coin", 10000,
				null).get();

		long serverTime0 = client.getServerTime(System.currentTimeMillis());

		Resource.convert(client, "coin_to_gold", 1, null).get();

		long serverTime1 = client.getServerTime(System.currentTimeMillis());

		convertHistoryList = Resource
				.getConvertHistoryList(client, 0, 10, null).get();
		Assert.assertEquals(1, convertHistoryList.size());
		Assert.assertTrue(convertHistoryList.get(0).time >= serverTime0 - 1000);
		Assert.assertTrue(convertHistoryList.get(0).time <= serverTime1 + 1000);
		Assert.assertEquals("coin_to_gold",
				convertHistoryList.get(0).resource_convert_key);
		Assert.assertEquals(1, convertHistoryList.get(0).count);
	}

	@Test
	public void testChangeHistory() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		HsClient client = createClient();
		createLogin(client);

		List<ChangeHistory> changeHistoryList = Resource.getChangeHistoryList(
				client, "coin", 0, 10, null).get();
		Assert.assertEquals(0, changeHistoryList.size());
		changeHistoryList = Resource.getChangeHistoryList(client, "gold", 0,
				10, null).get();
		Assert.assertEquals(0, changeHistoryList.size());

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Resource.setUserResourceCount(admin, clientUsername, "coin", 10000,
				null).get();

		long serverTime0 = client.getServerTime(System.currentTimeMillis());

		Resource.convert(client, "coin_to_gold", 1, null).get();

		long serverTime1 = client.getServerTime(System.currentTimeMillis());

		changeHistoryList = Resource.getChangeHistoryList(client, "gold", 0,
				10, null).get();
		Assert.assertEquals(1, changeHistoryList.size());
		Assert.assertTrue(changeHistoryList.get(0).time >= serverTime0 - 1000);
		Assert.assertTrue(changeHistoryList.get(0).time <= serverTime1 + 1000);
		Assert.assertEquals("convert",
				changeHistoryList.get(0).change_reason_key);
		Assert.assertEquals("gold", changeHistoryList.get(0).resource_key);
		Assert.assertEquals(10, changeHistoryList.get(0).count);
		Map<String, Object> msg = objectMapper.readValue(
				changeHistoryList.get(0).msg, Map.class);
		Assert.assertEquals("coin_to_gold", msg.get("resource_convert_key"));

		changeHistoryList = Resource.getChangeHistoryList(client, "coin", 0,
				10, null).get();
		Assert.assertEquals(0, changeHistoryList.size());
	}

	@Test
	public void testGiftResource() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		HsClient admin = createAdmin();

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();
		Item.ListMap itemListMap = new Item.ListMap();
		Resource.addResourceItem(itemListMap, "coin", 689, "reason_key",
				"reason_msg");

		MailBox.sendGiftMail(admin, clientUsername, "HelloTitle", "HelloWorld",
				itemListMap, null).get();

		List<Mail> mailList = MailBox.getMailList(client, true, true, 0, 10,
				null).get();
		Assert.assertEquals(1, mailList.size());

		List<Resource.ResourceItem> mailAttachList = Resource
				.getResourceItemList(client, mailList.get(0).item_list_map);
		Assert.assertEquals(1, mailAttachList.size());
		Assert.assertEquals("coin", mailAttachList.get(0).resource_key);
		Assert.assertEquals(689, mailAttachList.get(0).value);
		Assert.assertEquals("reason_key",
				mailAttachList.get(0).change_reason_key);
		Assert.assertEquals("reason_msg",
				mailAttachList.get(0).change_reason_msg);

		long coin0 = Resource.value(client, "coin", System.currentTimeMillis());

		MailBox.setRead(client, mailList.get(0).id, true, null).get();

		long coin1 = Resource.value(client, "coin", System.currentTimeMillis());

		Assert.assertEquals(coin1, coin0 + 689);
	}
}
