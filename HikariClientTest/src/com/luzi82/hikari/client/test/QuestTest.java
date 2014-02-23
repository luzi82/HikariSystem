package com.luzi82.hikari.client.test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.Base;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.Resource;
import com.luzi82.hikari.client.User;

public class QuestTest extends AbstractTest {

	@Test
	public void testQuestPlay() throws Exception {
		HsClient client = createClient();

		client.syncData(null).get();

		User.createUser(client, TEST_DEV, null).get(5, TimeUnit.SECONDS);
		User.login(client, null).get(5, TimeUnit.SECONDS);

		List<Quest.HsQuestEntryData> questEntryList = Quest
				.getHsQuestEntryDataList(client, null).get();
		Quest.HsQuestEntryData questEntry = questEntryList.get(0);

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

		List<Quest.HsQuestEntryData> questEntryList = Quest
				.getHsQuestEntryDataList(client, null).get();
		Quest.HsQuestEntryData questEntry = questEntryList.get(0);

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

		List<Quest.HsQuestEntryData> questEntryList = Quest
				.getHsQuestEntryDataList(client, null).get();
		Quest.HsQuestEntryData questEntry = questEntryList.get(0);

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
		Base.syncTime(client, null).get();
		createLogin(client);

		List<Quest.HsQuestEntryData> questEntryList = Quest
				.getHsQuestEntryDataList(client, null).get();
		Quest.HsQuestEntryData questEntry = questEntryList.get(0);

		List<Quest.HsQuestCostData> allQuestCostList = Quest
				.getHsQuestCostDataList(client, null).get();
		List<Quest.HsQuestCostData> questCostList = Quest.filter(
				allQuestCostList, questEntry.key);

		Resource.Status resourceStatus0 = Resource.getStatusValue(client).get();

		for (Quest.HsQuestCostData questCost : questCostList) {
			int count0 = Resource.getCount(resourceStatus0,
					questCost.resource_key);
			int cost = questCost.count;
			Assert.assertTrue(count0 >= cost);
		}

		Quest.questStart(client, questEntry.key, null).get();

		Resource.Status resourceStatus1 = Resource.getStatusValue(client).get();

		for (Quest.HsQuestCostData questCost : questCostList) {
			int count0 = Resource.getCount(resourceStatus0,
					questCost.resource_key);
			int count1 = Resource.getCount(resourceStatus1,
					questCost.resource_key);
			int cost = questCost.count;
			Assert.assertTrue(count1 < count0);
			Assert.assertTrue(count1 >= count0 - cost);
		}
	}
}
