package com.luzi82.hikari.client.android.demo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Future;

import android.content.Context;
import android.os.Build;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.HikariUserProtocolDef.CreateUserCmd;
import com.luzi82.hikari.client.protocol.HikariUserProtocolDef.LoginCmd;
import com.luzi82.hikari.client.protocol.HikariUserProtocolDef.LoginCmd.Result;

public class LoginView extends HikariListView implements
		HikariListView.UpdateList {

	ObjectMapper objectMapper;

	public LoginView(Context context) {
		super(context);

		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		User.loginDoneObservable(getClient()).addObserver(
				new NotifyDataSetChangedObserver(this));
		updateList();
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
		itemList.add(new Item(null) {
			@Override
			public String toString() {
				return "Login done: "
						+ (User.loginDoneObservable(getClient()).get() ? "true"
								: "false");
			}
		});

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
				getMain().dataSyncTimeObservable.setNotify(System
						.currentTimeMillis());
				completed(null);
			}
		}

	}

}
