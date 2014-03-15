package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.luzi82.hikari.client.protocol.HikariValueProtocol;

public class Value extends HikariValueProtocol {

	public static long value(HsClient client, String value_key, long now) {
		Map<String, UserValue> dataList = getValueStatusObservable(client)
				.get();
		UserValue rv = dataList.get(value_key);
		if (rv == null)
			throw new IllegalArgumentException(String.format(
					"value_key \"%s\" not valid", value_key));
		if (rv.count != null)
			return rv.count;
		if (rv.time != null) {
			long ret = client.getServerTime(now) + rv.max - rv.time;
			ret = Math.min(ret, rv.max);
			ret = Math.max(ret, 0);
			return ret;
		}
		return 0;
	}

	public static Map<String, ConvertEntry> getConvertEntryMap(HsClient client) {
		List<ValueConvertData> valueConvertDataList = getValueConvertDataList(client);
		if (valueConvertDataList == null)
			return null;

		List<ValueConvertChangeData> valueConvertChangeDataList = getValueConvertChangeDataList(client);

		Map<String, ConvertEntry> ret = new TreeMap<String, Value.ConvertEntry>();

		for (ValueConvertData valueConvertData : valueConvertDataList) {
			ConvertEntry convertEntry = new ConvertEntry();
			convertEntry.key = valueConvertData.key;
			ret.put(convertEntry.key, convertEntry);
		}

		for (ValueConvertChangeData valueConvertChangeData : valueConvertChangeDataList) {
			String convert_key = valueConvertChangeData.parent_key;
			ConvertEntry convertEntry = ret.get(convert_key);
			convertEntry.changeMap.put(valueConvertChangeData.value_key,
					valueConvertChangeData);
		}

		return ret;
	}

	public static class ConvertEntry {
		public String key;
		public Map<String, AbstractValueChangeD> changeMap = new HashMap<String, AbstractValueChangeD>();
	}

}
