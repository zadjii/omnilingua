package com.nth.kryogdx.meshes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nth.kryogdx.util.MeshBuilder;
import com.nth.kryogdx.util.VertexAttributes;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 4/6/14
 * Time: 8:00 PM
 */
public class SimpleAxisFactory {

	/**
	 * Makes a simple pointy-top hex.
	 * If you want flat top, just rotate the damned thing.
	 * @param size Radius of the hex
	 * @return
	 */
	public static Mesh simpleAxis(float size){
		MeshBuilder meshBuilder = new MeshBuilder();

		meshBuilder
				.pushVert(new VertexAttributes(new Vector3(0,0,0),new Color(1,0,0,1)))
				.pushVert(new VertexAttributes(new Vector3(size,0,0),new Color(1,0,0,1)))
				.pushVert(new VertexAttributes(new Vector3(0,0,0),new Color(0,1,0,1)))
				.pushVert(new VertexAttributes(new Vector3(0,size,0),new Color(0,1,0,1)))
				.pushVert(new VertexAttributes(new Vector3(0,0,0),new Color(0,0,1,1)))
				.pushVert(new VertexAttributes(new Vector3(0,0,size),new Color(0,0,1,1)))
				;


		meshBuilder
				.pushIndices(new int[]{0, 1, 0})
				.pushIndices(new int[]{2, 3, 2})
				.pushIndices(new int[]{4, 5, 4})
				;


		return meshBuilder.createMesh();
	}
}
