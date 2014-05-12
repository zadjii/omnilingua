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
 * Date: 5/2/14
 * Time: 3:58 PM
 */
public class RectangleMeshFactory {
	/**
	 * Returns a mesh in the XY plane, normals (0, 0, 1)
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 * @return
	 */
	public static Mesh billboard(float left, float right, float top, float bottom){
		MeshBuilder meshBuilder = new MeshBuilder();

		meshBuilder
			.pushVert(new VertexAttributes(
				new Vector3(left,top,0),
				new Color(1,1,1,1),
				new Vector2(0,0),
				new Vector3(0, 0, 1)
			))
			.pushVert(new VertexAttributes(
				new Vector3(right,top,0),
				new Color(1,1,1,1),
				new Vector2(1,0),
				new Vector3(0, 0, 1)
			))
			.pushVert(new VertexAttributes(
				new Vector3(left,bottom,0),
				new Color(1,1,1,1),
				new Vector2(0,1),
				new Vector3(0, 0, 1)
			))
			.pushVert(new VertexAttributes(
				new Vector3(right,bottom,0),
				new Color(1,1,1,1),
				new Vector2(1,1),
				new Vector3(0, 0, 1)
			))
		;

		meshBuilder
			.pushIndices(new int[]{0, 1, 2})
			.pushIndices(new int[]{2, 1, 3})
		;

		return meshBuilder.createMesh();
	}

	/**
	 * Returns a mesh in the XZ plane
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 * @return
	 */
	public static Mesh tile(float left, float right, float top, float bottom){
		return tile(left, right, top, bottom, new Color(1, 1, 1, 1));
	}
	public static Mesh tile(float left, float right, float top, float bottom, Color color){
		return tile(left, right, top, bottom, color, 0, 0, 1, 1);
	}
	public static Mesh tile(float left, float right, float top, float bottom, Color color,
	                        float texleft, float texright, float textop, float texbottom){
		MeshBuilder meshBuilder = new MeshBuilder();

		meshBuilder
			.pushVert(new VertexAttributes(
				new Vector3(left,0,top),
				color,
				new Vector2(texleft,textop),
				new Vector3(0, 1, 0)
			))
			.pushVert(new VertexAttributes(
				new Vector3(right,0,top),
				color,
				new Vector2(texright,textop),
				new Vector3(0, 1, 0)
			))
			.pushVert(new VertexAttributes(
				new Vector3(left,0,bottom),
				color,
				new Vector2(texleft,texbottom),
				new Vector3(0, 1, 0)
			))
			.pushVert(new VertexAttributes(
				new Vector3(right,0,bottom),
				color,
				new Vector2(texright,texbottom),
				new Vector3(0, 1, 0)
			))
		;

		meshBuilder
			.pushIndices(new int[]{0, 1, 2})
			.pushIndices(new int[]{2, 1, 3})
		;

		return meshBuilder.createMesh();
	}
}
