package com.nth.kryogdx.personas;

import com.badlogic.gdx.Input;
import com.nth.kryogdx.game.Engine;
import com.nth.kryogdx.util.ClientInputRequest;
import com.nth.kryogdx.util.ClientInputState;
import com.nth.kryogdx.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/10/14
 * Time: 11:27 AM
 */
public class Player extends Persona{

	private String playerName;
	private int playerIndex;
	private ClientInputState input;
	private long lastUpdateIndexProcessedByServer = -1;

	public Player() {
		playerName = "";
		input = new ClientInputState();
	}

	@Override
	public void moveAI(Engine e,float delta){

		if(e.getPlayerID() != this.getPlayerIndex())
			if(e.getPlayerID() != Constants.SERVER)
				return;
		/* TODO
		* The player will set it's dxyz based upon which buttons are pressed.
		* */
		if(input.keyReleased(Input.Keys.W) || input.keyReleased(Input.Keys.S) ) character.setDZ(0);
		if(input.keyReleased(Input.Keys.A) || input.keyReleased(Input.Keys.D) ) character.setDX(0);

		if(input.keyPressed(Input.Keys.W)) character.setDZ(-character.getSpeed());
		else if(input.keyPressed(Input.Keys.S)) character.setDZ(character.getSpeed());
		else character.setDZ(0);

		if(input.keyPressed(Input.Keys.A)) character.setDX(-character.getSpeed());
		else if(input.keyPressed(Input.Keys.D)) character.setDX(character.getSpeed());
		else character.setDX(0);
 	}

	@Override
	public void tick(Engine e, float delta) {
		super.tick(e, delta);
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
		this.input.setClientIndex(playerIndex);
	}

	public void applyInput(ClientInputRequest req){
		input.applyInput(req);
	}

	public long getLastUpdateIndexProcessedByServer() {
		return lastUpdateIndexProcessedByServer;
	}

	public void setLastUpdateIndexProcessedByServer(long lastUpdateIndexProcessedByServer) {
		this.lastUpdateIndexProcessedByServer = lastUpdateIndexProcessedByServer;
	}
}
