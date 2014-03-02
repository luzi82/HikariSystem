package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import com.luzi82.hikari.client.Resource;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ResourceData;
import com.luzi82.lang.WeakObserver;

public class ResourceView extends HikariListView implements
		HikariListView.UpdateList {

	// Resource.Mgr resourceMgr;

	UpdateListObserver updateListObserver;

	public ResourceView(Context context) {
		super(context);

		updateListObserver = new UpdateListObserver(this);

		getMain().dataSyncTimeObservable.addObserver(updateListObserver);
		User.loginDoneObservable(getClient()).addObserver(updateListObserver);
		updateList();

		getMain().foregroundObservable
				.addObserver(new UpdateTimerObserver(this));
		updateTimer();
	}

	public void updateList() {
		LinkedList<Item> itemList = new LinkedList<HikariListView.Item>();

		List<ResourceData> resourceList = Resource
				.getResourceDataList(getClient());
		if ((User.loginDoneObservable(getClient()).get())
				&& (resourceList != null)) {
			// resourceMgr = new Resource.Mgr(getClient());
			for (ResourceData resource : resourceList) {
				itemList.add(new Item(resource.key) {
					@Override
					public String toString() {
						String key = super.toString();
						return "res."
								+ key
								+ " = "
								+ Resource.value(getClient(), key,
										System.currentTimeMillis());
					}
				});
			}
		}

		setItemList(itemList);
	}

	Timer updateTimer;

	synchronized void timerOn() {
		System.err.println("Xj8Xpw2I");
		if (updateTimer != null)
			return;
		updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.err.println("Hd4j1aP0");
				getMain().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final BaseAdapter aa = (BaseAdapter) getAdapter();
						if (aa == null)
							return;
						aa.notifyDataSetChanged();
					}
				});
				updateTimer();
			}
		}, 500, 500);
	}

	synchronized void timerOff() {
		System.err.println("7rdRdgSh");
		if (updateTimer == null)
			return;
		updateTimer.cancel();
		updateTimer = null;
	}

	void enableTimer(boolean v) {
		if (v)
			timerOn();
		else
			timerOff();
	}

	void updateTimer() {
		enableTimer(shouldTimerEnable());
	}

	boolean shouldTimerEnable() {
		return isShown() && getMain().foregroundObservable.get();
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		System.err.println("Kf5TiZtn");
		updateTimer();
		super.onVisibilityChanged(changedView, visibility);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		System.err.println("sz8nPPfg");
		updateTimer();
		super.onWindowVisibilityChanged(visibility);
	}

	static class UpdateTimerObserver extends WeakObserver<ResourceView> {

		public UpdateTimerObserver(ResourceView host) {
			super(host);
		}

		@Override
		protected void update(ResourceView h, Observable o, Object arg) {
			h.updateTimer();
		}

	}

}
