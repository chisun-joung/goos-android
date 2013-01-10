package com.jooyunghan.auctionsniper.xmpp;

public class MissingValueException extends Exception {

	private static final long serialVersionUID = 7129635533055863158L;

	public MissingValueException(String fieldName) {
		super(fieldName + " value is missing.");
	}

}
