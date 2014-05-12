package com.nth.kryogdx.util;

import java.util.HashMap;

/**
 * Just a single update from a client, only the keys/things that changed on
 * this particular update. A CIR("sir") is applied to a Player's ClientInputState
 * before updating the engine, and the CIS("sis") represents all of that
 * Player's own input states.
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/10/14
 * Time: 3:46 PM
 */
public class ClientInputRequest {
	/*package*/ HashMap<Integer, Integer> inputUpdates;
	/*package*/ long updateIndex;
	/*package*/ int playerIndex = Constants.UNASSIGNED;

	public ClientInputRequest(){
		inputUpdates = new HashMap<Integer, Integer>();
	}

	public void pressKey(int key){
		addKeyState(key, ClientInputState.PRESSED);
	}
	public void releaseKey(int key){
		addKeyState(key, ClientInputState.RELEASED);
	}
	public void addKeyState(int key, int state){
		inputUpdates.put(key, state);
	}

	public long getUpdateIndex() {
		return updateIndex;
	}

	public void setUpdateIndex(long updateIndex) {
		this.updateIndex = updateIndex;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public String getInputUpdateContents(){return inputUpdates.toString();}
}
