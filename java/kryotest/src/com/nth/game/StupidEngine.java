package com.nth.game;

import com.nth.entities.Entity;

import com.nth.requests.WorldStateBroadcast;
import com.nth.run.GG_DisplayClient;
import com.nth.util.ClientInputState;
import com.nth.util.Constants;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 4/30/14
 * Time: 9:35 AM
 */
public class StupidEngine {

	public ArrayList<Entity> entities;
	public int playerID = Constants.UNASSIGNED;

	public StupidEngine() {
		entities = new ArrayList<Entity>();
	}

	public void update(float delta, ClientInputState input, boolean updateOthers){

		if(input.clientIndex == Constants.UNASSIGNED){
			throw new RuntimeException("StupidEngine.update on an UNASSIGNED inputRequest");
		}
		if(input.clientIndex != Constants.SERVER) playerID = input.clientIndex;

		Entity player = getEntityByID(playerID);
		//set the player's dxyz based on the current input
		//update our mouse info from the input
		//update all of the entities.
		if(player != null){
			if(input.keyReleased(Input.KEY_W) ||input.keyReleased(Input.KEY_S) )player.setDY(0);
			if(input.keyReleased(Input.KEY_A) ||input.keyReleased(Input.KEY_D) )player.setDX(0);

			if(input.keyPressed(Input.KEY_W))player.setDY(-player.getSpeed());
			else if(input.keyPressed(Input.KEY_S))player.setDY( player.getSpeed());
			else player.setDY(0);

			if(input.keyPressed(Input.KEY_A))player.setDX(-player.getSpeed());
			else if(input.keyPressed(Input.KEY_D))player.setDX(player.getSpeed());
			else player.setDX(0);


		}
		if(updateOthers)
			for (Entity e : entities){
				e.tick(delta);
			}
		else if(player != null)
			player.tick(delta);

		//LinkedList<Entity> tempE = new LinkedList<Entity>();
		//for (Entity e : entities){
		//	e.tick(delta);
		//  remove the entity
		//}
	}
	public Entity getEntityByID(int id){
		for (Entity e : entities){
			if (e.getID() == id)return e;
		}
		return null;
	}


	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void processServerState(WorldStateBroadcast serverState, List<ClientInputState> pendingInput){
		for(Entity e : serverState.entities){
			if(e.getID() == playerID){
				Entity player = this.getEntityByID(e.getID());

				if (player != null)
					player.setPositionFromOther(e);
				else
					entities.add(new Entity(e.getID()).setPositionFromOther(e));
				//*/
				ArrayList<ClientInputState> tempReqs = new ArrayList<ClientInputState>();
				for(ClientInputState jnput : pendingInput){
					if(jnput.updateIndex <= e.lastInputProcessed )
						tempReqs.add(jnput);
					else
						this.update(GG_DisplayClient.timeIncrement, jnput, false);
				}
				for(ClientInputState jnput : tempReqs) pendingInput.remove(jnput);
				//*/
			}
			else{
				Entity other = this.getEntityByID(e.getID());
				if (other != null){
					other.setPositionFromOther(e);
				}
				else{
					entities.add(new Entity(e.getID()).setPositionFromOther(e));
				}
				//interpolate position
			}
		}
	}
	public WorldStateBroadcast packageEngine(){
		WorldStateBroadcast broadcast = new WorldStateBroadcast();
		//for (Entity e : entities){
		//	broadcast.entities.add(new Entity(e));
		//}
		broadcast.entities = (ArrayList<Entity>)this.entities.clone();
		return broadcast;
	}
}
