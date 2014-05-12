package com.nth.kryogdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.nth.kryogdx.entities.Entity;
import com.nth.kryogdx.game.states.GameState;
import com.nth.kryogdx.meshes.SimpleAxisFactory;
import com.nth.kryogdx.particles.Particle;
import com.nth.kryogdx.personas.Persona;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.tiles.Tiles;

import static com.nth.kryogdx.util.Constants.GS;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/11/14
 * Time: 3:01 PM
 */
public class GameRenderer {

	private static Mesh axis = SimpleAxisFactory.simpleAxis(1.0f * GS);
	private static Mesh grid;
	private static Mesh testBuiltMesh;

	public static void render(Shader defaultShader, GameState s) {

		Camera camera = s.getCamera();
		//oh god this again. see how bad this *could* be?
		Engine engine = KryoGDX.instance.engine;

		// update camera
//		camera.apply(Gdx.graphics.getGL10());
		KryoGDX.standardRenderSetup(camera);

//        screenCamera.apply(Gdx.graphics.getGL10());
//        screenCamera.update()

		Shader currShader = defaultShader;
		ShaderProgram currSP = defaultShader.getShaderProgram();

		Matrix4 modl;
		Matrix4 view;
		Matrix4 proj;

		modl = new Matrix4();
		view = new Matrix4();//camera.view;
		proj = new Matrix4();//camera.projection;

		view = camera.view;
		proj = camera.projection;

		currSP.begin();
		currShader
			.setProjection(proj)
			.setView(view)
		;

		currShader.updateMVP(modl, view, proj);

		axis.render(currSP, GL20.GL_LINE_STRIP);
		//testBuiltMesh.render(currSP, GL20.GL_TRIANGLES);
		//currShader.setTextureUsage(false);

		modl = new Matrix4();
		for (int x = 0; x < engine.getMap().length; x++) {
			for (int z = 0; z < engine.getMap().length; z++) {
				Tiles.renderTile(engine.getMap()[x][z], x*GS, z*GS, modl, currShader);
			}
		}
		modl = new Matrix4();
		for (Persona p : engine.getPersonas().getPersonas()){
			p.getCharacter().render(modl, currShader);
		}
		//TODO: this vvv
		//for (Particle p : particles){
		//	p.render(modl, currShader);
		//}

		currShader.setTextureUsage(false);

		//FFS I can really? ahg damn.
		/*
		ShapeRenderer renderer = new ShapeRenderer();
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(new Color(0, 1, 1, 1));
		renderer.rect(32, 64, 128, 128);
		renderer.end();
		*/
	}
}
