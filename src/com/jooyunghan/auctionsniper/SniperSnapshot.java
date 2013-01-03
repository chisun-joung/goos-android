package com.jooyunghan.auctionsniper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SniperSnapshot {

	public final String itemId;
	public final int lastPrice;
	public final int lastBid;

	public SniperSnapshot(String itemId, int price, int bid) {
		this.itemId = itemId;
		this.lastPrice = price;
		this.lastBid = bid;
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
}
