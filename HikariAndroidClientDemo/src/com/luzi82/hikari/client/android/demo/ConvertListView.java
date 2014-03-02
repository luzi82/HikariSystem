package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import android.content.Context;

import com.luzi82.concurrent.DummyFutureCallback;
import com.luzi82.hikari.client.Resource;
import com.luzi82.hikari.client.Resource.ConvertEntry;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ConvertCmd;

public class ConvertListView extends HikariListView implements
		HikariListView.UpdateList {

	UpdateListObserver updateListObserver = new UpdateListObserver(this);

	public ConvertListView(Context context) {
		super(context);

		getMain().dataSyncTimeObservable.addObserver(updateListObserver);
		updateList();
	}

	public void updateList() {
		List<Item> itemList = new LinkedList<Item>();

		Map<String, ConvertEntry> convertEntryMap = Resource
				.getConvertEntryMap(getClient());

		if (convertEntryMap != null) {
			for (final String convertKey : convertEntryMap.keySet()) {
				itemList.add(new FutureDialogItem<ConvertCmd.Result>(
						convertKey, new DummyFutureCallback<ConvertCmd.Result>(
								null)) {
					@Override
					public Future<ConvertCmd.Result> getFuture() {
						return Resource.convert(getClient(), convertKey, 1,
								this);
					}
				});
			}
		}

		setItemList(itemList);
	}

}
