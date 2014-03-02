package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import android.content.Context;

import com.luzi82.concurrent.DummyFutureCallback;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestCostData;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestEntryData;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestStartCmd;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestStartCmd.Result;

public class QuestListView extends HikariListView implements
		HikariListView.UpdateList {

	// Listener<Long> dataSyncTimeListener;

	UpdateListObserver updateListObserver;

	public QuestListView(Context context) {
		super(context);

		updateListObserver = new UpdateListObserver(this);

		// dataSyncTimeListener = new Listener<Long>() {
		// @Override
		// public void onValueDirty(Value<Long> v) {
		// System.err.println("oWZpPqC7");
		// updateList();
		// }
		//
		// };

		// getMain().dataSyncTimeVar.addListener(dataSyncTimeListener);
		getMain().dataSyncTimeObservable.addObserver(updateListObserver);
		updateList();

	}

	public void updateList() {
		List<QuestEntryData> questEntryList = Quest
				.getQuestEntryDataList(getClient());
		List<QuestCostData> questCostList = Quest
				.getQuestCostDataList(getClient());
		List<Item> itemList = new LinkedList<Item>();
		if (questEntryList != null) {
			for (final QuestEntryData questEntry : questEntryList) {
				long cost = Quest.get(questCostList, questEntry.key, "ap").count;
				itemList.add(new FutureDialogItem<QuestStartCmd.Result>(String
						.format("%s: %d", questEntry.key, cost),
						new DummyFutureCallback<QuestStartCmd.Result>(null) {
							@Override
							public void completed(Result result) {
								getMain().questInstanceObservableVar
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
