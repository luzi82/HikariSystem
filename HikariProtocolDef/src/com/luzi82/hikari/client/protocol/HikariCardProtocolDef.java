package com.luzi82.hikari.client.protocol;

import java.util.Map;
import java.util.TreeMap;

import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestInstance;

public class HikariCardProtocolDef {

	public static class CardTypeData {
		public String key;
	}

	public static class CardStatus extends TreeMap<Integer, Card> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2417872520439884077L;

	}

	public static class DeskStatus {

		public Integer[][] desk_card_list_list;

	}

	public static class Card {
		public int id;
		public String card_type_key;
		public Map<String, Object> value_dict;
	}

	public static class SetDeskCmd {

		public static class Request {
			public int desk_id;
			public Integer[] card_list;
		}

		public static class Result {
		}

	}

}
