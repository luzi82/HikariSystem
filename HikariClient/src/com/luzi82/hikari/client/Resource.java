package com.luzi82.hikari.client;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.luzi82.hikari.client.protocol.ResourceProtocol;

public class Resource extends ResourceProtocol {

	// public static int getCount(
	// List<ResourceProtocolDef.HsResourceData> dataList,
	// Status resourceStatus0, String resource_key) {
	// for (ResourceValue rkc : resourceStatus0.resource_value_list) {
	// if (ObjectUtils.equals(rkc.resource_key, resource_key)) {
	// return rkc.count;
	// }
	// }
	// return 0;
	// }

	public static class Mgr {
		final HsClient client;

		public Mgr(HsClient client) {
			this.client = client;
		}

		public long value(String resource_key, long now) {
			List<ResourceValue> dataList = getStatusValue(client).get().resource_value_list;
			ResourceValue rv = null;
			for (ResourceValue value : dataList) {
				if (!ObjectUtils.equals(resource_key, value.resource_key))
					continue;
				rv = value;
				break;
			}
			if (rv == null)
				return 0;
			if (rv.count != null)
				return rv.count;
			if (rv.time != null) {
				long ret = now + client.getServerTimeOffset() - rv.time;
				ret = Math.min(ret, rv.max);
				ret = Math.max(ret, 0);
				return ret;
			}
			return 0;
		}
	}

}
