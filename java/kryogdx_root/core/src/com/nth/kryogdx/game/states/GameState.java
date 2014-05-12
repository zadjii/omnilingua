package com.nth.kryogdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nth.kryogdx.entities.Entity;
import com.nth.kryogdx.entities.HexFormation;
import com.nth.kryogdx.entities.Human;
import com.nth.kryogdx.game.Engine;
import com.nth.kryogdx.game.GameRenderer;
import com.nth.kryogdx.game.KryoGDX;
import com.nth.kryogdx.meshes.SimpleAxisFactory;
import com.nth.kryogdx.particles.Particle;
import com.nth.kryogdx.personas.Player;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.util.Constants;
import com.nth.kryogdx.util.MeshBuilder;
import com.nth.kryogdx.util.VertexAttributes;

import java.util.ArrayList;

import static com.nth.kryogdx.util.Constants.GS;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 4/7/14
 * Time: 10:59 AM
 */
public class GameState extends State {


	private Camera camera;

	@Override
	public void create(){

		// Create camera sized to screens width/height with Fov of 75 degrees
		//camera = new PerspectiveCamera(45,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//((OrthographicCamera) camera).zoom = 1.0f/100.0f;
		//((OrthographicCamera) camera).zoom = 1.0f/10.0f;
		((OrthographicCamera) camera).zoom = 1.0f/4.0f;

		initializeInputProcessor();

		camera.position.set(0.0f,2.5f,2.5f);
		camera.position.set(0.0f,2.5f,2.5f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane)
		camera.near = 0.1f;
		camera.far = 300000.0f;

		camera.update();
		//bob = new Texture((Gdx.files.internal("four_corner_gradient.png")));
		//
		//entities.add(new Human());
		//entities.add(new Human());
		//entities.add(new Human());
		//entities.get(1).setXYZ(2*GS, 0, 0);
		//entities.get(2).setXYZ(2*GS, 0, 2*GS);
		//entities.add(new HexFormation(100));
		//entities.get(3).setXYZ(-2*GS, -2*GS, -2*GS);
		//
		//for (int i = 0; i < 10; i++) {
		//	Particle p = new Particle();
		//	p.x = (float)(Math.random() * 15 - 7);
		//	//p.y = (float)(Math.random() * 5 - 2);
		//	p.y = 0;
		//	p.z = (float)(Math.random() * 15 - 7);
		//	p.dx = (float)(Math.random() * 5 - 2);
		//	p.dy = (float)(Math.random() * 5 - 2);
		//	p.dz = (float)(Math.random() * 5 - 2);
		//	particles.add(p);
		//}

	}
	private void initializeInputProcessor(){
		this.inputProcessor = new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				KryoGDX.instance.getCurrentInput().pressKey(keycode);
				return true;
			}

			@Override
			public boolean keyUp(int keycode) {
				KryoGDX.instance.getCurrentInput().releaseKey(keycode);
				if(keycode == Input.Keys.EQUALS)
					KryoGDX.instance.switchToNextState();
				return true;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}
		};
	}

	@Override
	public void update(float delta) {

		KryoGDX.instance.processMessagesFromServer();
		if(KryoGDX.instance.getClientIndex() == Constants.UNASSIGNED) return;
		KryoGDX.instance.processInputs(delta);
		Engine engine = KryoGDX.instance.engine;
		Player player = engine.getPlayer();
		if(player == null)return;
		camera.position.set(player.getCharacter().getPos().add(new Vector3(0, 3 * GS, 3 * GS)));
		//camera.rotateAround(new Vector3(0, 0, 0), new Vector3(.0f, .5f, 0), 1);
		camera.lookAt(player.getCharacter().getPos());
		if(Gdx.input.isTouched()) {
			Vector3 mouse = new Vector3();
			mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			System.out.print("initial: " + mouse.x + "," + mouse.y + "," + mouse.z);
			camera.unproject(mouse);
			System.out.println("\t->: " + mouse.x + "," + mouse.y + "," + mouse.z);

			mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		}
	}

	@Override
	public void render(Shader defaultShader) {
		GameRenderer.render(defaultShader, this);
	}

	@Override
	public void resize(int width, int height) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void pause() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void resume() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public Camera getCamera(){return camera;}
}
