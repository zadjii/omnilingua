package com.nth.kryogdx.requests;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 12/6/13
 * Time: 9:45 PM
 */
public class PlayerIndexResponse implements Serializable{

	private int index;
	private String playerName;
	public PlayerIndexResponse() {}
	public PlayerIndexResponse(int index, String playerName) {
		this.index = index;
		this.playerName = playerName;
	}

	public int getIndex() {
		return index;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
