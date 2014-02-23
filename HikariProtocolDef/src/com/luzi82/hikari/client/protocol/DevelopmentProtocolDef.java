package com.luzi82.hikari.client.protocol;

public class DevelopmentProtocolDef {

	public static class PassTimeCmd {
		public static class Request {
			public long start;
			public long end;
		}

		public static class Result extends AbstractResult {
			public long time;
		}
	}

}
