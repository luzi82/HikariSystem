package com.luzi82.hikari.client.test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.HsMemStorage;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.apache.HsClientApache;

public class QuestTest {

	public static String SERVER = "http://192.168.1.50";

	public static String TEST_DEV = "test_dev";

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
		
		Assert.assertNotEquals(questInstanceId0, questInstanceId1);

		try {
			Quest.questEnd(client, questInstanceId0, true, null).get();
			Assert.fail();
		} catch (ExecutionException e) {
		}

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

	public static HsClient createClient() {
		ExecutorService executor = Executors.newCachedThreadPool();
		return new HsClient(SERVER, new HsMemStorage(executor), executor,
				new HsClientApache(executor));
	}

}
