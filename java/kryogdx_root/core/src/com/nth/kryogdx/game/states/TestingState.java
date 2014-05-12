package com.nth.kryogdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nth.kryogdx.entities.*;
import com.nth.kryogdx.entities.Character;
import com.nth.kryogdx.game.KryoGDX;
import com.nth.kryogdx.meshes.SimpleAxisFactory;
import com.nth.kryogdx.meshes.SimpleHexFactory;
import com.nth.kryogdx.particles.Particle;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.util.Constants;
import static com.nth.kryogdx.util.Constants.GS;
import com.nth.kryogdx.util.MeshBuilder;
import com.nth.kryogdx.util.VertexAttributes;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 4/7/14
 * Time: 10:59 AM
 */
public class TestingState extends State {


	private Camera camera;

	public Mesh axis;
	public Mesh testBuiltMesh;
	public Mesh testManualMesh;

	Texture bob;
	//Texture mike;
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static ArrayList<Particle> particles = new ArrayList<Particle>();


	@Override
	public void create(){

		// Create camera sized to screens width/height with Fov of 75 degrees
		//camera = new PerspectiveCamera(45,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//((OrthographicCamera) camera).zoom = 1.0f/100.0f;
		((OrthographicCamera) camera).zoom = 1.0f/10.0f;
		//((OrthographicCamera) camera).zoom = 1.0f/4.0f;

		initializeInputProcessor();

		camera.position.set(0.0f,2.5f,2.5f);
		camera.position.set(0.0f,2.5f,2.5f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane)
//		camera.near = 0.1f;
		camera.far = 300000.0f;


		camera.update();

		initTestMesh();
		bob = new Texture((Gdx.files.internal("four_corner_gradient.png")));
		//mike = new Texture((Gdx.files.internal("Sprite-0003.png")));

		entities.add(new Human());
		entities.add(new Human());
		entities.add(new Human());
		entities.get(1).setXYZ(2*GS, 0, 0);
		entities.get(2).setXYZ(2*GS, 0, 2*GS);
		entities.add(new HexFormation(100));
		entities.get(3).setXYZ(-2*GS, -2*GS, -2*GS);

		for (int i = 0; i < 10; i++) {
			Particle p = new Particle();
			p.x = (float)(Math.random() * 15 - 7);
			//p.y = (float)(Math.random() * 5 - 2);
			p.y = 0;
			p.z = (float)(Math.random() * 15 - 7);
			p.dx = (float)(Math.random() * 5 - 2);
			p.dy = (float)(Math.random() * 5 - 2);
			p.dz = (float)(Math.random() * 5 - 2);
			particles.add(p);
		}



	}

	private void initTestMesh(){
		//*
		MeshBuilder newQuadBuilder = new MeshBuilder();

		newQuadBuilder
				.pushVert(new VertexAttributes(
						new Vector3(-0.5f, -0.5f, 0),
						new Color(0.0f, 0.0f, 1.0f, 1.0f),
						new Vector2(1, 1),
						new Vector3(0, 0, 1)))
				.pushVert(new VertexAttributes(
						new Vector3(0.5f, -0.5f, 0),
						new Color(1.0f, 0.0f, 1.0f, 1.0f),
						new Vector2(0, 1),
						new Vector3(0, 0, 1)))
				.pushVert(new VertexAttributes(
						new Vector3(0.5f, 0.5f, 0),
						new Color(0.0f, 0.0f, 1.0f, 1.0f),
						new Vector2(0, 0),
						new Vector3(0, 0, 1)))
				.pushVert(new VertexAttributes(
						new Vector3(-0.5f, 0.5f, 0),
						new Color(0.0f, 0.0f, 1.0f, 1.0f),
						new Vector2(1, 0),
						new Vector3(0, 0, 1)))
		//.pushVert(new VertexAttributes(new Vector3( 0.5f, -0.5f, 0), new Color(0.0f, 1.0f, 0.0f, 1.0f)))
		//.pushVert(new VertexAttributes(new Vector3( 0.5f,  0.5f, 0), new Color(1.0f, 0.0f, 0.0f, 1.0f)))
		//.pushVert(new VertexAttributes(new Vector3(-0.5f,  0.5f, 0), new Color(1.0f, 1.0f, 1.0f, 1.0f)))
		;
		newQuadBuilder
				.pushIndices(new int[]{0, 1, 2})
				.pushIndices(new int[]{2, 3, 0})
		;
		testBuiltMesh = newQuadBuilder.createMesh();
		//*/
//		testManualMesh = new Mesh(true, 4, 6,
//				VertexAttribute.Position(),
//				VertexAttribute.ColorUnpacked(),
//				VertexAttribute.TexCoords(0)
//		);
//		testManualMesh.setVertices(new float[]{
//				-0.5f, -0.5f, 0,     1, 1, 1, 1,     0, 1,
//				 0.5f, -0.5f, 0,     1, 1, 1, 1,     1, 1,
//				 0.5f,  0.5f, 0,     1, 1, 1, 1,     1, 0,
//				-0.5f,  0.5f, 0,     1, 1, 1, 1,     0, 0
//		});
//		testManualMesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});




		axis = SimpleAxisFactory.simpleAxis(1.0f * GS);

	}

	private void initializeInputProcessor(){
		this.inputProcessor = new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
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

		if(Gdx.input.isKeyPressed(Input.Keys.W))
			entities.get(0).setDZ(-100);
		else if(Gdx.input.isKeyPressed(Input.Keys.S))
			entities.get(0).setDZ( 100);
		else entities.get(0).setDZ(0);
		if(Gdx.input.isKeyPressed(Input.Keys.A))
			entities.get(0).setDX(-100);
		else if(Gdx.input.isKeyPressed(Input.Keys.D))
			entities.get(0).setDX( 100);
		else entities.get(0).setDX(0);
		for (Entity e : entities){
			try {
				e.tick(delta);
			}catch (RuntimeException ex){}
		}
		for (Particle p : particles){
			p.tick(delta);
		}
		//camera.translate(
		//	entities.get(0).getDX()*delta,
		//	entities.get(0).getDY()*delta,
		//	entities.get(0).getDZ()*delta
		//);
		camera.position.set(entities.get(0).getPos().add(new Vector3(0, 3*GS, 3*GS)));
		//camera.rotateAround(new Vector3(0, 0, 0), new Vector3(.0f, .5f, 0), 1);
		camera.lookAt(entities.get(0).getPos());
		//camera.rotateAround(
		//		new Vector3(0, 0, 0),
		//		new Vector3(
		//				(float)Math.random(),
		//				(float)Math.random(),
		//				(float)Math.random()
		//		),
		//		(float)Math.random()*2.0f);


		//*

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

		currShader.updateMVP(modl, view, proj);

		axis.render(currSP, GL20.GL_LINE_STRIP);
		//currShader.useTexture(mike);
		//testBuiltMesh.render(currSP, GL20.GL_TRIANGLES);
		//currShader.setTextureUsage(false);

		modl = new Matrix4();
		for (Entity e : entities){
			e.render(modl, currShader);
		}
		for (Particle p : particles){
			p.render(modl, currShader);
		}

		currShader.setTextureUsage(false);

		//FFS I can really? ahg.
		/*
		ShapeRenderer renderer = new ShapeRenderer();
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(new Color(0, 1, 1, 1));
		renderer.rect(32, 64, 128, 128);
		renderer.end();
		*/
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
