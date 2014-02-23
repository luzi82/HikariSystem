package com.luzi82.hikari.client.protocol;

import java.util.List;

public class ResourceProtocolDef {

	public static class HsResourceData {
		public final static int TYPE_COUNT = 1;
		public final static int TYPE_TIME = 2;
		public String key;
		public int type;
	}

	public static class Status {
		public List<ResourceValue> resource_value_list;
	}

	public static class ResourceValue {
		public String resource_key;
		public Integer count;
		public Long time;
		public Long max;
	}

}
