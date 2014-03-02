package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luzi82.hikari.client.protocol.HikariGachaProtocol;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.AbstractResourceChangeD;

public class Gacha extends HikariGachaProtocol {

	public static Map<String, Entry> getEntryDict(HsClient client) {
		List<GachaData> gachaDataList = getGachaDataList(client);

		if (gachaDataList == null)
			return null;

		List<GachaCostResourceChangeData> gachaCostDataList = getGachaCostResourceChangeDataList(client);
		
		Map<String, Entry> ret = new HashMap<String, Entry>();

		for (GachaData gachaData : gachaDataList) {
			Entry entry = new Entry();
			entry.key = gachaData.key;
			ret.put(entry.key, entry);
		}

		for (GachaCostResourceChangeData gachaCostData : gachaCostDataList) {
			ret.get(gachaCostData.parent_key).resourceChangeDict.put(gachaCostData.resource_key,
					gachaCostData);
		}

		return ret;
	}

	public static class Entry {

		public String key;
		public Map<String, AbstractResourceChangeD> resourceChangeDict = new HashMap<String, AbstractResourceChangeD>();

	}

}
