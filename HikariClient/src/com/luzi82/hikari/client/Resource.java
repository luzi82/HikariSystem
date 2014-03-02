package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.luzi82.hikari.client.protocol.HikariResourceProtocol;
import com.luzi82.hikari.client.protocol.HikariGachaProtocolDef.GachaCostData;
import com.luzi82.hikari.client.protocol.HikariGachaProtocolDef.GachaData;

public class Resource extends HikariResourceProtocol {

	public static long value(HsClient client, String resource_key, long now) {
		Map<String, ResourceValue> dataList = getResourceStatusObservable(
				client).get();
		ResourceValue rv = dataList.get(resource_key);
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
		List<ResourceConvertChangeData> resourceConvertChangeDataList = getResourceConvertChangeDataList(client);
		if (resourceConvertChangeDataList == null)
			return null;

		Map<String, ConvertEntry> ret = new TreeMap<String, Resource.ConvertEntry>();
		for (ResourceConvertChangeData resourceConvertChangeData : resourceConvertChangeDataList) {
			String convert_key = resourceConvertChangeData.resource_convert_key;
			ConvertEntry convertEntry = ret.get(convert_key);
			if (convertEntry == null) {
				convertEntry = new ConvertEntry();
				convertEntry.key = convert_key;
				ret.put(convert_key, convertEntry);
			}
			Change change = new Change();
			change.resource_key = resourceConvertChangeData.resource_key;
			change.change = resourceConvertChangeData.change;
			convertEntry.changeMap.put(change.resource_key, change);
		}

		return ret;
	}

	public static class ConvertEntry {
		public String key;
		public Map<String, Change> changeMap = new HashMap<String, Change>();
	}

	public static class Change {
		public String resource_key;
		public long change;
	}

}
