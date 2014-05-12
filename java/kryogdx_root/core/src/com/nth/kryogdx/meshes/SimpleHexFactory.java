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
public class SimpleHexFactory {

	/**
	 * Makes a simple pointy-top hex.
	 * If you want flat top, just rotate the damned thing.
	 * @param size Radius of the hex
	 * @param color The color of the hex
	 * @return the mesh for the hex
	 */
	public static Mesh simpleHex(float size, Color color){
		MeshBuilder meshBuilder = new MeshBuilder();

		float centerX = 0, centerY = 0;
		for (int i = 0; i < 6; i++) {
			float angle = (float)( (2 * Math.PI / 6) * (i + .5) );
			float x = (float)(centerX + (size * Math.cos(angle)));
			float y = (float)(centerY + (size * Math.sin(angle)));

			meshBuilder.pushVert(new VertexAttributes(
					new Vector3(x, y, 0),
					color == null ? new Color(1, 1, 1, 1) : color,
					new Vector2((float)Math.cos(angle)*2.0f + 1.0f, (float)Math.sin(angle)*2.0f + 1.0f),
					new Vector3(0, 1, 0)
			));

		}
		meshBuilder
				.pushIndices(new int[]{0, 1, 2})
				.pushIndices(new int[]{2, 3, 0})
				.pushIndices(new int[]{3, 5, 0})
				.pushIndices(new int[]{3, 4, 5})
				;


		return meshBuilder.createMesh();
	}
}
