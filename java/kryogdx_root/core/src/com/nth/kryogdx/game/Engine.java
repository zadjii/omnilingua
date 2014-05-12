package com.nth.kryogdx.game;

import com.nth.kryogdx.entities.*;
import com.nth.kryogdx.entities.Character;
import com.nth.kryogdx.personas.Persona;
import com.nth.kryogdx.personas.Player;
import com.nth.kryogdx.requests.WorldStateBroadcast;
import com.nth.kryogdx.tiles.Tiles;
import com.nth.kryogdx.util.ClientInputRequest;
import com.nth.kryogdx.util.ClientInputState;
import com.nth.kryogdx.util.Constants;
import com.nth.kryogdx.util.PersonaMap;

import javax.print.attribute.standard.PrinterLocation;
import java.util.ArrayList;
import java.util.List;

import static com.nth.kryogdx.util.Constants.GS;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/10/14
 * Time: 10:53 AM
 */
public class Engine {
	private int playerID = Constants.UNASSIGNED;
	private PersonaMap personas;
	private Player player;
	private long lastInputProcessed;
	private static int nextPlayerID = 1;

	private int mapSize = 64;
	private short[][] map;
	private boolean[] mapPieces;

	private boolean mapAssembled = false;

	//only creates the engine. not really viable for running a game from.
	public Engine() {
		personas = new PersonaMap();
		map = null;//new int[mapSize][mapSize];
		player = null;
		map = new short[mapSize][mapSize];
		mapPieces = new boolean[mapSize];
	}
	public void initializeEngine(){
		mapAssembled = true;
		mapPieces = null;
		for (int x = 0; x < map.length; x++) {
			for (int z = 0; z < map.length; z++) {
				map[x][z] = (short)(Math.random() * (Tiles.tileColors.length-1) + 1);
			}
		}
	}

	public void update(float delta, boolean updateEntireWorld){
		//shortcut to update
		if(player == null &&
			(playerID != Constants.SERVER && playerID != Constants.UNASSIGNED)){
			this.player = getPlayerByIndex(playerID);
		}
		//set the player's dxyz based on the current input
		//update our mouse info from the input
		//update all of the entities.

		personas.reseed();
		if(updateEntireWorld){
			for(Persona p : personas.getPersonas()){
				p.moveAI(this, delta);
			}
			//System.out.print("players:");
			for(Persona p : personas.getPersonas()){
				p.tick(this, delta);
				//if(p instanceof Player && ((Player) p).getPlayerIndex()==1){
				//	if(numPlayers != 0)System.out.print(
				//		"("
				//		+ ((Player) p).getPlayerIndex() + ":"
				//		+ p.getCharacter().getXZPos() + "),"
				//	);
				//}
			}
			//if(numPlayers != 0)System.out.println("/players");
		}
		else if (player != null){
			player.moveAI(this, delta);
			player.tick(this, delta);
		}

		//if(updateOthers)
		//	for (Entity e : entities){
		//		e.tick(delta);
		//	}
		//else if(player != null)
		//	player.tick(delta);

		//LinkedList<Entity> tempE = new LinkedList<Entity>();
		//for (Entity e : entities){
		//	e.tick(delta);
		//  remove the entity
		//}
	}


	public void setPlayer(int playerID) {
		if(playerID == Constants.SERVER){
			this.playerID = playerID;
			this.player = null;
			return;
		}
		// I think this is how java's lambda's work.
		// No clue.
		// I just kept auto completing on whatever looked good.
		//Persona possiblePlayer = personas.getPersonas()
		//	.stream()
		//	.filter((persona)->{return persona.getCharacter().getID() == playerID;})
		//	.findFirst().get();
		//if( possiblePlayer != null) {/*do things*/}
		this.playerID = playerID;
		this.player = findPlayer(playerID, this.getPersonas().getPersonas());
	}

	public int getPlayerID() {
		return playerID;
	}

	public PersonaMap getPersonas() {
		return personas;
	}

	public short[][] getMap() {
		return map;
	}

	public static int getNextPlayerID() {
		return nextPlayerID++;
	}

	public boolean hasPlayer(String playerName){
		//return personas.getPersonas().stream()
		//.anyMatch( (p)->
		//	{ return ((Player)p).getPlayerName().equals(playerName);}
		//);
		for (Persona p : personas.getPersonas()){
			if(p instanceof Player
				&& ((Player) p).getPlayerName().equals(playerName))
				return true;
		}
		return false;
	}

	public Player getPlayerByIndex(int index){
		for (Persona p : personas.getPersonas()){
			if(p instanceof Player
				&& ((Player) p).getPlayerIndex() == index)
				return ((Player) p);
		}
		return null;
		//throw new RuntimeException(
		//	"I didn't find the player with an index=\'" + index+ "\'"
		//);
	}

	public int getPlayerIndex(String playerName){
		for (Persona p : personas.getPersonas()){
			if(p instanceof Player
				&& ((Player) p).getPlayerName().equals(playerName))
				return ((Player) p).getPlayerIndex();
		}
		throw new RuntimeException(
			"I didn't find the player with a name \"" + playerName + "\""
		);
	}
	public int createPlayer(String playerName){
		int index = getNextPlayerID();
		Player newb = new Player();
		Character newbPC = new Human();
		newbPC.setXYZ(0, 0, 0);
		newb.setCharacter(newbPC);
		newb.setPlayerIndex(index);
		newb.setPlayerName(playerName);
		personas.add(newb);
		return index;
	}
	public void applyInputRequest(ClientInputRequest req){
		Player guy = getPlayerByIndex(req.getPlayerIndex());
		if(guy == null) {
			System.err.println(
				"Engine does not have a client with index=\'"
					+ req.getPlayerIndex()
					+ "\'" );
			return;
		}
		guy.applyInput(req);
		guy.setLastUpdateIndexProcessedByServer(req.getUpdateIndex());

	}
	private static Player findPlayer(int playerID, ArrayList<Persona> personas){
		for(Persona p : personas){
			if(p instanceof Player && ((Player) p).getPlayerIndex()==playerID){
				return (Player)p;
			}
		}
		return null;
	}
	private static Persona getPersona(int personaID, ArrayList<Persona> personas){
		for(Persona p : personas){
			if(p.getID() == personaID){
				return p;
			}
		}
		return null;
	}
	public void processServerState(
			WorldStateBroadcast serverState,
			List<ClientInputRequest> pendingInput ) {
		long lastServerInputProcessed = -1;
		if(this.playerID != Constants.SERVER
			&& this.playerID != Constants.UNASSIGNED
			&& this.player == null){
			this.player = findPlayer(playerID, serverState.getPersonas());
		}
		for(Persona p : serverState.getPersonas()){
			if(! this.getPersonas().getPersonas().contains(p)){
				//need to add the persona to us
				this.getPersonas().add(p);
			}
			else{
				//Persona already added to the engine
				Persona persona = getPersona(p.getID(), this.personas.getPersonas());
				if(p instanceof Player && ((Player) p).equals(player))
					lastServerInputProcessed = ((Player) p).getLastUpdateIndexProcessedByServer();
				persona.getCharacter().setPositionFromOther(p.getCharacter());
					//^^^ this line also updates the player's position
			}
		}
		ArrayList<ClientInputRequest> tempReqs = new ArrayList<ClientInputRequest>();

		for(ClientInputRequest req : pendingInput){
			if(req.getUpdateIndex() <= lastServerInputProcessed)
				tempReqs.add(req);
			else{
				player.applyInput(req);
				this.update(KryoGDX.timeIncrement, true);
				/*this way, both the player get's their input updated, and the
				other persona's get interpolated.*/
			}
		}
		for(ClientInputRequest req : tempReqs) pendingInput.remove(req);
	}
	public WorldStateBroadcast packageWorldStateBroadcast(){
		//WorldStateBroadcast broadcast = new WorldStateBroadcast(this);
		WorldStateBroadcast broad = new WorldStateBroadcast();
		broad.setEntitiesFromPersonaMap(this.getPersonas());
		return broad;
	}

	//public void setMap(short[][] map) {
	//	this.map = map;
	//}
	public void setMapPiece(int x, short[] zmap) {
		if(mapAssembled)throw new RuntimeException("The map should already be assembed...");
		System.arraycopy(zmap, 0, map[x], 0, zmap.length);
		mapPieces[x] = true;

		for (int i = 0; i < mapPieces.length; i++) {
			if( ! mapPieces[i])return;
		}
		mapAssembled = true;
		mapPieces = null;
	}

	public boolean isMapAssembled() {
		return mapAssembled;
	}

	public Player getPlayer() {
		return player;
	}
}
