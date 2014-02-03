package com.luzi82.hikari.client.protocol;

public class HsUserProtocol {

	public static class CreateUser {

		public static class Request {
			public String device_id;
		}

		public static class Result {
			public String username;
			public String password;
		}
	}

}
