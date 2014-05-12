package com.nth.kryogdx.requests;

import com.nth.kryogdx.entities.Entity;
import com.nth.kryogdx.game.Engine;
import com.nth.kryogdx.personas.Persona;
import com.nth.kryogdx.util.PersonaMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 4/30/14
 * Time: 12:31 PM
 */
public class WorldStateBroadcast implements Serializable{

	//public int lastUpdateIndex;
	//public ArrayList<Entity> entities = new ArrayList<Entity>();

	//private PersonaMap personas;
	private ArrayList<Persona> personas;

	public WorldStateBroadcast(){

	}
	public void setEntitiesFromPersonaMap(PersonaMap personaMap){
		//for(Persona p : personaMap.getPersonas()){
		//	entities.add(p.getCharacter());
		//}
		//this.personas = personaMap;
		this.personas = personaMap.getPersonas();
	}

	public ArrayList<Persona> getPersonas() {
		return personas;
	}
	//public PersonaMap getPersonas() {
	//	return personas;
	//}
}

