package com.nth.game;

import com.nth.entities.Entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 12/6/13
 * Time: 7:25 PM
 */
public class Player implements Serializable{
	private int id;
	private boolean wPressed, aPressed, sPressed, dPressed;
	private Entity entity;

	private static final float SPEED = 32.0f;

	public Player(){
		this.entity = new Entity();
		entity.setWidth(128);
		entity.setHeight(128);
	}

	public void moveAI(float delta){
		entity.setDY(0);
		entity.setDX(0);
//		System.out.println(
//				wPressed + ", "+
//				aPressed + ", "+
//				sPressed + ", "+
//				dPressed + ", "
//		);
		if (wPressed) {
			entity.setDY(-SPEED);
		}
		if (sPressed) {
			entity.setDY(SPEED);
		}
		if (aPressed) {
			entity.setDX(-SPEED);
		}
		if (dPressed) {
			entity.setDX(SPEED);
		}
	}

	public Entity getEntity(){return entity;}

	public int getId() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
		if(this.entity != null)this.entity.setID(id);
	}

	public boolean iswPressed() {
		return wPressed;
	}

	public void setwPressed(boolean wPressed) {
		this.wPressed = wPressed;
	}

	public boolean isaPressed() {
		return aPressed;
	}

	public void setaPressed(boolean aPressed) {
		this.aPressed = aPressed;
	}

	public boolean issPressed() {
		return sPressed;
	}

	public void setsPressed(boolean sPressed) {
		this.sPressed = sPressed;
	}

	public boolean isdPressed() {
		return dPressed;
	}

	public void setdPressed(boolean dPressed) {
		this.dPressed = dPressed;
	}



}
