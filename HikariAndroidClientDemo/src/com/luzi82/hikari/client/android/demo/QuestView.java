package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestEndCmd;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestInstance;
import com.luzi82.homuvalue.Value;
import com.luzi82.homuvalue.Value.Listener;

public class QuestView extends ListView {

	ObjectMapper objectMapper;

	Listener<QuestInstance> questInstanceListener;

	public QuestView(Context context) {
		super(context);

		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		this.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final Item item = (Item) arg0.getItemAtPosition(arg2);
				if ((item.type == Item.TYPE_SUCCESS)
						|| (item.type == Item.TYPE_FAIL)) {
					final boolean success = item.type == Item.TYPE_SUCCESS;
					FutureDialog<QuestEndCmd.Result> fd = new FutureDialog<QuestEndCmd.Result>(
							new ResultDialogFutureCallback<QuestEndCmd.Result>(
									getMain(),
									new DummyFutureCallback<QuestEndCmd.Result>(
											null) {
										@Override
										public void completed(QuestEndCmd.Result result) {
											getMain().questInstanceVar
													.set(null);
											super.completed(result);
										}
									}, getMain().executorService));

					fd.setFuture(Quest.questEnd(getClient(),
							getMain().questInstanceVar.get().id, success, fd));
				}
			}
		});

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
			Item item;
			item = new Item();
			item.type = Item.TYPE_SUCCESS;
			itemList.add(item);
			item = new Item();
			item.type = Item.TYPE_FAIL;
			itemList.add(item);
		}
		getMain().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setAdapter(new Adapter(itemList));
			}
		});
	}

	private class Item {
		public int type;
		public static final int TYPE_SUCCESS = -1;
		public static final int TYPE_FAIL = -2;

		@Override
		public String toString() {
			switch (type) {
			case TYPE_SUCCESS: {
				return "success";
			}
			case TYPE_FAIL: {
				return "fail";
			}
			}
			return null;
		}
	}

	private class Adapter extends ArrayAdapter<Item> {

		public Adapter(List<Item> itemList) {
			super(QuestView.this.getContext(),
					android.R.layout.simple_list_item_1, itemList);
		}

	}

	private MainActivity getMain() {
		return (MainActivity) getContext();
	}

	private HsClient getClient() {
		return getMain().hsClient;
	}

}
