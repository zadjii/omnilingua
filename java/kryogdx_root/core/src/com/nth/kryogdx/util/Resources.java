package com.nth.kryogdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/2/14
 * Time: 3:43 PM
 */
public class Resources {

	public static Texture sprite3;
	public static Texture human00;
	public static Texture badlogic;
	public static Texture rockTest000;
	public static Texture rockTestNorms000;
	public static Texture normalsTest002;
	public static Texture normalsTest001;
	public static Texture four_corner_gradient;

	public static void initResources(){
		sprite3 = new Texture(Gdx.files.internal("Sprite-0003.png"));
		human00 = new Texture(Gdx.files.internal("human00.png"));
		badlogic = new Texture(Gdx.files.internal("badlogic.jpg"));
		rockTest000 = new Texture(Gdx.files.internal("rockTest000.png"));
		rockTestNorms000 = new Texture(Gdx.files.internal("rockTestNorms000.png"));
		normalsTest002 = new Texture(Gdx.files.internal("normalsTest002.png"));
		normalsTest001 = new Texture(Gdx.files.internal("normalsTest001.png"));
		four_corner_gradient = new Texture(Gdx.files.internal("four_corner_gradient.png"));
	}
}
