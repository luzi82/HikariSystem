package com.luzi82.hikari.client.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.Card;
import com.luzi82.hikari.client.Gacha;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.Value;
import com.luzi82.hikari.client.protocol.HikariGachaProtocolDef.GachaCmd;
import com.luzi82.hikari.client.protocol.HikariProtocol;

public class GachaTest extends AbstractTest {

	@Test
	public void testGacha() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		HsClient admin = createAdmin();
		Value.setUserValueCount(admin, clientUsername, "gold", 10000, null)
				.get();

		HikariProtocol.syncStatus(client, null).get();

		Assert.assertEquals(10000,
				Value.value(client, "gold", System.currentTimeMillis()));

		GachaCmd.Result gachaResult = Gacha.gacha(client, "gacha_0", null)
				.get();

		Assert.assertEquals(9995,
				Value.value(client, "gold", System.currentTimeMillis()));
		Assert.assertEquals(1, gachaResult.user_card_id_list.size());

		Assert.assertTrue(Card.getCardStatusObservable(client).get()
				.containsKey(gachaResult.user_card_id_list.get(0)));
	}

	@Test
	public void testGachaKeyData() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		Map<String, Gacha.Entry> gachaMap = Gacha.getEntryDict(client);
		Assert.assertTrue(gachaMap.size() >= 1);
		Assert.assertTrue(gachaMap.containsKey("gacha_0"));

		Gacha.Entry gacha = gachaMap.get("gacha_0");
		Assert.assertEquals("gacha_0", gacha.key);
		Assert.assertEquals(1, gacha.valueChangeDict.size());
		Assert.assertEquals("gold",
				gacha.valueChangeDict.get("gold").value_key);
		Assert.assertEquals(-5, gacha.valueChangeDict.get("gold").change);
	}

}
