package com.luzi82.hikari.client.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.HsMemStorage;
import com.luzi82.hikari.client.HsUser;
import com.luzi82.hikari.client.protocol.HsUserProtocol;
import com.luzi82.hikari.client.protocol.HsUserProtocol.CreateUserResult;

public class HsUserTest {

	public static String SERVER = "http://192.168.1.50";

	@Test
	public void testCreateUser() throws InterruptedException,
			ExecutionException, TimeoutException {
		HsClient client = new HsClient(SERVER, new HsMemStorage(
				Executors.newCachedThreadPool()));

		// HsWaiter<HsUserCreateUserResult> createUserWaiter = new
		// HsWaiter<HsUser.CreateUserResult>();
		final HsUserProtocol.CreateUserResult[] curv = new HsUserProtocol.CreateUserResult[1];
		Future<HsUserProtocol.CreateUserResult> f = HsUser.createUser(client, "test_dev",
				new FutureCallback<HsUserProtocol.CreateUserResult>() {
					@Override
					public void cancelled() {
					}

					@Override
					public void completed(HsUserProtocol.CreateUserResult arg0) {
						curv[0] = arg0;
					}

					@Override
					public void failed(Exception arg0) {
						arg0.printStackTrace();
					}
				});

		Assert.assertNotNull(f);

		// System.err.println("0");
		CreateUserResult cur = f.get(5, TimeUnit.SECONDS);
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
		Assert.assertEquals(cur.username, HsUser
				.getUsername(client, usernameFc).get(1, TimeUnit.SECONDS));
		Assert.assertEquals(cur.username, usernameV[0]);
		Assert.assertEquals(cur.password, HsUser
				.getPassword(client, passwordFc).get(1, TimeUnit.SECONDS));
		Assert.assertEquals(cur.password, passwordV[0]);
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
		HttpGet req = new HttpGet("http://www.google.com");
		final HttpResponse[] hr = new HttpResponse[1];
		Future<HttpResponse> future = httpclient.execute(req,
				new FutureCallback<HttpResponse>() {
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

		Assert.assertNotNull(hrr);
		Assert.assertEquals(hrr, hr[0]);
	}

}
