package com.luzi82.hikari.client.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.Card;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.MailBox;
import com.luzi82.hikari.client.User;
import com.luzi82.hikari.client.apache.HsClientApache.StatusCodeException;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef.CardStatus;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef.CardTypeData;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef.DeskStatus;
import com.luzi82.hikari.client.protocol.HikariMailProtocolDef.Mail;
import com.luzi82.hikari.client.protocol.Item;

public class CardTest extends AbstractTest {

	@Test
	public void testCardTypeList() throws Exception {
		HsClient client = createClient();
		createLogin(client);
		client.syncData(null).get();

		List<CardTypeData> cardTypeDataList = Card.getCardTypeDataList(client);
		Assert.assertEquals(6, cardTypeDataList.size());

		Assert.assertEquals("cardtype_0", cardTypeDataList.get(0).key);
	}

	@Test
	public void testCardStatus() throws Exception {
		HsClient client = createClient();
		createLogin(client);
		client.syncData(null).get();

		CardStatus cardList = Card.getCardStatusObservable(client).get();
		Assert.assertEquals(6, cardList.size());
	}

	@Test
	public void testCardValue() throws Exception {
		HsClient client = createClient();
		createLogin(client);
		// client.syncData(null).get();

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();
		Card.Card card = cardStatus.firstEntry().getValue();
		Assert.assertEquals(1, card.value_dict.get("power"));
	}

	@Test
	public void testDeskStatus() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();

		DeskStatus deskStatus = Card.getDeskStatusObservable(client).get();
		Assert.assertEquals(4, deskStatus.get("pet").size());
		Assert.assertEquals(3, deskStatus.get("pet").get(0).length);

		Assert.assertNotNull(deskStatus.get("pet").get(0)[0]);
		Integer[] cardIdAry = cardStatus.keySet().toArray(new Integer[0]);
		Assert.assertNotNull(cardStatus.firstEntry().getValue().id);
		Assert.assertEquals((int) cardIdAry[0], (int) deskStatus.get("pet")
				.get(0)[0]);
		Assert.assertEquals((int) cardIdAry[1], (int) deskStatus.get("pet")
				.get(0)[1]);
		Assert.assertEquals((int) cardIdAry[2], (int) deskStatus.get("pet")
				.get(0)[2]);
	}

	@Test
	public void testSetDesk() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();
		Integer[] cardIdAry = cardStatus.keySet().toArray(new Integer[0]);

		DeskStatus deskStatus = Card.getDeskStatusObservable(client).get();
		Assert.assertEquals((int) cardIdAry[0], (int) deskStatus.get("pet")
				.get(0)[0]);
		Assert.assertEquals((int) cardIdAry[1], (int) deskStatus.get("pet")
				.get(0)[1]);
		Assert.assertEquals((int) cardIdAry[2], (int) deskStatus.get("pet")
				.get(0)[2]);

		Integer[] deskCardList = {//
		cardIdAry[2],//
				cardIdAry[3],//
				cardIdAry[4],//
		};
		Card.setDesk(client, "pet", 0, deskCardList, null).get();

		deskStatus = Card.getDeskStatusObservable(client).get();
		Assert.assertEquals((int) cardIdAry[2], (int) deskStatus.get("pet")
				.get(0)[0]);
		Assert.assertEquals((int) cardIdAry[3], (int) deskStatus.get("pet")
				.get(0)[1]);
		Assert.assertEquals((int) cardIdAry[4], (int) deskStatus.get("pet")
				.get(0)[2]);
	}

	@Test
	public void testDeskSetDoubleCard() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();
		Integer[] cardIdAry = cardStatus.keySet().toArray(new Integer[0]);

		Integer[] deskCardList = {//
		cardIdAry[0],//
				cardIdAry[0],//
				cardIdAry[1],//
		};

		StatusCodeException sce = null;
		try {
			Card.setDesk(client, "pet", 0, deskCardList, null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(400, sce.code);
	}

	// TODO desk set invalid card test

	@Test
	public void testDeskSetNonExistCard() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		Integer[] deskCardList = {//
		Integer.MAX_VALUE,//
				Integer.MAX_VALUE - 1,//
				Integer.MAX_VALUE - 2,//
		};

		StatusCodeException sce = null;
		try {
			Card.setDesk(client, "pet", 0, deskCardList, null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(400, sce.code);
	}

	@Test
	public void testDeskSetNonOwnCard() throws Exception {
		HsClient client0 = createClient();
		createLogin(client0);

		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client0).get();
		Integer[] cardIdAry = cardStatus.keySet().toArray(new Integer[0]);

		Integer[] deskCardList = {//
		cardIdAry[0],//
				cardIdAry[1],//
				cardIdAry[2],//
		};

		StatusCodeException sce = null;
		try {
			Card.setDesk(client, "pet", 0, deskCardList, null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(400, sce.code);
	}

	@Test
	public void testSetNegativeDesk() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();
		Integer[] cardIdAry = cardStatus.keySet().toArray(new Integer[0]);

		Integer[] deskCardList = {//
		cardIdAry[2],//
				cardIdAry[3],//
				cardIdAry[4],//
		};
		StatusCodeException sce = null;
		try {
			Card.setDesk(client, "pet", -1, deskCardList, null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(400, sce.code);
	}

	@Test
	public void testSetOverDesk() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();
		Integer[] cardIdAry = cardStatus.keySet().toArray(new Integer[0]);

		Integer[] deskCardList = {//
		cardIdAry[2],//
				cardIdAry[3],//
				cardIdAry[4],//
		};
		StatusCodeException sce = null;
		try {
			Card.setDesk(client, "pet", 4, deskCardList, null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(400, sce.code);
	}

	@Test
	public void testSetDeskBadTag() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();
		Integer[] cardIdAry = cardStatus.keySet().toArray(new Integer[0]);

		Integer[] deskCardList = {//
		cardIdAry[0],//
				cardIdAry[1],//
				cardIdAry[5],//
		};
		StatusCodeException sce = null;
		try {
			Card.setDesk(client, "pet", 0, deskCardList, null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(400, sce.code);
	}

	@Test
	public void testGiftCard() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		HsClient admin = createAdmin();

		String clientUsername = client.get(User.APP_NAME, User.DB_USERNAME,
				null).get();
		Item.ListMap itemListMap = new Item.ListMap();
		Card.addCardItem(itemListMap, "cardtype_0");

		MailBox.sendGiftMail(admin, clientUsername, "HelloTitle", "HelloWorld",
				itemListMap, null).get();

		List<Mail> mailList = MailBox.getMailList(client, true, true, 0, 10,
				null).get();
		Assert.assertEquals(1, mailList.size());

		List<Card.CardItem> mailAttachList = Card.getCardItemList(client,
				mailList.get(0).item_list_map);
		Assert.assertEquals(1, mailAttachList.size());
		Assert.assertEquals("cardtype_0", mailAttachList.get(0).card_type_key);

		CardStatus cardStatus0 = Card.getCardStatusObservable(client).get();

		MailBox.setRead(client, mailList.get(0).id, true, null).get();

		CardStatus cardStatus1 = Card.getCardStatusObservable(client).get();

		Assert.assertEquals(cardStatus0.size() + 1, cardStatus1.size());
		Set<Integer> extraCard = new HashSet<Integer>(cardStatus1.keySet());
		extraCard.removeAll(cardStatus0.keySet());
		Assert.assertEquals(1, extraCard.size());
		int cardId = extraCard.toArray(new Integer[0])[0];
		Assert.assertEquals("cardtype_0", cardStatus1.get(cardId).card_type_key);
	}
}
