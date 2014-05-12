package com.nth.kryogdx.requests;

import java.io.Serializable;

/**
 * should work for any array w/ length< 4K/sizeof(short)
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/11/14
 * Time: 2:59 PM
 */
public class MapPieceMessage implements Serializable {
	private int x;
	private short[] zmap;
	public MapPieceMessage(){}

	public short[] getMapPiece() {
		return zmap;
	}

	public int getX() {
		return x;
	}

	public void setMap(int x, short[] zmap) {
		this.x = x;
		this.zmap = zmap;
	}
}
