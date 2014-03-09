package com.luzi82.hikari.client.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.MailBox;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.protocol.HikariMailBoxProtocolDef.MailStatus;
import com.luzi82.hikari.client.protocol.HikariProtocol;
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

		MailStatus mailStatus = MailBox.getMailStatusObservable(client).get();
		Assert.assertEquals(0, mailStatus.readCount);
		Assert.assertEquals(0, mailStatus.unreadCount);

		long t0 = client.getServerTime(System.currentTimeMillis());

		MailBox.sendMail(client0, clientUsername, "HelloTitle", "HelloWorld",
				null).get();

		long t1 = client.getServerTime(System.currentTimeMillis());

		HikariProtocol.syncStatus(client, null).get();

		mailStatus = MailBox.getMailStatusObservable(client).get();
		Assert.assertEquals(0, mailStatus.readCount);
		Assert.assertEquals(1, mailStatus.unreadCount);

		mailList = MailBox.getMailList(client0, true, true, null).get();
		Assert.assertEquals(1, mailList.size());
		Assert.assertTrue(mailList.get(0).time >= t0 - 1000);
		Assert.assertTrue(mailList.get(0).time <= t1 + 1000);
		Assert.assertEquals(clientUsername0, mailList.get(0).from_username);
		Assert.assertEquals("HelloTitle", mailList.get(0).title);
		Assert.assertEquals("HelloWorld", mailList.get(0).message);
		Assert.assertEquals(false, mailList.get(0).read);
	}

	@Test
	public void testSetReadCmd() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		HsClient client0 = createClient();
		createLogin(client0);

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();

		MailBox.sendMail(client0, clientUsername, "HelloTitle", "HelloWorld",
				null).get();

		List<Mail> mailList = MailBox.getMailList(client, true, true, null)
				.get();
		Assert.assertEquals(1, mailList.size());

		MailStatus mailStatus = MailBox.getMailStatusObservable(client).get();
		Assert.assertEquals(1, mailStatus.readCount);
		Assert.assertEquals(0, mailStatus.unreadCount);

		int mailId = mailList.get(0).id;

		mailList = MailBox.getMailList(client0, true, false, null).get();
		Assert.assertEquals(0, mailList.size());

		mailList = MailBox.getMailList(client0, false, true, null).get();
		Assert.assertEquals(1, mailList.size());
		Assert.assertEquals(mailId, mailList.get(0).id);

		MailBox.setRead(client, mailId, true, null).get();

		mailStatus = MailBox.getMailStatusObservable(client).get();
		Assert.assertEquals(1, mailStatus.readCount);
		Assert.assertEquals(0, mailStatus.unreadCount);

		mailList = MailBox.getMailList(client0, true, true, null).get();
		Assert.assertEquals(1, mailList.size());
		Assert.assertEquals(mailId, mailList.get(0).id);

		mailList = MailBox.getMailList(client0, true, false, null).get();
		Assert.assertEquals(1, mailList.size());
		Assert.assertEquals(mailId, mailList.get(0).id);

		mailList = MailBox.getMailList(client0, false, true, null).get();
		Assert.assertEquals(0, mailList.size());

		MailBox.setRead(client, mailId, false, null).get();

		mailStatus = MailBox.getMailStatusObservable(client).get();
		Assert.assertEquals(0, mailStatus.readCount);
		Assert.assertEquals(1, mailStatus.unreadCount);

		mailList = MailBox.getMailList(client0, true, true, null).get();
		Assert.assertEquals(1, mailList.size());
		Assert.assertEquals(mailId, mailList.get(0).id);

		mailList = MailBox.getMailList(client0, true, false, null).get();
		Assert.assertEquals(0, mailList.size());

		mailList = MailBox.getMailList(client0, false, true, null).get();
		Assert.assertEquals(1, mailList.size());
		Assert.assertEquals(mailId, mailList.get(0).id);
	}

}
