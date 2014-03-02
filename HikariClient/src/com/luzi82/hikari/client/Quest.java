package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.luzi82.hikari.client.protocol.HikariQuestProtocol;
import com.luzi82.hikari.client.protocol.HikariResourceProtocolDef.AbstractResourceChangeD;

public class Quest extends HikariQuestProtocol {

	public static SortedMap<String, Entry> getEntryMap(HsClient client) {
		List<QuestEntryData> gachaDataList = getQuestEntryDataList(client);
		if (gachaDataList == null)
			return null;

		List<QuestCostResourceChangeData> gachaCostDataList = getQuestCostResourceChangeDataList(client);

		SortedMap<String, Entry> ret = new TreeMap<String, Entry>();

		for (QuestEntryData gachaData : gachaDataList) {
			Entry entry = new Entry();
			entry.key = gachaData.key;
			ret.put(entry.key, entry);
		}

		for (QuestCostResourceChangeData gachaCostData : gachaCostDataList) {
			ret.get(gachaCostData.parent_key).resourceChangeDict.put(
					gachaCostData.resource_key, gachaCostData);
		}

		return ret;
	}

	public static class Entry {

		public String key;
		public Map<String, AbstractResourceChangeD> resourceChangeDict = new HashMap<String, AbstractResourceChangeD>();

	}

}
