package com.luzi82.hikari.client.protocol;

import java.util.LinkedList;

public class HikariMailBoxProtocolDef {

	public static class MailStatus {
		public int readCount;
		public int unreadCount;
	}

	public static class Mail {
		public int id;
		public long time;
		public String from_username;
		public String title;
		public String message;
		public boolean read;
	}

	public static class GetMailListCmd {
		public static class Request {
			public boolean read;
			public boolean unread;
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
			public int id;
			public boolean read;
		}

		public static class Result {
		}
		
	}

}
