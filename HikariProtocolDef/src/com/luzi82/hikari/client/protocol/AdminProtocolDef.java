package com.luzi82.hikari.client.protocol;

public class AdminProtocolDef {

	public static class CreateAdminUserCmd {

		public static class Request {
			public String secret;
		}

		public static class Result {
			public String username;
			public String password;
		}
	}

	public static class CheckAdminCmd {
		public static class Request {
		}

		public static class Result {
		}
	}

}
