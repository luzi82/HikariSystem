package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luzi82.hikari.client.protocol.HikariGachaProtocol;
import com.luzi82.hikari.client.protocol.HikariValueProtocolDef.AbstractValueChangeD;

public class Gacha extends HikariGachaProtocol {

	public static Map<String, Entry> getEntryDict(HsClient client) {
		List<GachaData> gachaDataList = getGachaDataList(client);

		if (gachaDataList == null)
			return null;

		List<GachaCostValueChangeData> gachaCostDataList = getGachaCostValueChangeDataList(client);
		
		Map<String, Entry> ret = new HashMap<String, Entry>();

		for (GachaData gachaData : gachaDataList) {
			Entry entry = new Entry();
			entry.key = gachaData.key;
			ret.put(entry.key, entry);
		}

		for (GachaCostValueChangeData gachaCostData : gachaCostDataList) {
			ret.get(gachaCostData.parent_key).resourceChangeDict.put(gachaCostData.value_key,
					gachaCostData);
		}

		return ret;
	}

	public static class Entry {

		public String key;
		public Map<String, AbstractValueChangeD> resourceChangeDict = new HashMap<String, AbstractValueChangeD>();

	}

}
