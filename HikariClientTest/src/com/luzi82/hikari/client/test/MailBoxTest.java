package com.luzi82.hikari.client.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.MailBox;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.HikariMailBoxProtocolDef.Mail;

public class MailBoxTest extends AbstractTest {

	@Test
	public void testSendGetMail() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		HsClient client0 = createClient();
		createLogin(client0);

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();
		String clientUsername0 = client0.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		List<Mail> mailList = MailBox.getMailList(client, true, true, null)
				.get();
		Assert.assertEquals(0, mailList.size());

		long t0 = client.getServerTime(System.currentTimeMillis());

		MailBox.sendMail(client0, clientUsername, "HelloTitle", "HelloWorld",
				null).get();

		long t1 = client.getServerTime(System.currentTimeMillis());

		mailList = MailBox.getMailList(client0, true, true, null).get();
		Assert.assertEquals(1, mailList.size());
		Assert.assertTrue(mailList.get(0).time >= t0 - 1000);
		Assert.assertTrue(mailList.get(0).time <= t1 + 1000);
		Assert.assertEquals(clientUsername0, mailList.get(0).from_username);
		Assert.assertEquals("HelloTitle", mailList.get(0).title);
		Assert.assertEquals("HelloWorld", mailList.get(0).message);
		Assert.assertEquals(false, mailList.get(0).read);
	}

}
