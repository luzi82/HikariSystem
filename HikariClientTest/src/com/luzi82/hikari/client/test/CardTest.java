package com.luzi82.hikari.client.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.hikari.client.Card;
import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef.CardStatus;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef.CardTypeData;

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

		CardStatus cardList = Card.getCardStatusObservable(client)
				.get();
		Assert.assertEquals(5, cardList.size());
	}

//	@Test
//	public void testCardValue() throws Exception {
//		HsClient client = createClient();
//		createLogin(client);
//		client.syncData(null).get();
//
//		CardMapStatus cardList = Card.getCardMapStatusObservable(client)
//				.get();
//		Assert.assertEquals(5, cardList.size());
//		
//	}

}
