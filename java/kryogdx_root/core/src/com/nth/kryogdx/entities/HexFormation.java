package com.nth.kryogdx.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.nth.kryogdx.meshes.SimpleHexFactory;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.util.Resources;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/1/14
 * Time: 10:40 PM
 */
public class HexFormation extends Entity{


	private ArrayList<Mesh> hexes = new ArrayList<Mesh>();
	private ArrayList<Vector3> hexPoses = new ArrayList<Vector3>();

	public HexFormation(){
		this(50);
	}
	public HexFormation(int numHexes){
		for (int i = 0; i < numHexes; i++) {
			hexes.add(SimpleHexFactory.simpleHex((float) (Math.random() * .25 + 0.25f),
				new Color(
					(float) (Math.random() * .5 + .5f),
					(float) (Math.random() * .5 + .5f),
					(float) (Math.random() * .5 + .5f),
					1.0f)
			));
			hexPoses.add(new Vector3(
				(float)(Math.random() * 5.0f - 2.5f),
				(float)(Math.random() * 5.0f - 2.5f),
				(float)(Math.random() * 5.0f - 2.5f)
			));
		}
	}
	public void tick(float delta){
		super.tick(delta);

	}
	public void render(Matrix4 modl, Shader shader){
		modl = modl.cpy().translate(this.x, this.y, this.z);
		shader.updateModel(modl);
		shader.setTextureUsage(false);
		for(int i = 0; i < hexes.size(); i++){
			shader.updateModel(modl.cpy().translate(hexPoses.get(i)));
			modl.rotate(new Vector3(1, 1, 1), 5);
			//hexes.get(i).render(currSP, GL20.GL_TRIANGLES);
			//shader.billboard(hexes.get(i));
			shader.render(hexes.get(i));
			if(i > hexes.size()/2){
				shader.useTexture(Resources.four_corner_gradient);
			}
			//else
		}
		shader.setTextureUsage(false);
	}
}
