package com.nth.kryogdx.entities;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.nth.kryogdx.meshes.RectangleMeshFactory;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.util.Constants;
import com.nth.kryogdx.util.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/2/14
 * Time: 3:51 PM
 */
public class Human extends Character{

	public static final float HUMAN_WIDTH = 2 * Constants.GS;
	public static final float HUMAN_HEIGHT = 2 * Constants.GS;

	public static Mesh HUMAN_BILLBOARD;// =
		//RectangleMeshFactory.billboard(-HUMAN_WIDTH / 2, HUMAN_WIDTH / 2, HUMAN_HEIGHT, 0.0f);


	public Human(){
		super();
		if(HUMAN_BILLBOARD == null)
			HUMAN_BILLBOARD =
			RectangleMeshFactory.billboard(-HUMAN_WIDTH / 2, HUMAN_WIDTH / 2, HUMAN_HEIGHT, 0.0f);

		this.w = HUMAN_WIDTH;
		this.h = HUMAN_HEIGHT;
	}

	@Override
	public void render(Matrix4 modl, Shader shader) {
		//System.out.println("human render");
		shader
			.updateModel(modl.cpy().translate(x, y, z))
			//.useTexture(Resources.human00)
			.useTexture(Resources.sprite3)
			.render(HUMAN_BILLBOARD)
			//.billboard(HUMAN_BILLBOARD)
		;

	}
}
