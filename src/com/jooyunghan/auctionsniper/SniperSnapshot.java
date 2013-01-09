package com.jooyunghan.auctionsniper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SniperSnapshot {

	public final String itemId;
	public final int lastPrice;
	public final int lastBid;
	public final SniperState state;

	public SniperSnapshot(String itemId, int price, int bid, SniperState state) {
		this.itemId = itemId;
		this.lastPrice = price;
		this.lastBid = bid;
		this.state = state;
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public static SniperSnapshot joining(String itemId) {
		return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
	}

	public SniperSnapshot closed() {
		return new SniperSnapshot(itemId, lastPrice, lastBid,
				state.whenAuctionClosed());
	}

	public SniperSnapshot winning(int price) {
		return new SniperSnapshot(itemId, price, price, SniperState.WINNING);
	}

	public SniperSnapshot bidding(int price, int bid) {
		return new SniperSnapshot(itemId, price, bid, SniperState.BIDDING);
	}

	public SniperSnapshot losing(int newPrice) {
		return new SniperSnapshot(itemId, newPrice, lastBid, SniperState.LOSING);
	}

	public boolean isForSameItemAs(SniperSnapshot other) {
		return this.itemId.equals(other.itemId);
	}
}
