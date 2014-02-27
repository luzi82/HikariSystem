package com.luzi82.hikari.client.android.demo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ArrayAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.Resource;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ResourceData;
import com.luzi82.hikari.client.protocol.HikariUserProtocolDef.CreateUserCmd;
import com.luzi82.hikari.client.protocol.HikariUserProtocolDef.LoginCmd;
import com.luzi82.hikari.client.protocol.HikariUserProtocolDef.LoginCmd.Result;
import com.luzi82.homuvalue.Value;
import com.luzi82.homuvalue.Value.Listener;

public class LoginView extends HikariListView {

	Listener<Long> dataSyncTimeListener;
	Listener<Boolean> loginListener;

	ObjectMapper objectMapper;

	Resource.Mgr resourceMgr;

	public LoginView(Context context) {
		super(context);

		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		dataSyncTimeListener = new Listener<Long>() {
			@Override
			public void onValueDirty(Value<Long> v) {
				System.err.println("PyJcV4bM");
				updateList();
			}
		};
		loginListener = new Listener<Boolean>() {

			@Override
			public void onValueDirty(Value<Boolean> v) {
				updateList();
			}

		};

		// addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
		// @Override
		// public void onViewDetachedFromWindow(View v) {
		// }
		//
		// @Override
		// public void onViewAttachedToWindow(View v) {
		// }
		// });

		// setOnFocusChangeListener(new OnFocusChangeListener() {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// enableTimer(isShown());
		// }
		// });

		getMain().dataSyncTimeVar.addListener(dataSyncTimeListener);
		User.isLoginDoneValue(getClient()).addListener(loginListener);
		updateList();

		updateTimer();
	}

	public void updateList() {
		LinkedList<Item> itemList = new LinkedList<HikariListView.Item>();

		itemList.add(new FutureDialogItem<Void>("sync", null) {
			@Override
			public Future<Void> getFuture() {
				SyncFuture sf = new SyncFuture(this);
				sf.start();
				return sf;
			}
		});
		itemList.add(new FutureDialogItem<CreateUserCmd.Result>("createUser",
				null) {
			@Override
			public Future<CreateUserCmd.Result> getFuture() {
				Map<String, Object> modelData = new HashMap<String, Object>();
				modelData.put("H_MANUFACTURER", Build.MANUFACTURER);
				modelData.put("H_MODEL", Build.MODEL);
				modelData.put("H_PRODUCT", Build.PRODUCT);
				modelData.put("S_CODENAME", Build.VERSION.CODENAME);
				modelData.put("S_INCREMENTAL", Build.VERSION.INCREMENTAL);
				modelData.put("S_RELEASE", Build.VERSION.RELEASE);
				modelData.put("S_SDK_INT", Build.VERSION.SDK_INT);
				String modelString = "unknown";
				try {
					modelString = objectMapper.writeValueAsString(modelData);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return User.createUser(getClient(), modelString, this);
			}
		});
		itemList.add(new FutureDialogItem<LoginCmd.Result>("login", null) {
			@Override
			public Future<Result> getFuture() {
				return User.login(getClient(), this);
			}
		});

		List<ResourceData> resourceList = Resource
				.getResourceDataList(getClient());
		if ((User.isLoginDoneValue(getClient()).get())
				&& (resourceList != null)) {
			resourceMgr = new Resource.Mgr(getClient());
			for (ResourceData resource : resourceList) {
				itemList.add(new Item(resource.key) {
					@Override
					public String toString() {
						String key = super.toString();
						return "res."
								+ key
								+ " = "
								+ resourceMgr.value(key,
										System.currentTimeMillis());
					}
				});
			}
		}

		setItemList(itemList);
	}

	public class SyncFuture extends GuriFuture<Void> {

		public SyncFuture(FutureCallback<Void> callback) {
			super(false, callback, getMain().executorService);
		}

		public void fire() {
			new Step0().start();
		}

		public class Step0 extends Step {
			@Override
			public void _run() throws Exception {
				setFuture(getClient().syncData(new Callback<Void>(new Step1())));
			}
		}

		public class Step1 extends Step {
			@Override
			public void _run() throws Exception {
				System.err.println("8lpgs4Rw");
				// getMain().questEntryListVar.set(Quest.getHsQuestEntryDataList(getClient()));
				getMain().dataSyncTimeVar.set(System.currentTimeMillis());
				completed(null);
			}
		}

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
						final ArrayAdapter aa = (ArrayAdapter) getAdapter();
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
		enableTimer(isShown());
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

}
