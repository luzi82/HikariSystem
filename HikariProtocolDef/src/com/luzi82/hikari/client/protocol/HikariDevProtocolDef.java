package com.luzi82.hikari.client.protocol;

public class HikariDevProtocolDef {

	public static class PassTimeCmd {
		public static class Request {
			public long start;
			public long end;
		}

		public static class Result {
			public long time;
		}
	}

}
