package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import android.content.Context;

import com.luzi82.concurrent.DummyFutureCallback;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestEndCmd;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestEndCmd.Result;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestInstance;

public class QuestView extends HikariListView implements
		HikariListView.UpdateList {

	UpdateListObserver updateListObserver = new UpdateListObserver(this);

	public QuestView(Context context) {
		super(context);

		getMain().questInstanceObservableVar.addObserver(updateListObserver);
		updateList();
	}

	public void updateList() {
		final List<Item> itemList = new LinkedList<Item>();
		QuestInstance questInstance = getMain().questInstanceObservableVar.get();
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
						getMain().questInstanceObservableVar.set(null);
						super.completed(result);
					}
				}) {
			@Override
			public Future<Result> getFuture() {
				return Quest.questEnd(getClient(),
						getMain().questInstanceObservableVar.get().id, success, this);
			}
		};
	}

}
