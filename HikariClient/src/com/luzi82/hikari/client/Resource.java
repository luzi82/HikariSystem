package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.luzi82.hikari.client.protocol.HikariResourceProtocol;

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
		List<ResourceConvertData> resourceConvertDataList = getResourceConvertDataList(client);
		if (resourceConvertDataList == null)
			return null;

		List<ResourceConvertChangeData> resourceConvertChangeDataList = getResourceConvertChangeDataList(client);

		Map<String, ConvertEntry> ret = new TreeMap<String, Resource.ConvertEntry>();

		for (ResourceConvertData resourceConvertData : resourceConvertDataList) {
			ConvertEntry convertEntry = new ConvertEntry();
			convertEntry.key = resourceConvertData.key;
			ret.put(convertEntry.key, convertEntry);
		}

		for (ResourceConvertChangeData resourceConvertChangeData : resourceConvertChangeDataList) {
			String convert_key = resourceConvertChangeData.parent_key;
			ConvertEntry convertEntry = ret.get(convert_key);
			convertEntry.changeMap.put(resourceConvertChangeData.resource_key, resourceConvertChangeData);
		}

		return ret;
	}

	public static class ConvertEntry {
		public String key;
		public Map<String, AbstractResourceChangeD> changeMap = new HashMap<String, AbstractResourceChangeD>();
	}

}
