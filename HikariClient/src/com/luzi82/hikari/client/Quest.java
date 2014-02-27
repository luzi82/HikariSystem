package com.luzi82.hikari.client;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.luzi82.hikari.client.protocol.HikariQuestProtocol;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef;

public class Quest extends HikariQuestProtocol {

	public static List<QuestCostData> filter(
			List<QuestCostData> allQuestCostList, String key) {
		List<QuestCostData> ret = new LinkedList<HikariQuestProtocolDef.QuestCostData>();
		for (QuestCostData data : allQuestCostList) {
			if (ObjectUtils.equals(data.quest_entry_key, key)) {
				ret.add(data);
			}
		}
		return ret;
	}

	public static QuestCostData get(List<QuestCostData> allQuestCostList,
			String questKey, String resourceKey) {
		for (QuestCostData data : allQuestCostList) {
			if (!ObjectUtils.equals(data.quest_entry_key, questKey))
				continue;
			if (!ObjectUtils.equals(data.resource_key, resourceKey))
				continue;
			return data;
		}
		return null;
	}

}
