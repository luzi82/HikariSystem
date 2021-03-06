package com.luzi82.hikari.client.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.apache.HsClientApache.StatusCodeException;
import com.luzi82.hikari.client.protocol.HikariUserProtocolDef;

public class UserTest extends AbstractTest {

	@Test
	public void testCreateUser() throws Exception {
		HsClient client = createClient();

		final HikariUserProtocolDef.CreateUserCmd.Result[] curv = new HikariUserProtocolDef.CreateUserCmd.Result[1];
		Future<HikariUserProtocolDef.CreateUserCmd.Result> f = User.createUser(
				client, TEST_DEV,
				new FutureCallback<HikariUserProtocolDef.CreateUserCmd.Result>() {
					@Override
					public void cancelled() {
					}

					@Override
					public void completed(
							HikariUserProtocolDef.CreateUserCmd.Result arg0) {
						curv[0] = arg0;
					}

					@Override
					public void failed(Exception arg0) {
						arg0.printStackTrace();
					}
				});

		Assert.assertNotNull(f);

		// System.err.println("0");
		HikariUserProtocolDef.CreateUserCmd.Result cur = f.get(5, TimeUnit.SECONDS);
		// System.err.println("1");
		Assert.assertNotNull(cur);
		Assert.assertEquals(cur, curv[0]);

		Assert.assertNotNull(cur.username);
		Assert.assertNotNull(cur.password);

		final String[] usernameV = new String[1];
		final String[] passwordV = new String[1];
		FutureCallback<String> usernameFc = new FutureCallback<String>() {
			@Override
			public void failed(Exception arg0) {
				arg0.printStackTrace();
			}

			@Override
			public void completed(String arg0) {
				usernameV[0] = arg0;
			}

			@Override
			public void cancelled() {
			}
		};
		FutureCallback<String> passwordFc = new FutureCallback<String>() {
			@Override
			public void failed(Exception arg0) {
				arg0.printStackTrace();
			}

			@Override
			public void completed(String arg0) {
				passwordV[0] = arg0;
			}

			@Override
			public void cancelled() {
			}
		};
		Assert.assertEquals(
				cur.username,
				client.get(User.APP_NAME, User.DB_USERNAME, usernameFc).get(1,
						TimeUnit.SECONDS));
		Assert.assertEquals(cur.username, usernameV[0]);
		Assert.assertEquals(
				cur.password,
				client.get(User.APP_NAME, User.DB_PASSWORD, passwordFc).get(1,
						TimeUnit.SECONDS));
		Assert.assertEquals(cur.password, passwordV[0]);
	}

	@Test
	public void testBadLogin() throws Exception {
		HsClient client = createClient();

		StatusCodeException sce = null;
		try {
			User.login(client, "XXX", "XXX", null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(401, sce.code);
	}

	@Test
	public void testLogin() throws Exception {
		HsClient client = createClient();

		User.createUser(client, TEST_DEV, null).get(5, TimeUnit.SECONDS);

		User.login(client, null).get(5, TimeUnit.SECONDS);
	}

	@Test
	public void testCheckLogin() throws Exception {
		HsClient client = createClient();

		try {
			User.checkLogin(client, null).get(5, TimeUnit.SECONDS);
			Assert.fail();
		} catch (ExecutionException ee) {
		}

		User.createUser(client, TEST_DEV, null).get(5, TimeUnit.SECONDS);
		User.login(client, null).get(5, TimeUnit.SECONDS);
		User.checkLogin(client, null).get(5, TimeUnit.SECONDS);
	}

	@Test
	public void testJson() throws JsonProcessingException {
		class X {
			@SuppressWarnings("unused")
			public String a;
			@SuppressWarnings("unused")
			public int b;
		}
		X x = new X();
		x.a = "c";
		x.b = 42;
		ObjectMapper om = new ObjectMapper();
		String s = om.writeValueAsString(x);
		// System.err.println(s);
		Assert.assertEquals("{\"a\":\"c\",\"b\":42}", s);
	}

	@Test
	public void testWeb() throws InterruptedException, ExecutionException {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		httpclient.start();
		HttpGet req = new HttpGet("http://www.luzi82.com");
		final HttpResponse[] hr = new HttpResponse[1];
		Future<HttpResponse> future = httpclient.execute(req,
				new org.apache.http.concurrent.FutureCallback<HttpResponse>() {
					@Override
					public void failed(Exception arg0) {
						arg0.printStackTrace();
					}

					@Override
					public void completed(HttpResponse arg0) {
						hr[0] = arg0;
					}

					@Override
					public void cancelled() {
					}
				});
		HttpResponse hrr = future.get();

		Thread.sleep(100); // WARNING: get may faster than completed

		Assert.assertNotNull(hrr);
		Assert.assertEquals(hrr, hr[0]);
	}

}
