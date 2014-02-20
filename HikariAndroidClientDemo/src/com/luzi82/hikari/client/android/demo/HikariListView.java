package com.luzi82.hikari.client.android.demo;

import java.util.List;

import com.luzi82.hikari.client.HsClient;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HikariListView extends ListView {

	public HikariListView(Context context) {
		super(context);

		this.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Item item = (Item) arg0.getItemAtPosition(arg2);
				item.onClick();
			}
		});
	}

	public interface Item {
		public void onClick();
	}

	public class InfoItem implements Item {

		@Override
		public void onClick() {
			// nothing
		}
	}

	public abstract class CmdItem implements Item {
	}

	public void setItemList(final List<Item> itemList) {
		getMain().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setAdapter(new ArrayAdapter<Item>(HikariListView.this
						.getContext(), android.R.layout.simple_list_item_1,
						itemList));
			}
		});
	}

	public MainActivity getMain() {
		return (MainActivity) getContext();
	}

	public HsClient getClient() {
		return getMain().hsClient;
	}

	
}
