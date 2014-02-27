package com.luzi82.hikari.client.protocol;

import java.util.HashMap;

public class HikariResourceProtocolDef {

	public static class ResourceData {
		public final static int TYPE_COUNT = 1;
		public final static int TYPE_TIME = 2;
		public String key;
		public int type;
	}

	public static class ResourceStatus extends HashMap<String, ResourceValue> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2417872520439884077L;

	}

	public static class ResourceValue {
		// public String resource_key;
		public Integer count;
		public Long time;
		public Long max;
	}

}
