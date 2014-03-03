package com.luzi82.hikari.client.android.demo;

import java.util.LinkedList;

import android.content.Context;

import com.luzi82.hikari.client.Card;
import com.luzi82.hikari.client.protocol.HikariCardProtocolDef;

public class CardView extends HikariListView implements
		HikariListView.UpdateList {

	UpdateListObserver updateListObserver;

	public CardView(Context context) {
		super(context);

		updateListObserver = new UpdateListObserver(this);

		Card.getCardStatusObservable(getClient()).addObserver(
				updateListObserver);
		updateList();
	}

	public void updateList() {
		LinkedList<Item> itemList = new LinkedList<HikariListView.Item>();

		HikariCardProtocolDef.CardStatus cardListStatus = Card
				.getCardStatusObservable(getClient()).get();
		if (cardListStatus != null) {
			for (Card.Card card : cardListStatus.values()) {
				itemList.add(new Item(String.format("%d: %s", card.id,
						card.card_type_key)));
			}
		}

		setItemList(itemList);
	}
}
