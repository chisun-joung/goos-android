package com.jooyunghan.auctionsniper.xmpp;


public class XMPPAuctionException extends Exception {
	private static final long serialVersionUID = 1570210765867851451L;

	public XMPPAuctionException(String message, Exception e) {
		super(message, e);
	}
}
