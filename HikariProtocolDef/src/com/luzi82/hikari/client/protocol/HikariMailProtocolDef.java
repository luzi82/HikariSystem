package com.luzi82.hikari.client.protocol;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class HikariMailProtocolDef {

	public static class MailStatus {
		public int read_count;
		public int unread_count;
	}

	public static class Mail {
		public int id;
		public long time;
		public String from_username;
		public String title;
		public String message;
		public boolean read;
		public Map<String, List<JsonNode>> item_list_map;
	}

	public static class GetMailListCmd {
		public static class Request {
			public boolean read;
			public boolean unread;
			public int offset;
			public int count;
		}

		public static class Result extends LinkedList<Mail> {
			private static final long serialVersionUID = -579763863924411479L;
		}
	}

	public static class SendMailCmd {
		public static class Request {
			public String username;
			public String title;
			public String message;
		}

		public static class Result {
		}
	}

	public static class SetReadCmd {

		public static class Request {
			public int mail_id;
			public boolean read;
		}

		public static class Result {
		}

	}

	public static class SendGiftMailCmd {
		public static class Request {
			public String username;
			public String title;
			public String message;
			public Item.ListMap item_list_map;
		}

		public static class Result {
		}
	}

}
