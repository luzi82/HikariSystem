package com.luzi82.hikari.client.protocol;


public class QuestProtocolDef {

	public static class QuestEntryData {
		public String key;
	}

	public static class QuestStartCmd {

		public static class Request {
			public String quest_entry_key;
		}

		public static class Result {
			public int quest_instance_id;
		}

	}

	public static class QuestEndCmd {

		public static class Request {
			public int quest_instance_id;
			public boolean success;
		}

		public static class Result {
		}
	}

}
