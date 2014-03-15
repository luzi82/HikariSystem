package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import android.content.Context;

import com.luzi82.concurrent.DummyFutureCallback;
import com.luzi82.hikari.client.Gacha;
import com.luzi82.hikari.client.Gacha.Entry;
import com.luzi82.hikari.client.Value;
import com.luzi82.hikari.client.protocol.HikariGachaProtocolDef.GachaCmd;

public class GachaListView extends HikariListView implements
		HikariListView.UpdateList {

	UpdateListObserver updateListObserver = new UpdateListObserver(this);

	public GachaListView(Context context) {
		super(context);

		getMain().dataSyncTimeObservable.addObserver(updateListObserver);
		updateList();
	}

	public void updateList() {
		List<Item> itemList = new LinkedList<Item>();

		Map<String, Entry> entryDict = Gacha.getEntryDict(getClient());

		if (entryDict != null) {
			for (final Gacha.Entry convertKey : entryDict.values()) {
				itemList.add(new FutureDialogItem<GachaCmd.Result>(
						convertKey.key,
						new DummyFutureCallback<GachaCmd.Result>(null)) {
					@Override
					public Future<GachaCmd.Result> getFuture() {
						return Gacha.gacha(getClient(), convertKey.key, this);
					}
				});
			}
		}

		setItemList(itemList);
	}

}
