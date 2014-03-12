package com.luzi82.hikari.client.protocol;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface Item {

	public class ListMap extends HashMap<String, List<Item>> {

		private static final long serialVersionUID = 1942772396301415994L;

		public void attach(String key, Item attach) {
			List<Item> attachList = get(key);
			if (attachList == null) {
				attachList = new LinkedList<Item>();
				put(key, attachList);
			}
			attachList.add(attach);
		}

		public static <T> List<T> toList(List<JsonNode> jsonNodeList,
				Class<T> clazz, ObjectMapper objectMapper) {
			List<T> ret = new LinkedList<T>();
			for (JsonNode jsonNode : jsonNodeList) {
				ret.add(objectMapper.convertValue(jsonNode, clazz));
			}
			return ret;
		}

	}
}
