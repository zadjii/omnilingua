package com.nth.kryogdx.personas;

import com.nth.kryogdx.entities.Character;
import com.nth.kryogdx.game.Engine;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/10/14
 * Time: 11:25 AM
 */
public class Persona implements Serializable {
	protected Character character;
	private static int nextPID = 1;
	private int id;

	public Persona() {
		this.id = nextPID++;
	}

	public void moveAI(Engine e,float delta){}
	public void tick(Engine e,float delta){
		if (this.getCharacter() != null)this.getCharacter().tick(e,delta);
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public int getID() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Persona persona = (Persona) o;

		if (id != persona.id) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
