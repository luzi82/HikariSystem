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
			return 0;
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
		List<ValueConvertData> resourceConvertDataList = getValueConvertDataList(client);
		if (resourceConvertDataList == null)
			return null;

		List<ValueConvertChangeData> resourceConvertChangeDataList = getValueConvertChangeDataList(client);

		Map<String, ConvertEntry> ret = new TreeMap<String, Value.ConvertEntry>();

		for (ValueConvertData resourceConvertData : resourceConvertDataList) {
			ConvertEntry convertEntry = new ConvertEntry();
			convertEntry.key = resourceConvertData.key;
			ret.put(convertEntry.key, convertEntry);
		}

		for (ValueConvertChangeData resourceConvertChangeData : resourceConvertChangeDataList) {
			String convert_key = resourceConvertChangeData.parent_key;
			ConvertEntry convertEntry = ret.get(convert_key);
			convertEntry.changeMap.put(resourceConvertChangeData.value_key,
					resourceConvertChangeData);
		}

		return ret;
	}

	public static class ConvertEntry {
		public String key;
		public Map<String, AbstractValueChangeD> changeMap = new HashMap<String, AbstractValueChangeD>();
	}

}
