package com.luzi82.hikari.client.android.demo;

import java.util.concurrent.Future;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.luzi82.concurrent.FutureCallback;

public class FutureDialog<T> extends DummyFutureCallback<T> implements
		OnCancelListener {

	ProgressDialog dialog;
	boolean callbackDone = false;
	Future<T> future;

	public FutureDialog(FutureCallback<T> callback) {
		super(callback);
	}

	public synchronized void show(Context context, CharSequence title,
			CharSequence message, boolean indeterminate) {
		if (!callbackDone) {
			dialog = ProgressDialog.show(context, title, message,
					indeterminate, future != null, this);
		}
	}

	public synchronized void setFuture(Future<T> future) {
		this.future = future;
	}

	@Override
	public synchronized void completed(T result) {
		callbackDone = true;
		future = null;
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = null;
		super.completed(result);
	}

	@Override
	public synchronized void failed(Exception ex) {
		callbackDone = true;
		future = null;
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = null;
		super.failed(ex);
	}

	@Override
	public synchronized void cancelled() {
		callbackDone = true;
		future = null;
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = null;
		super.cancelled();
	}

	@Override
	public void onCancel(DialogInterface arg0) {
		if (future != null) {
			future.cancel(true);
		}
	}

}
