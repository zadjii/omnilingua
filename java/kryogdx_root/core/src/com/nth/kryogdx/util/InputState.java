package com.nth.kryogdx.util;


import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/1/14
 * Time: 9:46 AM
 */
public class InputState {
	public HashMap<Integer, Integer> input;
	public int clientIndex = Constants.UNASSIGNED;
	public long updateIndex;

	public static final int LMB = -1;
	public static final int RMB = -2;
	public static final int MMB = -3;
	public static final int RELEASED= -1;
	public static final int PRESSED = 1;

	public InputState(){
		input = new HashMap<Integer, Integer>();
	}
	public InputState(InputState other){
		this.input = new HashMap<Integer, Integer>(other.input);
		this.clientIndex = other.clientIndex;
	}
	public void pressKey(int key){
		addKeyState(key, PRESSED);
	}
	public void releaseKey(int key){
		addKeyState(key, RELEASED);
	}
	public void addKeyState(int key, int state){
		input.put(key, state);
	}
	public boolean keyPressed(int button){
		return (input.containsKey(button) && input.get(button) == PRESSED);
	}
	public boolean keyReleased(int button){
		return (!input.containsKey(button) || input.get(button) == RELEASED);
	}

}
