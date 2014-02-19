package com.luzi82.hikari.client.android.demo;

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
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestInstance;
import com.luzi82.homuvalue.Value;
import com.luzi82.homuvalue.Value.Listener;

public class QuestView extends ListView {

	ObjectMapper objectMapper;

	Listener<QuestInstance> questInstanceListener;

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

		questInstanceListener = new Listener<QuestInstance>() {
			@Override
			public void onValueDirty(Value<QuestInstance> v) {
				System.err.println("9skSaliZ onValueDirty");
			}
		};

		getMain().questInstanceVar.addListener(questInstanceListener);
		questInstanceListener.onValueDirty(getMain().questInstanceVar);

	}

	// private class Adapter extends ArrayAdapter<QuestInstance> {
	//
	// public Adapter() {
	// super(QuestView.this.getContext(),
	// android.R.layout.simple_list_item_1,
	// getMain().questInstanceVar.get());
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// QuestInstance questEntry = getItem(position);
	// TextView ret = (TextView) super.getView(position, convertView,
	// parent);
	// ret.setText(questEntry.key);
	// return ret;
	// }
	//
	// }

	private MainActivity getMain() {
		return (MainActivity) getContext();
	}

	private HsClient getClient() {
		return getMain().hsClient;
	}

}
