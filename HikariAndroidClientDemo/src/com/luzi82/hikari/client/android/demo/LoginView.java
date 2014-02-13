package com.luzi82.hikari.client.android.demo;

import java.util.concurrent.Future;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.hikari.client.Hikari;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.protocol.HikariProtocolDef;

public class LoginView extends ListView {

	ObjectMapper objectMapper;

	public LoginView(Context context) {
		super(context);

		setAdapter(new Adapter());

		objectMapper = new ObjectMapper();

		this.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				cmdV[arg2].run();
			}
		});
	}

	public final Cmd[] cmdV = { //
	new Cmd("createUser") {
		@Override
		public void run1() {
			setFuture(Hikari
					.createUser(
							getClient(),
							"asdf",
							new MyFutureCallback<HikariProtocolDef.CreateUserCmd.Result>(
									this)));
		}
	} };

	public class MyFutureCallback<T> implements FutureCallback<T> {

		public Cmd cmd;

		public MyFutureCallback(Cmd cmd) {
			this.cmd = cmd;
		}

		@Override
		public void cancelled() {
			cmd.dialog.dismiss();
		}

		@Override
		public void completed(T arg0) {
			try {
				cmd.dialog.dismiss();
				final String v = objectMapper.writeValueAsString(arg0);
				System.err.println(v);
				getMain().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getContext());
						builder.setTitle("completed");
						builder.setMessage(v);
						builder.setPositiveButton("ok", null);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				});
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void failed(final Exception arg0) {
			arg0.printStackTrace();
			cmd.dialog.dismiss();
			getMain().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getContext());
					builder.setTitle("failed");
					builder.setMessage(arg0.getMessage());
					builder.setPositiveButton("ok", null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});
		}

	}

	public abstract class Cmd implements OnCancelListener {
		final String title;
		ProgressDialog dialog;
		public Future<?> future;

		public Cmd(String title) {
			this.title = title;
		}

		@Override
		public String toString() {
			return title;
		}

		public void run() {
			run1();
			dialog = ProgressDialog.show(getContext(), title, "Wait...", false,
					future != null, this);
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			if (future != null) {
				future.cancel(true);
			}
		}

		public void setFuture(Future<?> future) {
			this.future = future;
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

}
