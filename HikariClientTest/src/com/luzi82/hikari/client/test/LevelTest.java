package com.luzi82.hikari.client.test;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.Value;

public class LevelTest extends AbstractTest {

	@Test
	public void testLevelUp() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		long now = System.currentTimeMillis();

		Assert.assertEquals(0, Value.value(client, "exp", now));
		Assert.assertEquals(1, Value.value(client, "lv", now));

		int questInstanceId;
		questInstanceId = Quest.questStart(client, "quest_0", null).get().quest_instance.id;
		Quest.questEnd(client, questInstanceId, true, null).get();

		now = System.currentTimeMillis();

		Assert.assertEquals(100, Value.value(client, "exp", now));
		Assert.assertEquals(2, Value.value(client, "lv", now));
	}

}
