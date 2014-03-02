package com.luzi82.hikari.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.luzi82.hikari.client.protocol.HikariQuestProtocol;

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
			ResourceChange cost = new ResourceChange();
			cost.resource_key = gachaCostData.resource_key;
			cost.change = gachaCostData.change;
			ret.get(gachaCostData.parent_key).resourceChangeDict.put(cost.resource_key,
					cost);
		}

		return ret;
	}
	
	public static class Entry {

		public String key;
		public Map<String, ResourceChange> resourceChangeDict = new HashMap<String, ResourceChange>();

	}

	public static class ResourceChange {
		public String resource_key;
		public long change;
	}

}
