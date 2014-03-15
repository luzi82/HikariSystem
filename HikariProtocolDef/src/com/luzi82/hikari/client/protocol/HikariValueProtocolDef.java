package com.luzi82.hikari.client.protocol;

import java.util.LinkedList;
import java.util.TreeMap;

public class HikariValueProtocolDef {

	public static class ValueData {
		public final static int TYPE_COUNT = 1;
		public final static int TYPE_TIME = 2;
		public String key;
		public int type;
	}

	public static class ValueConvertData {
		public String key;
	}

	public static class AbstractValueChangeD {
		public String parent_key;
		public String value_key;
		public long change;
	}

	public static class ValueConvertChangeData extends
			AbstractValueChangeD {
	}

	public static class ValueStatus extends TreeMap<String, UserValue> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2417872520439884077L;

	}

	public static class UserValue {
		public String value_key;
		public Integer count;
		public Long time;
		public Long max;
	}

	public static class ConvertCmd {

		public static class Request {
			public String value_convert_key;
			public long count;
		}

		public static class Result {
		}

	}

	public static class ConvertHistory {
		public long time;
		public String value_convert_key;
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
		public String value_key;
		public long value;
		public String change_reason_key;
		public String change_reason_msg;
	}

	public static class GetChangeHistoryListCmd {

		public static class Request {
			public String value_key;
			public long offset;
			public int count;
		}

		public static class Result extends LinkedList<ChangeHistory> {
			private static final long serialVersionUID = -1236770084966746653L;
		}

	}

	public static class SetUserValueCountCmd {

		public static class Request {
			public String username;
			public String value_key;
			public long count;
		}

		public static class Result {
		}

	}

	public static class ValueItem implements Item {
		public String value_key;
		public long value;
		public String change_reason_key;
		public String change_reason_msg;
	}

}
