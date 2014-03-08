package com.luzi82.hikari.client.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.L10n;

public class L10nTest extends AbstractTest {

	@Test
	public void testText() throws Exception {
		HsClient client = createClient();
		L10n.setL10n(client, "en");
		client.syncData(null).get();

		Map<String, String> langTextMap = L10n.getTextMap(client);
		Assert.assertEquals("Quest 0", langTextMap.get("quest_entry__quest_0"));
		Assert.assertEquals("Coin", langTextMap.get("resource__coin"));
		Assert.assertEquals("Coin to gold",
				langTextMap.get("resource_convert__coin_to_gold"));
	}

}
