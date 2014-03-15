package com.luzi82.hikari.client.protocol;

import java.util.List;

import com.luzi82.hikari.client.protocol.HikariValueProtocolDef.AbstractValueChangeD;

public class HikariGachaProtocolDef {

	public static class GachaData {
		public String key;
	}

	public static class GachaCostValueChangeData extends
			AbstractValueChangeD {
	}

	public static class GachaCmd {

		public static class Request {
			public String gacha_key;
		}

		public static class Result {
			public List<Integer> user_card_id_list;
		}

	}

}
