package com.luzi82.hikari.client;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.luzi82.hikari.client.protocol.HikariQuestProtocol;
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef;

public class Quest extends HikariQuestProtocol {

	public static List<QuestCostResourceChangeData> filter(
			List<QuestCostResourceChangeData> allQuestCostList, String key) {
		List<QuestCostResourceChangeData> ret = new LinkedList<HikariQuestProtocolDef.QuestCostResourceChangeData>();
		for (QuestCostResourceChangeData data : allQuestCostList) {
			if (ObjectUtils.equals(data.parent_key, key)) {
				ret.add(data);
			}
		}
		return ret;
	}

	public static QuestCostResourceChangeData get(
			List<QuestCostResourceChangeData> allQuestCostList,
			String questKey, String resourceKey) {
		for (QuestCostResourceChangeData data : allQuestCostList) {
			if (!ObjectUtils.equals(data.parent_key, questKey))
				continue;
			if (!ObjectUtils.equals(data.resource_key, resourceKey))
				continue;
			return data;
		}
		return null;
	}

}
