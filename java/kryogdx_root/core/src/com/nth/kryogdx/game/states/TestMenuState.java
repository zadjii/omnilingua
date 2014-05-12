package com.nth.kryogdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nth.kryogdx.entities.*;
import com.nth.kryogdx.game.KryoGDX;
import com.nth.kryogdx.meshes.RectangleMeshFactory;
import com.nth.kryogdx.meshes.SimpleAxisFactory;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.util.GUIPanel;
import com.nth.kryogdx.util.MeshBuilder;
import com.nth.kryogdx.util.VertexAttributes;

import java.lang.Character;
import java.util.ArrayList;

import static com.nth.kryogdx.util.Constants.GS;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 4/7/14
 * Time: 10:59 AM
 */
public class TestMenuState extends State {


	private Camera camera;

	public Mesh testMesh;
	public GUIPanel panelTest00 =
		new GUIPanel(GUIPanel.TOP_LEFT, new Vector2(16,16), new Vector2(160,160));
	private String playerName = "";

	@Override
	public void create(){

		initializeInputProcessor();

		// Create camera sized to screens width/height with Fov of 75 degrees
		//camera = new PerspectiveCamera(75,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//((OrthographicCamera) camera).zoom = 1.0f/100.0f;
		//((OrthographicCamera) camera).zoom = 1.0f/10.0f;
		//((OrthographicCamera) camera).zoom = 1.0f/4.0f;

		//camera.position.set(0.0f,2.5f,2.5f);
		//camera.position.set(0.0f,2.5f,2.5f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane)
		camera.near = 0.1f;
		camera.far = 300000.0f;

		camera.update();
		//testMesh = RectangleMeshFactory.billboard(-.5f, -.5f, .5f, .5f);
		testMesh = RectangleMeshFactory.billboard(-100.5f, -100.5f, 100.5f, 100.5f);

	}


	@Override
	public void update(float delta) {

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

		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		Shader guiShader = KryoGDX.guiShader;
		guiShader.setTextureUsage(false);
		//modl.translate(new Vector3(-1, 1, 0))
		//	.rotate(new Vector3(1.0f, 0.0f, 0.0f), 180.0f);
		//

		view = new Matrix4();//camera.view;
		proj = new Matrix4();//camera.projection;
		guiShader.getShaderProgram().begin();
		guiShader
			.setProjection(proj)
			.setView(view)
		;
		//guiShader.updateMVP(modl, view, proj);
		////guiShader.billboard(testMesh);
		//guiShader.render(testMesh);
		modl = GUIPanel.getGUIOriginMatrix();
		guiShader.updateModel(modl);
		panelTest00.draw(modl, guiShader, KryoGDX.viewport.getSize(new Vector2()));

		//ShapeRenderer renderer = new ShapeRenderer();
		//renderer.begin(ShapeRenderer.ShapeType.Filled);
		//renderer.setColor(new Color(1, 0, 0, 1));
		//renderer.rect(32, 64, 128, 128);
		//renderer.end();

	}



	private void initializeInputProcessor(){
		this.inputProcessor = new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				if(keycode == Input.Keys.EQUALS &&  Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
					KryoGDX.instance.switchToNextState();
				}
				else if(keycode == Input.Keys.BACKSPACE){
					if(playerName.length() > 0)
						playerName = playerName.substring(0, playerName.length()-1);
				}
				else if(keycode == Input.Keys.ENTER){
					System.out.println("Enter Pressed");
					if(playerName.length() > 0)
						KryoGDX.instance.connectToServer(playerName);
				}
				System.out.println(playerName);
				return true;
			}

			@Override
			public boolean keyTyped(char character) {
				if(Character.isAlphabetic(character) ||Character.isDigit(character) )
					playerName = playerName + character;
				//System.out.println(playerName);
				return true;
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
}
