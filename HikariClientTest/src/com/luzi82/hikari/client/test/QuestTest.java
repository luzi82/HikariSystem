package com.luzi82.hikari.client.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.Resource;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestStartCmd;

public class QuestTest extends AbstractTest {

	@Test
	public void testQuestPlay() throws Exception {
		HsClient client = createClient();

		client.syncData(null).get();

		User.createUser(client, TEST_DEV, null).get(5, TimeUnit.SECONDS);
		User.login(client, null).get(5, TimeUnit.SECONDS);

		List<Quest.QuestEntryData> questEntryList = Quest
				.getQuestEntryDataList(client);
		Quest.QuestEntryData questEntry = questEntryList.get(0);

		int questInstanceId;

		// win game
		questInstanceId = Quest.questStart(client, questEntry.key, null).get().quest_instance.id;
		Quest.questEnd(client, questInstanceId, true, null).get();

		// lose game
		questInstanceId = Quest.questStart(client, questEntry.key, null).get().quest_instance.id;
		Quest.questEnd(client, questInstanceId, false, null).get();

	}

	@Test
	public void testDoubleQuestStart() throws Exception {
		HsClient client = createClient();

		client.syncData(null).get();

		User.createUser(client, TEST_DEV, null).get(5, TimeUnit.SECONDS);
		User.login(client, null).get(5, TimeUnit.SECONDS);

		List<Quest.QuestEntryData> questEntryList = Quest
				.getQuestEntryDataList(client);
		Quest.QuestEntryData questEntry = questEntryList.get(0);

		int questInstanceId0;
		int questInstanceId1;

		questInstanceId0 = Quest.questStart(client, questEntry.key, null).get().quest_instance.id;
		questInstanceId1 = Quest.questStart(client, questEntry.key, null).get().quest_instance.id;

		System.err.println(client.getCookie("seqid"));

		Assert.assertNotEquals(questInstanceId0, questInstanceId1);

		try {
			Quest.questEnd(client, questInstanceId0, true, null).get();
			Assert.fail();
		} catch (ExecutionException e) {
		}

		System.err.println(client.getCookie("seqid"));

		Quest.questEnd(client, questInstanceId1, true, null).get();
	}

	@Test
	public void testDoubleQuestEnd() throws Exception {
		HsClient client = createClient();

		client.syncData(null).get();

		User.createUser(client, TEST_DEV, null).get(5, TimeUnit.SECONDS);
		User.login(client, null).get(5, TimeUnit.SECONDS);

		List<Quest.QuestEntryData> questEntryList = Quest
				.getQuestEntryDataList(client);
		Quest.QuestEntryData questEntry = questEntryList.get(0);

		int questInstanceId;

		questInstanceId = Quest.questStart(client, questEntry.key, null).get().quest_instance.id;

		Quest.questEnd(client, questInstanceId, true, null).get();
		try {
			Quest.questEnd(client, questInstanceId, true, null).get();
			Assert.fail();
		} catch (ExecutionException e) {
		}
	}

	@Test
	public void testQuestCost() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		// Resource.Mgr resMgr = new Resource.Mgr(client);

		List<Quest.QuestEntryData> questEntryList = Quest
				.getQuestEntryDataList(client);
		Quest.QuestEntryData questEntry = questEntryList.get(0);

		List<Quest.QuestCostData> allQuestCostList = Quest
				.getQuestCostDataList(client);
		List<Quest.QuestCostData> questCostList = Quest.filter(
				allQuestCostList, questEntry.key);

		// Resource.Status resourceStatus0 =
		// Resource.getStatusValue(client).get();
		// Assert.assertNotNull(resourceStatus0);

		long now;
		now = System.currentTimeMillis();

		Map<String, Long> oldValue = new HashMap<String, Long>();

		for (Quest.QuestCostData questCost : questCostList) {
			String key = questCost.resource_key;
			long count0 = Resource.value(client, key, now);
			int cost = questCost.count;
			Assert.assertTrue(count0 >= cost);
			oldValue.put(key, count0);
		}

		Quest.questStart(client, questEntry.key, null).get();

		now = System.currentTimeMillis();

		for (Quest.QuestCostData questCost : questCostList) {
			String key = questCost.resource_key;
			long count0 = oldValue.get(key);
			long count1 = Resource.value(client, key, now);
			int cost = questCost.count;
			System.err.println("count0 " + count0);
			System.err.println("count1 " + count1);
			Assert.assertTrue(count1 < count0);
			Assert.assertTrue(count1 >= count0 - cost);
		}
	}

	@Test
	public void testQuestReward() throws Exception {
		HsClient client = createClient();
		client.syncData(null).get();
		createLogin(client);

		// Resource.Mgr resMgr = new Resource.Mgr(client);

		List<Quest.QuestEntryData> questEntryList = Quest
				.getQuestEntryDataList(client);
		Quest.QuestEntryData questEntry = questEntryList.get(0);

		long now;
		now = System.currentTimeMillis();

		long oldCoin = Resource.value(client, "coin", now);

		QuestStartCmd.Result startResult = Quest.questStart(client,
				questEntry.key, null).get();
		Quest.questEnd(client, startResult.quest_instance.id, true, null).get();

		now = System.currentTimeMillis();

		long newCoin = Resource.value(client, "coin", now);

		Assert.assertTrue(String.format("old: %d,  new: %d", oldCoin, newCoin),
				newCoin > oldCoin);

	}
}
