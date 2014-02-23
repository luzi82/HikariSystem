package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import android.content.Context;

import com.luzi82.concurrent.DummyFutureCallback;
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
						itemList.add(new FutureDialogItem<QuestStartCmd.Result>(
								questEntry.key,
								new DummyFutureCallback<QuestStartCmd.Result>(
										null) {
									@Override
									public void completed(Result result) {
										getMain().questInstanceVar
												.set(result.quest_instance);
										super.completed(result);
									}
								}) {
							@Override
							public Future<QuestStartCmd.Result> getFuture() {
								return Quest.questStart(getClient(),
										questEntry.key, this);
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

}
