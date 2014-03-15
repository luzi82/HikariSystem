package com.luzi82.hikari.client.test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.MailBox;
import com.luzi82.hikari.client.Value;
import com.luzi82.hikari.client.Value.ConvertEntry;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.apache.HsClientApache.StatusCodeException;
import com.luzi82.hikari.client.protocol.HikariMailProtocolDef.Mail;
import com.luzi82.hikari.client.protocol.HikariProtocol;
import com.luzi82.hikari.client.protocol.HikariValueProtocolDef.ChangeHistory;
import com.luzi82.hikari.client.protocol.HikariValueProtocolDef.ConvertHistory;
import com.luzi82.hikari.client.protocol.HikariValueProtocolDef.ValueStatus;
import com.luzi82.hikari.client.protocol.HikariValueProtocolDef.UserValue;
import com.luzi82.hikari.client.protocol.Item;

public class ValueTest extends AbstractTest {

	@Test
	public void testStatus() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		ValueStatus valueStatus = Value.getValueStatusObservable(
				client).get();
		Assert.assertTrue(valueStatus.size() > 0);

		for (UserValue valueValue : valueStatus.values()) {
			Assert.assertNotNull(valueValue.value_key);
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
		Value.setUserValueCount(admin, clientUsername, "coin", 10000,
				null).get();

		HikariProtocol.syncStatus(client, null).get();

		Assert.assertEquals(10000,
				Value.value(client, "coin", System.currentTimeMillis()));
		Assert.assertEquals(0,
				Value.value(client, "gold", System.currentTimeMillis()));

		Value.convert(client, "coin_to_gold", 1, null).get();

		Assert.assertEquals(0,
				Value.value(client, "coin", System.currentTimeMillis()));
		Assert.assertEquals(10,
				Value.value(client, "gold", System.currentTimeMillis()));
	}

	@Test
	public void testNegativeConvert() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Value.setUserValueCount(admin, clientUsername, "coin", 10000,
				null).get();

		HikariProtocol.syncStatus(client, null).get();

		Assert.assertEquals(10000,
				Value.value(client, "coin", System.currentTimeMillis()));
		Assert.assertEquals(0,
				Value.value(client, "gold", System.currentTimeMillis()));

		Value.convert(client, "coin_to_gold", 1, null).get();

		Assert.assertEquals(0,
				Value.value(client, "coin", System.currentTimeMillis()));
		Assert.assertEquals(10,
				Value.value(client, "gold", System.currentTimeMillis()));

		StatusCodeException sce = null;
		try {
			Value.convert(client, "coin_to_gold", -1, null).get();
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

		Map<String, ConvertEntry> convertEntryMap = Value
				.getConvertEntryMap(client);
		Assert.assertTrue(convertEntryMap.containsKey("coin_to_gold"));
	}

	@Test
	public void testConvertHistory() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		List<ConvertHistory> convertHistoryList = Value
				.getConvertHistoryList(client, 0, 10, null).get();
		Assert.assertEquals(0, convertHistoryList.size());

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Value.setUserValueCount(admin, clientUsername, "coin", 10000,
				null).get();

		long serverTime0 = client.getServerTime(System.currentTimeMillis());

		Value.convert(client, "coin_to_gold", 1, null).get();

		long serverTime1 = client.getServerTime(System.currentTimeMillis());

		convertHistoryList = Value
				.getConvertHistoryList(client, 0, 10, null).get();
		Assert.assertEquals(1, convertHistoryList.size());
		Assert.assertTrue(convertHistoryList.get(0).time >= serverTime0 - 1000);
		Assert.assertTrue(convertHistoryList.get(0).time <= serverTime1 + 1000);
		Assert.assertEquals("coin_to_gold",
				convertHistoryList.get(0).value_convert_key);
		Assert.assertEquals(1, convertHistoryList.get(0).count);
	}

	@Test
	public void testChangeHistory() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		HsClient client = createClient();
		createLogin(client);

		List<ChangeHistory> changeHistoryList = Value.getChangeHistoryList(
				client, "coin", 0, 10, null).get();
		Assert.assertEquals(0, changeHistoryList.size());
		changeHistoryList = Value.getChangeHistoryList(client, "gold", 0,
				10, null).get();
		Assert.assertEquals(0, changeHistoryList.size());

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Value.setUserValueCount(admin, clientUsername, "coin", 10000,
				null).get();

		long serverTime0 = client.getServerTime(System.currentTimeMillis());

		Value.convert(client, "coin_to_gold", 1, null).get();

		long serverTime1 = client.getServerTime(System.currentTimeMillis());

		changeHistoryList = Value.getChangeHistoryList(client, "gold", 0,
				10, null).get();
		Assert.assertEquals(1, changeHistoryList.size());
		Assert.assertTrue(changeHistoryList.get(0).time >= serverTime0 - 1000);
		Assert.assertTrue(changeHistoryList.get(0).time <= serverTime1 + 1000);
		Assert.assertEquals("convert",
				changeHistoryList.get(0).change_reason_key);
		Assert.assertEquals("gold", changeHistoryList.get(0).value_key);
		Assert.assertEquals(10, changeHistoryList.get(0).value);
		Map<String, Object> msg = objectMapper.readValue(
				changeHistoryList.get(0).change_reason_msg, Map.class);
		Assert.assertEquals("coin_to_gold", msg.get("value_convert_key"));

		changeHistoryList = Value.getChangeHistoryList(client, "coin", 0,
				10, null).get();
		Assert.assertEquals(0, changeHistoryList.size());
	}

	@Test
	public void testGiftValue() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		HsClient admin = createAdmin();

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();
		Item.ListMap itemListMap = new Item.ListMap();
		Value.addValueItem(itemListMap, "coin", 689, "reason_key",
				"reason_msg");

		MailBox.sendGiftMail(admin, clientUsername, "HelloTitle", "HelloWorld",
				itemListMap, null).get();

		List<Mail> mailList = MailBox.getMailList(client, true, true, 0, 10,
				null).get();
		Assert.assertEquals(1, mailList.size());

		List<Value.ValueItem> mailAttachList = Value
				.getValueItemList(client, mailList.get(0).item_list_map);
		Assert.assertEquals(1, mailAttachList.size());
		Assert.assertEquals("coin", mailAttachList.get(0).value_key);
		Assert.assertEquals(689, mailAttachList.get(0).value);
		Assert.assertEquals("reason_key",
				mailAttachList.get(0).change_reason_key);
		Assert.assertEquals("reason_msg",
				mailAttachList.get(0).change_reason_msg);

		long coin0 = Value.value(client, "coin", System.currentTimeMillis());

		MailBox.setRead(client, mailList.get(0).id, true, null).get();

		long coin1 = Value.value(client, "coin", System.currentTimeMillis());

		Assert.assertEquals(coin1, coin0 + 689);
	}
}
