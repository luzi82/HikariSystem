package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.Map;

import com.luzi82.hikari.client.protocol.HikariL10nProtocol;

public class L10n extends HikariL10nProtocol {

	public static void setL10n(HsClient client, String string) {
		client.setStaticDataParam(L10N_DATA_PARAM, string);
	}

	public static Map<String, String> getTextMap(HsClient client) {
		Map<String, String> ret = new HashMap<String, String>();

		for (L10nData ld : getL10nDataList(client)) {
			ret.put(ld.key, ld.text);
		}

		return ret;
	}

}
