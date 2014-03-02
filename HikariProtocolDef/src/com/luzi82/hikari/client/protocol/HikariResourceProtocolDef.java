package com.luzi82.hikari.client.protocol;

import java.util.TreeMap;

public class HikariResourceProtocolDef {

	public static class ResourceData {
		public final static int TYPE_COUNT = 1;
		public final static int TYPE_TIME = 2;
		public String key;
		public int type;
	}

	public static class ResourceConvertChangeData {
		public String resource_convert_key;
		public String resource_key;
		public long change;
	}

	public static class ResourceStatus extends TreeMap<String, ResourceValue> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2417872520439884077L;

	}

	public static class ResourceValue {
		public String resource_key;
		public Integer count;
		public Long time;
		public Long max;
	}

	public static class ConvertCmd {

		public static class Request {
			public String resource_convert_key;
			public long count;
		}

		public static class Result {
		}

	}

	public static class SetUserResourceCountCmd {

		public static class Request {
			public String username;
			public String resource_key;
			public long count;
		}

		public static class Result {
		}

	}

}
