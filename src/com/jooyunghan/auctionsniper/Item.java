package com.jooyunghan.auctionsniper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Item {
	public final String identifier;
	public final int stopPrice;

	public Item(String identifier, int stopPrice) {
		this.identifier = identifier;
		this.stopPrice = stopPrice;
	}

	public Item(String itemId) {
		this(itemId, Integer.MAX_VALUE);
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public boolean allowsBid(final int bid) {
		return bid <= stopPrice;
	}
}
