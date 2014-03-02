package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luzi82.hikari.client.protocol.HikariGachaProtocol;

public class Gacha extends HikariGachaProtocol {

	public static Map<String, Entry> getEntryDict(HsClient client) {
		Map<String, Entry> ret = new HashMap<String, Entry>();

		List<GachaData> gachaDataList = getGachaDataList(client);
		List<GachaCostData> gachaCostDataList = getGachaCostDataList(client);

		for (GachaData gachaData : gachaDataList) {
			Entry entry = new Entry();
			entry.key = gachaData.key;
			ret.put(entry.key, entry);
		}

		for (GachaCostData gachaCostData : gachaCostDataList) {
			Cost cost = new Cost();
			cost.resource_key = gachaCostData.resource_key;
			cost.value = gachaCostData.value;
			ret.get(gachaCostData.gacha_key).costDict.put(cost.resource_key,
					cost);
		}

		return ret;
	}

	public static class Entry {

		public String key;
		public Map<String, Cost> costDict = new HashMap<String, Gacha.Cost>();

	}

	public static class Cost {
		public String resource_key;
		public long value;
	}

}
