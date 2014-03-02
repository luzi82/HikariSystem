package com.luzi82.hikari.client.protocol;

import java.util.List;

public class HikariGachaProtocolDef {

	public static class GachaData {
		public String key;
	}

	public static class GachaCostData {
		public String gacha_key;
		public String resource_key;
		public long value;
	}

	public static class GachaCmd {

		public static class Request {
			public String gacha_key;
		}

		public static class Result {
			public List<Integer> item_id_list;
		}

	}

}
