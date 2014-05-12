package com.nth.kryogdx.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nth.kryogdx.meshes.RectangleMeshFactory;
import com.nth.kryogdx.shaders.Shader;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/7/14
 * Time: 9:37 PM
 */
public class GUIPanel {

	public static final int TOP_LEFT 		= 0;
	public static final int TOP_RIGHT 		= 1;
	public static final int BOTTOM_LEFT 	= 2;
	public static final int BOTTOM_RIGHT 	= 3;
	public static final int CENTER 		    = 4;
	public static final Mesh defaultPanel = RectangleMeshFactory.billboard(0,1,0,1);

	private int anchorType;
	private Vector2 offset;
	private Vector2 size;
	private Color color;

	public GUIPanel(int anchor, Vector2 offset, Vector2 size){
		this(anchor, offset, size, null);
	}
	public GUIPanel(int anchor, Vector2 offset, Vector2 size, Color color){
		this.anchorType = anchor;
		this.offset = offset;
		this.size = size;
		this.color = color;
	}



	private static Vector2 getAnchor(int anchorType, Vector2 windowSize){
		switch(anchorType){
			case TOP_LEFT:
				return new Vector2(0,0);
			case TOP_RIGHT:
				return new Vector2(windowSize.x,0);
			case BOTTOM_LEFT:
				return new Vector2(0,windowSize.y);
			case BOTTOM_RIGHT:
				return new Vector2(windowSize.x,windowSize.y);
			case CENTER:
				return new Vector2(windowSize.x/2,windowSize.y/2);
		}
		return new Vector2(0,0);
	}

	public static Matrix4 getGUIOriginMatrix(){
		Matrix4 modl = new Matrix4();
		modl.translate(new Vector3(-1, 1, 0))
			.rotate(new Vector3(1.0f, 0.0f, 0.0f),180.0f);
		return modl;
	}

	/*
	 * typically m = mat4(1), unless you're getting balls-y with nested panels.
	 * I'd think that windowSize is typically KryoGDX.viewport.
	 */
	public void draw(Matrix4 modl, Shader shader, Vector2 windowSize){
		this.draw(modl, shader, windowSize, null);
	}
	public void draw(Matrix4 modl, Shader shader, Vector2 windowSize, Color color){
		Vector2 window = windowSize.cpy().scl(1.0f / 2.0f);
		Vector2 pos = (
			(GUIPanel.getAnchor(this.anchorType, windowSize))
				.add(this.offset)
		);
		pos.x = pos.x / window.x;
		pos.y = pos.y / window.y;

		Vector2 size = new Vector2(
			(this.size.x) / (window.x),
			(this.size.y) / (window.y)
		);
		modl.translate(pos.x, pos.y, 0);
		modl.scale(size.x, size.y, 1.0f);

		//if(color != null)
		//  shader.colorUpdate(color);
		//else if(this.color != null)
		//  shader.colorUpdate(this.color);
		//else shader.colorUpdate(null);

		shader.updateMVP(modl, new Matrix4(), new Matrix4());
		//shader.render(defaultPanel);
		shader.billboard(defaultPanel);
	}

}
