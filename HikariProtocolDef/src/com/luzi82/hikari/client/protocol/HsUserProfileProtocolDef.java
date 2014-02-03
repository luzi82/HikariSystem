package com.luzi82.hikari.client.protocol;

public class HsUserProfileProtocolDef {

	public static class SetNameCmd {

		public static class Request {
			public String name;
		}

		public static class Result {
		}

	}

	public static class Profile {
		public String username;
		public String name;
	}

	public static class GetProfileCmd {

		public static class Request {
			public String username;
		}

		public static class Result {
			public Profile profile;
		}

	}

}
