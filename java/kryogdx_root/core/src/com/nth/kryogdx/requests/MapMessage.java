package com.nth.kryogdx.requests;

import java.io.Serializable;

/**
 *
 * DOESN'T WORK LOL
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/11/14
 * Time: 2:59 PM
 */
public class MapMessage implements Serializable {
	private short[][] map;
	public MapMessage(){}

	public short[][] getMap() {
		return map;
	}

	public void setMap(short[][] map) {
		this.map = map;
	}
}
