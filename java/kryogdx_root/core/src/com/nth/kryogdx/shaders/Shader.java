package com.nth.kryogdx.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 4/6/14
 * Time: 2:05 PM
 */
public class Shader {

	private static boolean PEDANTIC = false;

	private ShaderProgram shaderProgram;
	private int modl_handle;
	private int view_handle;
	private int proj_handle;

	private int mv_handle;
	private int mvp_handle;
	private int norm_handle;

	private int usingTexture_handle;


	private Matrix4 last_modl;
	private Matrix4 last_view;
	private Matrix4 last_proj;

	public Shader(String vertexShaderPath, String fragShaderPath){
		this.shaderProgram = new ShaderProgram(
				new FileHandle(vertexShaderPath),
				new FileHandle(fragShaderPath)
		);

		// check there's no shader compile errors
		if (!shaderProgram.isCompiled())
			throw new IllegalStateException(shaderProgram.getLog());

		this.modl_handle = shaderProgram.fetchUniformLocation("modl", PEDANTIC);
		this.view_handle = shaderProgram.fetchUniformLocation("view", PEDANTIC);
		this.proj_handle = shaderProgram.fetchUniformLocation("proj", PEDANTIC);

		this.mv_handle = shaderProgram.fetchUniformLocation("mv", PEDANTIC);
		this.mvp_handle = shaderProgram.fetchUniformLocation("mvp", PEDANTIC);
		this.norm_handle = shaderProgram.fetchUniformLocation("norm", PEDANTIC);

		this.usingTexture_handle = shaderProgram.fetchUniformLocation("usingTexture", PEDANTIC);

	}

	public Shader updateMVP(
			Matrix4 modl,
			Matrix4 view,
			Matrix4 proj
	){
		shaderProgram.begin();
		shaderProgram.setUniformMatrix(modl_handle, modl);
		shaderProgram.setUniformMatrix(view_handle, view);
		shaderProgram.setUniformMatrix(proj_handle, proj);

		last_modl = modl.cpy();
		last_view = view.cpy();
		last_proj = proj.cpy();

		shaderProgram.setUniformMatrix(mv_handle, view.cpy().mul(modl));
		shaderProgram.setUniformMatrix(mvp_handle, proj.cpy().mul(view.cpy().mul(modl)));
		shaderProgram.setUniformMatrix(norm_handle, modl.cpy().toNormalMatrix());

		return this;
	}

	public Shader setProjection(Matrix4 proj){
		shaderProgram.begin();
		shaderProgram.setUniformMatrix(proj_handle, proj);
		last_proj = proj.cpy();
		return this;
	}

	public Shader setView(Matrix4 view){
		shaderProgram.begin();
		shaderProgram.setUniformMatrix(view_handle, view);
		last_view = view.cpy();
		return this;
	}

	public Shader updateMV(
			Matrix4 modl,
			Matrix4 view
	){
		shaderProgram.begin();
		shaderProgram.setUniformMatrix(modl_handle, modl);
		shaderProgram.setUniformMatrix(view_handle, view);

		last_modl = modl.cpy();
		last_view = view.cpy();

		shaderProgram.setUniformMatrix(mv_handle, view.cpy().mul(modl));
		shaderProgram.setUniformMatrix(mvp_handle, last_proj.cpy().mul(view.cpy().mul(modl)));
		shaderProgram.setUniformMatrix(norm_handle, modl.cpy().toNormalMatrix());

		return this;
	}

	public Shader updateModel(Matrix4 modl){
		shaderProgram.begin();
		shaderProgram.setUniformMatrix(modl_handle, modl);
		last_modl = modl.cpy();

		shaderProgram.setUniformMatrix(mv_handle, last_view.cpy().mul(modl));
		shaderProgram.setUniformMatrix(mvp_handle, last_proj.cpy().mul(last_view.cpy().mul(modl)));
		shaderProgram.setUniformMatrix(norm_handle, modl.cpy().toNormalMatrix());
		return this;
	}
	public Shader setTextureUsage(boolean on){
		shaderProgram.setUniformi(this.usingTexture_handle, on ? 1 : 0);
		return this;
	}
	public Shader useTexture(int textureHandle){
		this.setTextureUsage(true);
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
		Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, textureHandle);
		return this;
	}
	public Shader useSecondTexture(int textureHandle){
		this.setTextureUsage(true);
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
		Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, textureHandle);
		return this;
	}
	public Shader useTexture(Texture texture){
		return useTexture(texture.getTextureObjectHandle());
	}
	public Shader useSecondTexture(Texture texture){
		return useSecondTexture(texture.getTextureObjectHandle());
	}

	/**
	 * Draws the mesh directly facing the screen,removing rotation.
	 * modl and view should already have been set by this point.
	 *
	 * @param mesh the mesh to draw billboarded.
	 * @return this for chaining
	 */
	public Shader billboard(Mesh mesh){
		Matrix4 mv = last_view.cpy().mul(last_modl);

		float[] mvv = mv.getValues();
		//System.out.print("mvv0[");
		//for (int i = 0; i < mvv.length; i++) {
		//	System.out.print(mvv[i] + ",");
		//}
		//System.out.println("]");
		mv = new Matrix4(new float[]{
			1.0f,		0.0f,		0.0f,	mvv[Matrix4.M30],
			0.0f,		1.0f,		0.0f,	mvv[Matrix4.M31],
			0.0f,		0.0f,		1.0f,	mvv[Matrix4.M32],
			mvv[Matrix4.M03],	mvv[Matrix4.M13],	mvv[Matrix4.M23], mvv[Matrix4.M33]
		});
		mvv = mv.getValues();
		//System.out.print("mvv1[");
		//for (int i = 0; i < mvv.length; i++) {
		//	System.out.print(mvv[i] + ",");
		//}
		//System.out.println("]");

		//mv.set(new Matrix3());
		//shaderProgram.setUniformMatrix(modl_handle, modl);
		//last_modl = modl.cpy();

		shaderProgram.setUniformMatrix(mv_handle, mv);
		shaderProgram.setUniformMatrix(mvp_handle, last_proj.cpy().mul(mv));
		//shaderProgram.setUniformMatrix(norm_handle, last_modl.cpy().toNormalMatrix());

		mesh.render(this.getShaderProgram(), GL20.GL_TRIANGLES);
		return this;
	}
	public Shader render(Mesh mesh){
		mesh.render(this.getShaderProgram(), GL20.GL_TRIANGLES);
		return this;
	}
	public Shader render(Mesh mesh, int primitive){
		mesh.render(this.getShaderProgram(), primitive);
		return this;
	}
	public ShaderProgram getShaderProgram(){return this.shaderProgram;}

}
