package com.luzi82.hikari.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.luzi82.hikari.client.protocol.HikariResourceProtocol;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.ResourceConvertChangeData;

public class Resource extends HikariResourceProtocol {

	public static long value(HsClient client, String resource_key, long now) {
		Map<String, ResourceValue> dataList = getResourceStatusValue(client).get();
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

	public static List<String> getConvertKeyList(HsClient client) {
		List<String> ret = new LinkedList<String>();
		List<ResourceConvertChangeData> resourceConvertChangeDataList = getResourceConvertChangeDataList(client);

		for (ResourceConvertChangeData resourceConvertChangeData : resourceConvertChangeDataList) {
			String key = resourceConvertChangeData.key;
			if (ret.contains(key))
				continue;
			ret.add(key);
		}

		return ret;
	}

}
