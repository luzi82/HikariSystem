package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.HsQuestEntryData;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestStartCmd;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestStartCmd.Result;
import com.luzi82.homuvalue.Value;
import com.luzi82.homuvalue.Value.Listener;

public class QuestListView extends HikariListView {

	Listener<List<HsQuestEntryData>> questEntryListListener;

	public QuestListView(Context context) {
		super(context);

		questEntryListListener = new Listener<List<HsQuestEntryData>>() {
			@Override
			public void onValueDirty(Value<List<HsQuestEntryData>> v) {
				List<HsQuestEntryData> questEntryList = v.get();
				List<Item> itemList = new LinkedList<Item>();
				if (questEntryList != null) {
					for (final HsQuestEntryData questEntry : questEntryList) {
						itemList.add(new Item(questEntry.key) {
							@Override
							public void onClick() {
								startQuest(questEntry);
							}
						});
					}
				}
				setItemList(itemList);
			}

		};

		getMain().questEntryListVar.addListener(questEntryListListener);
		questEntryListListener.onValueDirty(getMain().questEntryListVar);

	}

	public void startQuest(final HsQuestEntryData questEntry) {
		FutureDialog<QuestStartCmd.Result> fd = new FutureDialog<QuestStartCmd.Result>(
				new ResultDialogFutureCallback<QuestStartCmd.Result>(getMain(),
						new DummyFutureCallback<QuestStartCmd.Result>(null) {
							@Override
							public void completed(Result result) {
								getMain().questInstanceVar
										.set(result.quest_instance);
								super.completed(result);
							}
						}, getMain().executorService));

		fd.setFuture(Quest.questStart(getClient(), questEntry.key, fd));
	}

}
