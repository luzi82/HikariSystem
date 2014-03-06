package com.luzi82.hikari.client.test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.Card;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.apache.HsClientApache.StatusCodeException;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef.CardStatus;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef.CardTypeData;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef.DeskStatus;

public class CardTest extends AbstractTest {

	@Test
	public void testCardTypeList() throws Exception {
		HsClient client = createClient();
		createLogin(client);
		client.syncData(null).get();

		List<CardTypeData> cardTypeDataList = Card.getCardTypeDataList(client);
		Assert.assertEquals(3, cardTypeDataList.size());

		Assert.assertEquals("cardtype_0", cardTypeDataList.get(0).key);
	}

	@Test
	public void testCardStatus() throws Exception {
		HsClient client = createClient();
		createLogin(client);
		client.syncData(null).get();

		CardStatus cardList = Card.getCardStatusObservable(client).get();
		Assert.assertEquals(5, cardList.size());
	}

	@Test
	public void testCardValue() throws Exception {
		HsClient client = createClient();
		createLogin(client);
		// client.syncData(null).get();

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();
		Assert.assertEquals(5, cardStatus.size());

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

}
