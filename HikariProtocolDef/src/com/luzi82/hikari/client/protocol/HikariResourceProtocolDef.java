package com.luzi82.hikari.client.protocol;

import java.util.LinkedList;
import java.util.TreeMap;

public class HikariResourceProtocolDef {

	public static class ResourceData {
		public final static int TYPE_COUNT = 1;
		public final static int TYPE_TIME = 2;
		public String key;
		public int type;
	}

	public static class ResourceConvertData {
		public String key;
	}

	public static class AbstractResourceChangeD {
		public String parent_key;
		public String resource_key;
		public long change;
	}

	public static class ResourceConvertChangeData extends
			AbstractResourceChangeD {
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

	public static class ConvertHistory {
		public long time;
		public String resource_convert_key;
		public String resource_key;
		public String msg;
		public long count;
	}

	public static class GetConvertHistoryListCmd {

		public static class Request {
			public long offset;
			public int count;
		}

		public static class Result extends LinkedList<ConvertHistory> {
			private static final long serialVersionUID = -1236770084966746653L;
		}

	}

	public static class ChangeHistory {
		public long time;
		public String resource_key;
		public long count;
		public String change_reason_key;
		public String msg;
	}

	public static class GetChangeHistoryListCmd {

		public static class Request {
			public String resource_key;
			public long offset;
			public int count;
		}

		public static class Result extends LinkedList<ChangeHistory> {
			private static final long serialVersionUID = -1236770084966746653L;
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
