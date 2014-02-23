package com.luzi82.hikari.client.android.demo;

import java.util.concurrent.Executor;

import android.app.Activity;
import android.app.AlertDialog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.concurrent.DummyFutureCallback;
import com.luzi82.concurrent.FutureCallback;

public class ResultDialogFutureCallback<T> extends DummyFutureCallback<T> {

	Executor executor;
	ObjectMapper objectMapper;
	Activity activity;

	public ResultDialogFutureCallback(Activity activity,
			FutureCallback<T> callback, Executor executor) {
		super(callback);
		this.activity = activity;
		this.executor = executor;
		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}

	@Override
	public void completed(final T result) {
		if (result != null) {
			try {
				final String v = objectMapper.writeValueAsString(result);
				System.err.println(v);
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								activity);
						builder.setTitle("Completed");
						builder.setMessage(v);
						builder.setPositiveButton("Ok", null);
						AlertDialog dialog = builder.create();
						dialog.show();
						if (callback != null) {
							executor.execute(new Runnable() {
								@Override
								public void run() {
									superCompleted(result);
								}
							});
						}
					}
				});
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				super.completed(result);
			}
		} else {
			super.completed(result);
		}
	}

	private void superCompleted(final T result) {
		super.completed(result);
	}

	@Override
	public void failed(final Exception ex) {
		ex.printStackTrace();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("Failed");
				builder.setMessage(ex.getMessage());
				builder.setPositiveButton("Ok", null);
				AlertDialog dialog = builder.create();
				dialog.show();
				if (callback != null) {
					executor.execute(new Runnable() {
						@Override
						public void run() {
							superFailed(ex);
						}
					});
				}
			}
		});
	}

	private void superFailed(final Exception ex) {
		super.failed(ex);
	}

}
