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
		Assert.assertEquals(4, deskStatus.desk_card_list_list.length);
		Assert.assertEquals(3, deskStatus.desk_card_list_list[0].length);

		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][0],
				cardStatus.get(0).id);
		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][1],
				cardStatus.get(1).id);
		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][2],
				cardStatus.get(2).id);
	}

	@Test
	public void testSetDesk() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();

		DeskStatus deskStatus = Card.getDeskStatusObservable(client).get();
		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][0],
				cardStatus.get(0).id);
		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][1],
				cardStatus.get(1).id);
		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][2],
				cardStatus.get(2).id);

		Integer[] deskCardList = {//
		cardStatus.get(2).id,//
				cardStatus.get(3).id,//
				cardStatus.get(4).id,//
		};
		Card.setDesk(client, 0, deskCardList, null).get();

		deskStatus = Card.getDeskStatusObservable(client).get();
		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][0],
				cardStatus.get(2).id);
		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][1],
				cardStatus.get(3).id);
		Assert.assertEquals((int) deskStatus.desk_card_list_list[0][2],
				cardStatus.get(4).id);
	}

	@Test
	public void testDeskSetDoubleCard() throws Exception {
		HsClient client = createClient();
		createLogin(client);

		CardStatus cardStatus = Card.getCardStatusObservable(client).get();

		Integer[] deskCardList = {//
		cardStatus.get(0).id,//
				cardStatus.get(0).id,//
				cardStatus.get(1).id,//
		};

		StatusCodeException sce = null;
		try {
			Card.setDesk(client, 0, deskCardList, null).get();
			Assert.fail();
		} catch (ExecutionException ee) {
			sce = (StatusCodeException) ee.getCause();
		}
		Assert.assertEquals(400, sce.code);
	}

}
