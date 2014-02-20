package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestEndCmd;
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
			itemList.add(new Item("Success") {
				@Override
				public void onClick() {
					report(true);
				}
			});
			itemList.add(new Item("Fail") {
				@Override
				public void onClick() {
					report(false);
				}
			});
		}
		setItemList(itemList);
	}

	public void report(boolean success) {
		FutureDialog<QuestEndCmd.Result> fd = new FutureDialog<QuestEndCmd.Result>(
				new ResultDialogFutureCallback<QuestEndCmd.Result>(getMain(),
						new DummyFutureCallback<QuestEndCmd.Result>(null) {
							@Override
							public void completed(QuestEndCmd.Result result) {
								getMain().questInstanceVar.set(null);
								super.completed(result);
							}
						}, getMain().executorService));

		fd.setFuture(Quest.questEnd(getClient(),
				getMain().questInstanceVar.get().id, success, fd));
	}

}
