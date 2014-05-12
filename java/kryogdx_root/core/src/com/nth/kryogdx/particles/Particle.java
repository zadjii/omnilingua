package com.nth.kryogdx.particles;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.nth.kryogdx.meshes.RectangleMeshFactory;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.util.Constants;
import com.nth.kryogdx.util.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/9/14
 * Time: 10:34 AM
 */
public class Particle {
	public float x, y, z;
	public float dx, dy, dz;
	public float ddx, ddy, ddz;
	public float w, h;
	//private Mesh square = RectangleMeshFactory.billboard(0, 0, 1, 1);
	//private static Mesh square = RectangleMeshFactory.billboard(-.5f, .5f, -.5f, .5f);
	private static Mesh square = RectangleMeshFactory.billboard(0f, 1f, 0f, 1f);
	SpriteBatch batch = new SpriteBatch();
	private static float FBO_W = 64.0f*4;
	private static float FBO_H = 64.0f*4;
	static FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, ((int)(FBO_W)), ((int)(FBO_H)), false);

	public Particle(){
		this.w = this.h = Constants.GS;
	}
	public void tick(float delta){
		dx += ddx*delta;
		dy += ddy*delta;
		dz += ddz*delta;
		x += dx*delta;
		y += dy*delta;
		z += dz*delta;
	}
	public void render(Matrix4 modl, Shader shader){
		//translate to this.xyz

		fbo.begin();
		batch.begin();
		//batch.disableBlending();
		//batch.flush();

		//batch.draw(Resources.rockTest000, 8, 8, 64, 32);
		//batch.draw(Resources.rockTest000, 0, 0, fbo.getWidth(), fbo.getHeight());
		//batch.draw(Resources.rockTest000, 0, 0, FBO_W, FBO_H);
		batch.draw(Resources.rockTest000, 0, 0, FBO_W, FBO_H);
		//batch.draw(Resources.human00, 0, 0, w, h);
		//batch.draw(Resources.normalsTest001, fbo.getWidth()/4, fbo.getHeight()/4, fbo.getWidth()/2, fbo.getHeight()/2);
		batch.end();
		fbo.end();


		shader.getShaderProgram().begin();
		modl = modl
			.cpy()
			.translate(this.x, this.y, this.z);
		//modl.scale(w*w, h*h, 1);
		modl.scale(w, h, 1);
		//System.out.println(modl);

		//modl.scale(w, h, 1);
		shader.updateModel(modl);
		shader.setTextureUsage(false);

		//shader.updateModel(modl);
		shader.useTexture(fbo.getColorBufferTexture());
		shader.render(square);
		//shader.billboard(square);

	}
}
