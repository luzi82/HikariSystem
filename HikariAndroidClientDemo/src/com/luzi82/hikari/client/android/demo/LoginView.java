package com.luzi82.hikari.client.android.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.Quest;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.HsQuestEntryData;
import com.luzi82.hikari.client.protocol.UserProtocolDef;

public class LoginView extends ListView {

	ObjectMapper objectMapper;

	public LoginView(Context context) {
		super(context);

		setAdapter(new Adapter());

		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		this.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				cmdV[arg2].run();
			}
		});
	}

	public final Cmd<?>[] cmdV = { //
	new Cmd<Void>("sync") {
		@Override
		public void run1() {
			SyncFuture sf = new SyncFuture(this);
			sf.start();
			setFuture(sf);
		}
	}, new Cmd<UserProtocolDef.CreateUserCmd.Result>("createUser") {
		@Override
		public void run1() {
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
			setFuture(User.createUser(getClient(), modelString, this));
		}
	}, new Cmd<UserProtocolDef.LoginCmd.Result>("login") {
		@Override
		public void run1() {
			setFuture(User.login(getClient(), this));
		}
	} };

	public abstract class Cmd<T> extends DummyFutureCallback<T> {
		final String title;
		final FutureDialog<T> futureDialog;

		public Cmd(String title) {
			super(new FutureDialog<T>(new ResultDialogFutureCallback<T>(
					getMain(), null, getMain().executorService)));
			this.title = title;
			this.futureDialog = (FutureDialog<T>) callback;
		}

		@Override
		public String toString() {
			return title;
		}

		public void run() {
			run1();
			futureDialog.show(getContext(), title, "Wait...", false);
		}

		public void setFuture(Future<T> future) {
			futureDialog.setFuture(future);
		}

		public abstract void run1();

	}

	private class Adapter extends ArrayAdapter<Cmd> {

		public Adapter() {
			super(LoginView.this.getContext(),
					android.R.layout.simple_list_item_1, cmdV);
		}

	}

	private MainActivity getMain() {
		return (MainActivity) getContext();
	}

	private HsClient getClient() {
		return getMain().hsClient;
	}

	public class SyncFuture extends GuriFuture<Void> {

		public SyncFuture(FutureCallback<Void> callback) {
			super(callback, getMain().executorService);
		}

		public void start() {
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
				setFuture(Quest.getHsQuestEntryDataList(getClient(),
						new Callback<List<HsQuestEntryData>>(new Step2())));
			}
		}

		public class Step2 extends Step {
			@Override
			public void _run() throws Exception {
				System.err.println("BkdAm5yx");
				// questEntryAry = (List<HsQuestEntryData>) lastFutureResult;
				getMain().questEntryListVar
						.set((List<HsQuestEntryData>) lastFutureResult);
				System.err.println("1LQrH8Wj");
				completed(null);
			}
		}

	}

}
