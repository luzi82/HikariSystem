package com.luzi82.hikari.client;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.luzi82.hikari.client.protocol.QuestProtocol;
import com.luzi82.hikari.client.protocol.QuestProtocolDef;

public class Quest extends QuestProtocol {

	public static List<HsQuestCostData> filter(
			List<HsQuestCostData> allQuestCostList, String key) {
		List<HsQuestCostData> ret = new LinkedList<QuestProtocolDef.HsQuestCostData>();
		for (HsQuestCostData data : allQuestCostList) {
			if (ObjectUtils.equals(data.quest_entry_key, key)) {
				ret.add(data);
			}
		}
		return ret;
	}

	public static HsQuestCostData get(List<HsQuestCostData> allQuestCostList,
			String questKey, String resourceKey) {
		for (HsQuestCostData data : allQuestCostList) {
			if (!ObjectUtils.equals(data.quest_entry_key, questKey))
				continue;
			if (!ObjectUtils.equals(data.resource_key, resourceKey))
				continue;
			return data;
		}
		return null;
	}

}
