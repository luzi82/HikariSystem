package com.luzi82.hikari.client.android.demo;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.HsQuestEntryData;
import com.luzi82.homuvalue.Value;
import com.luzi82.homuvalue.Value.Listener;

public class QuestView extends ListView {

	ObjectMapper objectMapper;

	Listener<List<HsQuestEntryData>> questEntryListListener;

	public QuestView(Context context) {
		super(context);

		// if (getMain().questEntryAry != null) {
		// setAdapter(new Adapter());
		// }

		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		this.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// cmdV[arg2].run();
			}
		});

		questEntryListListener = new Listener<List<HsQuestEntryData>>() {
			@Override
			public void onValueDirty(Value<List<HsQuestEntryData>> v) {
				List<HsQuestEntryData> questEntryList = v.get();
				if (questEntryList != null) {
					getMain().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setAdapter(new Adapter());
						}
					});
				}
			}
		};

		getMain().questEntryListVar.addListener(questEntryListListener);
		questEntryListListener.onValueDirty(getMain().questEntryListVar);

	}

	// public class MyFutureCallback<T> implements FutureCallback<T> {
	//
	// public Cmd cmd;
	//
	// public MyFutureCallback(Cmd cmd) {
	// this.cmd = cmd;
	// }
	//
	// @Override
	// public void cancelled() {
	// cmd.dialog.dismiss();
	// }
	//
	// @Override
	// public void completed(T arg0) {
	// try {
	// cmd.dialog.dismiss();
	// final String v = objectMapper.writeValueAsString(arg0);
	// System.err.println(v);
	// getMain().runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(
	// getContext());
	// builder.setTitle("Completed");
	// builder.setMessage(v);
	// builder.setPositiveButton("Ok", null);
	// AlertDialog dialog = builder.create();
	// dialog.show();
	// }
	// });
	// } catch (JsonProcessingException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void failed(final Exception arg0) {
	// arg0.printStackTrace();
	// cmd.dialog.dismiss();
	// getMain().runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(
	// getContext());
	// builder.setTitle("Failed");
	// builder.setMessage(arg0.getMessage());
	// builder.setPositiveButton("Ok", null);
	// AlertDialog dialog = builder.create();
	// dialog.show();
	// }
	// });
	// }
	//
	// }

	private class Adapter extends ArrayAdapter<HsQuestEntryData> {

		public Adapter() {
			super(QuestView.this.getContext(),
					android.R.layout.simple_list_item_1,
					getMain().questEntryListVar.get());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HsQuestEntryData questEntry = getItem(position);
			TextView ret = (TextView) super.getView(position, convertView,
					parent);
			ret.setText(questEntry.key);
			return ret;
		}

	}

	private MainActivity getMain() {
		return (MainActivity) getContext();
	}

	private HsClient getClient() {
		return getMain().hsClient;
	}

}
