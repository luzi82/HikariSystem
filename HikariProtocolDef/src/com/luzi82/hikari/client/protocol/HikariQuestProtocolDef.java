package com.luzi82.hikari.client.protocol;

import com.luzi82.hikari.client.protocol.HikariValueProtocolDef.AbstractValueChangeD;

public class HikariQuestProtocolDef {

	public static class QuestEntryData {
		public String key;
	}

	public static class QuestCostValueChangeData extends
			AbstractValueChangeD {
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
