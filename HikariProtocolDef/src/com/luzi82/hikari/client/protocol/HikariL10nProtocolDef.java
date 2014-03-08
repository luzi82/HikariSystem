package com.luzi82.hikari.client.protocol;

public class HikariL10nProtocolDef {

	public static final String L10N_DATA_PARAM = "L10N";

	public static class L10nTextData {
		public static final String[] PARAM = { L10N_DATA_PARAM };
		public String text_key;
		public String text;
	}

	public static class L10nLangData {
		public String key;
	}

}
