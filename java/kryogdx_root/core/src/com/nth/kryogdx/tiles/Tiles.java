package com.nth.kryogdx.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.nth.kryogdx.meshes.RectangleMeshFactory;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/11/14
 * Time: 5:19 PM
 */
public class Tiles {

	public static final short NONE          = 0;
	public static final short GRASS00       = 1;
	public static final short GRASS01       = 2;
	public static final short GRASS02       = 3;
	public static final short GRASS03       = 4;

	//public static Mesh basicTile;
	public static Mesh[] tileMeshes;

	public static final Color[] tileColors = new Color[]{
		new Color(  0.0f,  0.0f, 0.0f, 1.0f),               //NONE          = 0
		new Color(  0.3f,  0.8f, 0.4f, 1.0f),               //GRASS00       = 1
		new Color(  0.5f,  0.8f, 0.4f, 1.0f),               //GRASS01       = 2
		new Color(  0.3f,  0.5f, 0.4f, 1.0f),               //GRASS02       = 3
		new Color(  0.3f,  0.8f, 0.6f, 1.0f),               //GRASS03       = 4
	};
	//Use this line to test out colors in the sidebar <<<<<<<<<<<<<<<<
	//java.awt.Color foo = new java.awt.Color(0.3f,  0.8f, 0.6f, 1.0f);

	public static void initTiles(){
		tileMeshes = new Mesh[tileColors.length];
		for (int i = 0; i < tileColors.length; i++) {
			tileMeshes[i] = RectangleMeshFactory.tile(0, Constants.GS, 0, Constants.GS, tileColors[i]);
		}
	}


	public static void renderTile(short type, float x, float z, Matrix4 modl, Shader currShader){
		renderTile(type, currShader.updateModel(modl.cpy().translate(x, 0, z)));
	}
	public static void renderTile(short type, Shader currShader){
		currShader.render(tileMeshes[type]);
	}

}
