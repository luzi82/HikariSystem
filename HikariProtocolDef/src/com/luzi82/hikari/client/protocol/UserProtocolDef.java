package com.luzi82.hikari.client.protocol;

public class UserProtocolDef {

	public static class CreateUserCmd {

		public static class Request {
			public String device_model;
		}

		public static class Result {
			public String username;
			public String password;
		}
	}

	public static class LoginCmd {
		public static class Request {
			public String username;
			public String password;
		}

		public static class Result {
		}
	}

	public static class CheckLoginCmd {
		public static class Request {
		}

		public static class Result {
		}
	}

}
