package com.luzi82.hikari.client.protocol;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HikariCardProtocolDef {

	public static class CardTypeData {
		public String key;
	}

	public static class CardStatus extends TreeMap<Integer, Card> {

		private static final long serialVersionUID = -2417872520439884077L;

	}

	public static class Card {
		public int id;
		public String card_type_key;
		public Map<String, Object> value_dict;
	}

	public static class DeskStatus extends TreeMap<String, List<Integer[]>> {

		private static final long serialVersionUID = -3890914451228135129L;

	}

	public static class SetDeskCmd {

		public static class Request {
			public String desk_type_key;
			public int desk_id;
			public Integer[] card_list;
		}

		public static class Result {
		}

	}

}
