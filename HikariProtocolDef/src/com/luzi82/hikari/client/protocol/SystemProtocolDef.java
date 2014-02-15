package com.luzi82.hikari.client.protocol;

public class SystemProtocolDef {

	public static class GetTimeCmd {

		public static class Request {
		}

		public static class Result {
			public long time;
		}

	}

}
