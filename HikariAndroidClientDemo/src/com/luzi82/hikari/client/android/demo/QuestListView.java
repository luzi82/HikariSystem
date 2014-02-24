package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import android.content.Context;

import com.luzi82.concurrent.DummyFutureCallback;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.HsQuestCostData;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.HsQuestEntryData;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestStartCmd;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestStartCmd.Result;
import com.luzi82.homuvalue.Value;
import com.luzi82.homuvalue.Value.Listener;

public class QuestListView extends HikariListView {

	Listener<Long> dataSyncTimeListener;

	public QuestListView(Context context) {
		super(context);

		dataSyncTimeListener = new Listener<Long>() {
			@Override
			public void onValueDirty(Value<Long> v) {
				System.err.println("oWZpPqC7");
				updateList();
			}

		};

		getMain().dataSyncTimeVar.addListener(dataSyncTimeListener);
		updateList();

	}

	public void updateList() {
		List<HsQuestEntryData> questEntryList = Quest
				.getHsQuestEntryDataList(getClient());
		List<HsQuestCostData> questCostList = Quest
				.getHsQuestCostDataList(getClient());
		List<Item> itemList = new LinkedList<Item>();
		if (questEntryList != null) {
			for (final HsQuestEntryData questEntry : questEntryList) {
				long cost = Quest.get(questCostList, questEntry.key, "ap").count;
				itemList.add(new FutureDialogItem<QuestStartCmd.Result>(String
						.format("%s: %d", questEntry.key, cost),
						new DummyFutureCallback<QuestStartCmd.Result>(null) {
							@Override
							public void completed(Result result) {
								getMain().questInstanceVar
										.set(result.quest_instance);
								super.completed(result);
							}
						}) {
					@Override
					public Future<QuestStartCmd.Result> getFuture() {
						return Quest.questStart(getClient(), questEntry.key,
								this);
					}
				});
			}
		}
		setItemList(itemList);
	}

}
