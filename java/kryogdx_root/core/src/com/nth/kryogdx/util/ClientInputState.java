package com.nth.kryogdx.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/1/14
 * Time: 9:46 AM
 */
public class ClientInputState {
	private HashMap<Integer, Integer> input;
	//public SparseIntArray array = new SparseIntArray();
	private int clientIndex = Constants.UNASSIGNED;
	private long lastUpdateIndex;

	public static final int LMB = -1;
	public static final int RMB = -2;
	public static final int MMB = -3;
	public static final int RELEASED= -1;
	public static final int PRESSED = 1;

	public ClientInputState(){
		//This keeps suggesting I use "SparseIntArray", but I see no such class
		//probably one of those 1.7/1.8 changes
		input = new HashMap<Integer, Integer>();
	}
	public boolean keyPressed(int button){
		return (input.containsKey(button) && input.get(button) == PRESSED);
	}
	public boolean keyReleased(int button){
		return (!input.containsKey(button) || input.get(button) == RELEASED);
	}
	public void applyInput(ClientInputRequest inputUpdate){
		//if(inputUpdate.updateIndex < lastUpdateIndex){
		//	throw new RuntimeException(
		//		"I think this input should have been processed already; ci, lui, iu.ui:"
		//		+ clientIndex + ", "
		//		+ lastUpdateIndex + ", "
		//		+ inputUpdate.updateIndex + ", "
		//	);
		//}
		if(inputUpdate.playerIndex != this.clientIndex){
			throw new RuntimeException(
				"The playerIndex and clientIndex don't correspond; ci,iu.pi, lui, iu.ui:"
					+ clientIndex + ", "
					+ inputUpdate.playerIndex + ", "
					+ lastUpdateIndex + ", "
					+ inputUpdate.updateIndex + ", "
			);
		}
		for(Map.Entry<Integer, Integer> entry :  inputUpdate.inputUpdates.entrySet()){
			input.put(entry.getKey(), entry.getValue());
		}
		this.lastUpdateIndex = inputUpdate.updateIndex;
	}

	public void setClientIndex(int clientIndex) {
		this.clientIndex = clientIndex;
	}
}
