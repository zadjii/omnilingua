package com.nth.kryogdx.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 3/30/14
 * Time: 4:06 PM
 */
public class VertexAttributes {
	public Vector3 pos;
	public Color color;
	public Vector2 texCoords;
	public Vector3 norm;

	public VertexAttributes(Vector3 pos, Color color, Vector2 texCoords, Vector3 norm) {
		this.pos = pos;
		this.color = color;
		this.texCoords = texCoords;
		this.norm = norm;
	}

	public VertexAttributes(Vector3 pos, Vector2 texCoords) {
		this.pos = pos;
		this.color = new Color(0.0f, 0.0f, 0.0f, 1.0f);
		this.texCoords = texCoords;
		this.norm = new Vector3(1.0f, 0.0f, 0.0f);
	}

	public VertexAttributes(Vector3 pos, Color color) {
		this.pos = pos;
		this.color = color;
		this.texCoords =  new Vector2(0.0f, 0.0f);
		this.norm = new Vector3(1.0f, 0.0f, 0.0f);
	}
	//make sure this matches the size of all of the elements.
	public static int sizeOf(){
		return (3 + 4 + 2 + 3);
	}
	public static int put(float[] vertices, int index, VertexAttributes vert){

		vertices[index] = vert.pos.x; index++;
		vertices[index] = vert.pos.y; index++;
		vertices[index] = vert.pos.z; index++;

		vertices[index] = vert.color.r; index++;
		vertices[index] = vert.color.g; index++;
		vertices[index] = vert.color.b; index++;
		vertices[index] = vert.color.a; index++;

		vertices[index] = vert.texCoords.x; index++;
		vertices[index] = vert.texCoords.y; index++;

		vertices[index] = vert.norm.x; index++;
		vertices[index] = vert.norm.y; index++;
		vertices[index] = vert.norm.z; index++;

		return index;
	}
}
