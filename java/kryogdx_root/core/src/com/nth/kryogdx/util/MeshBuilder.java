package com.nth.kryogdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.nth.kryogdx.util.VertexAttributes;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 3/30/14
 * Time: 4:06 PM
 */
public class MeshBuilder {
	public LinkedList<VertexAttributes> vertices;
	public LinkedList<Short> indices;

	public MeshBuilder() {
		this.vertices = new LinkedList<VertexAttributes>();
		this.indices = new LinkedList<Short>();
	}
	public Mesh createMesh(){

		int numVertices = this.vertices.size();
		int numIndices = this.indices.size();

		float[] vertices = new float[numVertices * VertexAttributes.sizeOf()];
		short[] indices = new short[numIndices];

		//System.out.println("Should be 4, 6: " + numVertices + ", " + numIndices);
		Mesh mesh;
		try {//FIXME There is a better way of handling this. Definitely.
			mesh = new Mesh(
				true, numVertices, numIndices,  // static mesh with vertices and indices
				VertexAttribute.Position(),
				VertexAttribute.ColorUnpacked(),
				VertexAttribute.TexCoords(0),
				VertexAttribute.Normal()
			);
		}catch (UnsatisfiedLinkError ex){return null;}
		int index = 0;
		for(VertexAttributes vert : this.vertices){
			index = VertexAttributes.put(vertices, index, vert);
		}

		//System.out.print("vertices = [");
		//for (int i = 0; i < vertices.length; i++) {
		//	if (i % VertexAttributes.sizeOf() == 0) System.out.print("\n\t");
		//	System.out.print("\t" + vertices[i]);
		//	if (i+1 < vertices.length) System.out.print(",");
		//
		//} System.out.println("\n]");


		index = 0;
		for(Short s : this.indices){
			indices[index] = s; index++;
		}

		//System.out.print("indices = [");
		//for (int i = 0; i < indices.length; i++) {
		//	if (i % 3 == 0) System.out.print("\n\t");
		//	System.out.print(indices[i]);
		//	if (i+1 < indices.length) System.out.print(",");
		//} System.out.println("\n]");

		mesh
				.setVertices(vertices)
				.setIndices(indices);

		//int max = mesh.getNumVertices() * mesh.getVertexSize() / 4;
		//System.out.println("mesh.getNumVertices() = " + mesh.getNumVertices());
		//System.out.println("mesh.getVertexSize() = " + mesh.getVertexSize());
		//System.out.println("numVertices * VertexAttributes.sizeOf() = " + (numVertices * VertexAttributes.sizeOf()));
		//
		//System.out.println("max: " + max);
		//
		//float[] buffer = new float[numVertices * VertexAttributes.sizeOf()];
		//mesh.getVertices(buffer);
		//
		//System.out.println("Vertices: " + buffer);


		return mesh;
	}
	public MeshBuilder pushVert(VertexAttributes vertexAttribute){
		vertices.addLast(vertexAttribute);
		return this;
	}
	public MeshBuilder pushIndex(int index){
		indices.addLast((short) index);
		return this;
	}
	public MeshBuilder pushIndices(int[] indices){
		for(int i : indices){
			this.indices.addLast((short) i);
		}
		return this;
	}

}
