package com.luzi82.hikari.client.protocol;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class AbstractResult {

	public static class StatusUpdate {
		public String app_name;
		public JsonNode status;
	}

	public List<StatusUpdate> status_update_list;

}
