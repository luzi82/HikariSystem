package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import android.content.Context;

import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestEndCmd;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestEndCmd.Result;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestInstance;
import com.luzi82.homuvalue.Value;
import com.luzi82.homuvalue.Value.Listener;

public class QuestView extends HikariListView {

	Listener<QuestInstance> questInstanceListener;

	public QuestView(Context context) {
		super(context);

		questInstanceListener = new Listener<QuestInstance>() {
			@Override
			public void onValueDirty(Value<QuestInstance> v) {
				refresh();
			}
		};

		getMain().questInstanceVar.addListener(questInstanceListener);
		questInstanceListener.onValueDirty(getMain().questInstanceVar);

	}

	public void refresh() {
		final List<Item> itemList = new LinkedList<Item>();
		QuestInstance questInstance = getMain().questInstanceVar.get();
		if (questInstance != null) {
			itemList.add(resultItem("Success", true));
			itemList.add(resultItem("Fail", false));
		}
		setItemList(itemList);
	}

	public Item resultItem(String name, final boolean success) {
		return new FutureDialogItem<QuestEndCmd.Result>(name,
				new DummyFutureCallback<QuestEndCmd.Result>(null) {
					@Override
					public void completed(Result result) {
						getMain().questInstanceVar.set(null);
						super.completed(result);
					}
				}) {
			@Override
			public Future<Result> getFuture() {
				return Quest.questEnd(getClient(),
						getMain().questInstanceVar.get().id, success, this);
			}
		};
	}

}
