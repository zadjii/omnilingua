package com.nth.kryogdx.requests;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/10/14
 * Time: 10:42 AM
 */
public class PlayerConnectRequest implements Serializable{

	private String playerName;

	public PlayerConnectRequest(){}
	public PlayerConnectRequest(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}
}
