package com.luzi82.hikari.client.protocol;

import java.util.List;

public class ResourceProtocolDef {

	public static class Status {
		public List<ResourceKeyCount> resourceKeyCountList;
	}

	public static class ResourceKeyCount {
		public String resource_key;
		public int count;
	}

}
