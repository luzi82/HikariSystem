package com.luzi82.hikari.client.protocol;

import java.util.TreeMap;


public class HikariCardProtocolDef {

	public static class CardTypeData {
		public String key;
	}

//	public static class CardValueTypeData {
//		public String key;
//		public String type;
//	}
//
//	public static abstract class CardValueD<T> {
//		public String card_type_key;
//		public String card_value_type_key;
//		public T value;
//	}
//
//	public static class CardIntValueData extends CardValueD<Integer> {
//	}
//
//	public static class CardStringValueData extends CardValueD<String> {
//	}

	public static class CardListStatus extends TreeMap<Integer, Card> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2417872520439884077L;

	}

	public static class Card {
		public int id;
		public String card_type_key;
	}

}
