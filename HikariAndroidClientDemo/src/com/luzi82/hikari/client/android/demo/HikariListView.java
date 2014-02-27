package com.luzi82.hikari.client.android.demo;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.Future;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.luzi82.concurrent.FutureCallback;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.lang.WeakObserver;

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

	public class Item {
		public String name;

		public Item(String name) {
			this.name = name;
		}

		public void onClick() {
		}

		@Override
		public String toString() {
			return this.name;
		}

	}

	public abstract class FutureDialogItem<T> extends Item implements
			FutureCallback<T> {
		FutureDialog<T> futureDialog;
		FutureCallback<T> callback;

		public FutureDialogItem(String name, FutureCallback<T> callback) {
			super(name);
			this.callback = callback;
		}

		@Override
		public void completed(T result) {
			futureDialog.completed(result);
		}

		@Override
		public void failed(Exception ex) {
			futureDialog.failed(ex);
		}

		@Override
		public void cancelled() {
			futureDialog.cancelled();
		}

		@Override
		public void onClick() {
			futureDialog = new FutureDialog<T>(
					new ResultDialogFutureCallback<T>(getMain(), callback,
							getMain().executorService));
			futureDialog.setFuture(getFuture());
			futureDialog.show(getContext(), name, "Wait...", false);
		}

		public abstract Future<T> getFuture();
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

	protected static class UpdateListObserver extends WeakObserver<UpdateList> {

		public UpdateListObserver(UpdateList host) {
			super(host);
		}

		@Override
		protected void update(UpdateList h, Observable o, Object arg) {
			h.updateList();
		}

	}

	public interface UpdateList {
		public void updateList();
	}

}
