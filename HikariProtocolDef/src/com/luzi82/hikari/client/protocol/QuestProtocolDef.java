package com.luzi82.hikari.client.protocol;

public class QuestProtocolDef {

	public static class HsQuestEntryData {
		public String key;
	}
	
	public static class HsQuestCostData {
		public String quest_entry_key;
		public String resource_key;
		public int count;
	}

	public static class QuestInstance {
		public int id;
	}

	public static class QuestStartCmd {

		public static class Request {
			public String quest_entry_key;
		}

		public static class Result {
			public QuestInstance quest_instance;
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
