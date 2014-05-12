package com.nth.kryogdx.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nth.kryogdx.game.states.GameState;
import com.nth.kryogdx.game.states.State;
import com.nth.kryogdx.game.states.TestMenuState;
import com.nth.kryogdx.game.states.TestingState;
import com.nth.kryogdx.requests.*;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.tiles.Tiles;
import com.nth.kryogdx.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * All the "client" rendering code.
 * Additionally, a fuckload of network code too.
 * (I apologize. Ill fix it soon(tm))
 */
public class KryoGDX extends Game {

	/** A public reference to the last created version of the game.
	 * Can't possibly go wrong....
	 */
	public static KryoGDX instance;

	/**
	 * We'll put the Engine for the client here.
	 * Both the loading state and the actual game state will need it equally,
	 *  so neither has a particular claim to owning it over the other.
	 * Additionally, all the witchcraft will need to access it directly.
	 */
	public Engine engine;

	// --- Main Application camera management data --- //
	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 800;
	public static final int VIRTUAL_WIDTH = (int) (SCREEN_WIDTH);
	public static final int VIRTUAL_HEIGHT = (int) (SCREEN_HEIGHT);
	private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
	public static OrthographicCamera screenCamera;
	public static Rectangle viewport;
	public static Vector2 crop;

	// --- Other Static Variables --- //
	public boolean wireframe = false;
	public static final int MSAA_NUM_SAMPLES = 4;
	private static final boolean MAINTAIN_ASPECT_RATIO = false;


	////////////////////////////// Shaders //////////////////////////////
	public int currentShaderIndex = 0;
	public static ArrayList<Shader> shaders = new ArrayList<Shader>();
	// --- List of Shaders --- //
	public static Shader testShader;
	private static final int SHADER_TEST_SHADER = 0;
	public static Shader guiShader;
	private static final int SHADER_GUI_SHADER = 1;

	////////////////////////////// States //////////////////////////////

	public int currentStateIndex = -1;
	private ArrayList<State> states = new ArrayList<State>();
	// --- List of States --- //

	private State testMenuState;
	private static final int STATE_TEST_MENU_STATE = 0;
	private State testingState;
	private static final int STATE_TESTING_STATE = 1;
	private State gameState;
	private static final int STATE_GAME_STATE = 2;

	////////////////////////////// Time Management /////////////////////////////
	public static float timeIncrement = 1.0f / 60.0f;
	private float totalTime = 0;
	private long lastTime;
	private float timeAccumulator = 0.0f;
	private long updateIndex = -1;

	//////////////////////////// NETWORK WITCHCRAFT ////////////////////////////
	private Client kryoclient;
	private boolean exitIsRequested = false;
	private int clientIndex = Constants.UNASSIGNED;
	///////////////// Inputs
	private final List<ClientInputRequest> pendingInput
		= Collections.synchronizedList(new LinkedList<ClientInputRequest>());
	private ClientInputRequest currentInputRequest;
	//////////////// Server broadcasts
	private final List<WorldStateBroadcast> serverUpdates
		= Collections.synchronizedList(new LinkedList<WorldStateBroadcast>());



	@Override
	public void create() {
		System.out.println("KryoGDX created");

		instance = this;//This cannot possibly lead to gaping flaws...

		this.engine = new Engine();
		Resources.initResources();
		System.out.println("Textures loaded");

		Tiles.initTiles();
		System.out.println("Tiles Generated");

		screenCamera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

		loadShaders();
		System.out.println("Shaders loaded");

		instance.startClient();//outputs it's own success

		this.initStates();
		this.switchToState(0);
		lastTime = TimeUtils.millis();
		System.out.println("Application started");
	}

	private static void loadShaders(){

		testShader = new Shader("TestShader.vert","TestShader.frag");
		shaders.add(SHADER_TEST_SHADER, testShader);

		guiShader = new Shader("GUIShader.vert","GUIShader.frag");
		shaders.add(SHADER_GUI_SHADER, guiShader);
	}

	private void initStates(){

		this.testMenuState = new TestMenuState();
		testMenuState.create();
		states.add(STATE_TEST_MENU_STATE, testMenuState);

		this.testingState = new TestingState();
		testingState.create();
		states.add(STATE_TESTING_STATE, testingState);

		this.gameState = new GameState();
		gameState.create();
		states.add(STATE_GAME_STATE, gameState);

	}

	public static void standardRenderSetup(Camera camera){



		//Gdx.gl10.glPolygonMode(GL20.GL_FRONT_AND_BACK, wireframe ? GL10.GL_LINE : GL10.GL_FILL);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glDepthFunc(GL20.GL_LEQUAL);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(
			GL20.GL_COLOR_BUFFER_BIT
			| GL20.GL_DEPTH_BUFFER_BIT
			| (Gdx.graphics.getBufferFormat().coverageSampling ?
				GL20.GL_COVERAGE_BUFFER_BIT_NV : 0
		));


		Gdx.gl.glViewport((int) viewport.x, (int) (viewport.y),
				(int) viewport.width, (int) (viewport.height) );

		if(camera != null){
			camera.viewportWidth = viewport.width;
			camera.viewportHeight= viewport.height;

			camera.update();
		}
	}

	/**
	 * Updates then renders.
	 */
	@Override
	public void render() {

		long newTime = (TimeUtils.millis()) ;
		float frameTime = newTime - lastTime;
		lastTime = newTime;
		frameTime  *= (1.0f / 1000.0f);


		timeAccumulator += frameTime;

		while ( timeAccumulator >= timeIncrement ){

			currState().update(timeIncrement);
			timeAccumulator -= timeIncrement;
			totalTime += timeIncrement;
			//kryoclient.sendUDP(new ClientSetupAck());
		}

		currState().render(currShader());

		//if(Gdx.input.isKeyPressed(Input.Keys.EQUALS)){
		//	switchToState((currentStateIndex + 1) % states.size());
		//}

		/* Swap front and back buffers */
		//glfwSwapBuffers(window->windowHandle);

		/* Poll for and process events */
		//glfwPollEvents();
		//if(Display.isCloseRequested()){
		//	ClientDisconnectRequest req = new ClientDisconnectRequest();
		//	req.index = this.clientIndex;
		//	client.sendUDP(req);
		//}
	}


	@Override
	public void resize(int width, int height) {
		if(MAINTAIN_ASPECT_RATIO){
			maintainAspectRatioResize(width, height);
		}
		else{
			normalResize(width, height);
		}
		currState().resize(width, height);
	}

	private void maintainAspectRatioResize(int width, int height){

		// calculate new viewport
		float aspectRatio = (float)width/(float)height;
		float scale = 1f;
		crop = new Vector2(0f, 0f);

		if(aspectRatio > ASPECT_RATIO){
			scale = (float)height/(float)VIRTUAL_HEIGHT;
			crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
		}
		else if(aspectRatio < ASPECT_RATIO){
			scale = (float)width/(float)VIRTUAL_WIDTH;
			crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
		}
		else{
			scale = (float)width/(float)VIRTUAL_WIDTH;
		}

		float w = (float)VIRTUAL_WIDTH*scale;
		float h = (float)VIRTUAL_HEIGHT*scale;



		viewport = new Rectangle(crop.x, crop.y, w, h);
//        System.out.println("Viewport" + viewport.width + ", " + viewport.height);
//        System.out.println("Crop" + crop.x + ", " + crop.y);
//        screenCamera = new OrthographicCamera(width, height);
		screenCamera.setToOrtho(false, viewport.width, viewport.height);
	}

	private void normalResize(int width, int height){

		crop = new Vector2(0f, 0f);

		viewport = new Rectangle(0, 0, width, height);

		screenCamera.setToOrtho(false, viewport.width, viewport.height);
	}


	@Override
	public void pause() {
		currState().pause();
	}

	@Override
	public void resume() {
		currState().resume();
	}

	public ShaderProgram currSP(){
		return shaders.get(currentShaderIndex).getShaderProgram();
	}

	public Shader currShader(){
		return shaders.get(currentShaderIndex);
	}

	private State currState() {
		return states.get(currentStateIndex);
	}

	//todo state - changing from, to a state
	public void switchToState(int newState){
		System.out.println("Switching to state#" + newState + "from: " + this.currentStateIndex);
		this.currentStateIndex = newState;
		Gdx.input.setInputProcessor(states.get(currentStateIndex).getInputProcessor());
	}
	public void switchToNextState(){
		int newState = (currentStateIndex + 1) % states.size();
		switchToState(newState);
	}

	public float getTotalTime(){
		return this.totalTime;
	}








	//shortcut to:
	///////////////////////// THE ACTUAL CODE FOR THE //////////////////////////
	//////////////////////////// NETWORK WITCHCRAFT ////////////////////////////

	private void startClient(){
		System.out.println("Connecting to server... TCP:"
			+ Constants.TCP_PORT + " UDP:" + Constants.UDP_PORT);

		try {
			kryoclient = new Client();
			KryoInitializer.initializeKryo(kryoclient.getKryo());
			kryoclient.start();
			kryoclient.connect(5000, "localhost", Constants.TCP_PORT, Constants.UDP_PORT);

			kryoclient.addListener(
				//new Listener.ThreadedListener(
					new Listener() {
						public void received (Connection connection, Object object) {
							handleConnection(connection, object);
						}
					}
				//)
			);

			//kryoclient.sendTCP(new NewPlayerRequest());

		} catch (IOException e) {
			System.err.println("Client Encountered an Error");
			e.printStackTrace();
			exitIsRequested = true;
		}
		System.out.println("Client successfully connected");
	}
	private void handleConnection(Connection connection, Object object){
//		System.out.println("recieved connection: " + object);
		if (object instanceof PlayerIndexResponse) {
			PlayerIndexResponse data = (PlayerIndexResponse)object;
			System.out.println(data + ":Index,Name=( "
				+ data.getIndex()
				+ ",\""
				+ data.getPlayerName()
				+ "\")"
			);
			this.clientIndex = data.getIndex();
			engine.setPlayer(this.clientIndex);
			//this.currentInput = new ClientInputState();
			//this.currentInput.clientIndex = clientIndex;
		}
		else if (object instanceof WorldStateBroadcast) {
			WorldStateBroadcast data = (WorldStateBroadcast)object;
			System.out.println("received WUp");
			synchronized (serverUpdates){
				serverUpdates.add(data);
			}
		}
		//else if (object instanceof MapMessage) {
		//	MapMessage mapMessage = (MapMessage)object;
		//	//System.out.println(data);
		//	this.engine.setMap(mapMessage.getMap());
		//}
		else if (object instanceof MapPieceMessage) {
			MapPieceMessage mapMessage = (MapPieceMessage)object;
			//System.out.println(data);
			this.engine.setMapPiece(mapMessage.getX(), mapMessage.getMapPiece());

		}
		else System.out.println(object);

		//TODO: Move this to the loading state's update()
		if(this.currentStateIndex == STATE_TEST_MENU_STATE)
			if(engine.isMapAssembled())
				if(this.clientIndex != Constants.UNASSIGNED){
					currentInputRequest = new ClientInputRequest();
					int rc = this.kryoclient.sendUDP(new ClientSetupAck());
					System.out.println("Ack rc=" + rc);
					System.out.println(
						kryoclient.isConnected() + ", "
						+ kryoclient.isIdle()
					);
					this.switchToState(STATE_GAME_STATE);
				}

	}

	public void connectToServer(String playerName){
		int rc = kryoclient.sendTCP(new PlayerConnectRequest(playerName));
		System.out.println("sent PCR, rc=" + rc);
	}

	//////FIXME: This will need go in GameState's update().
	//private void clientUpdate(float delta, long updateIndex){
	//	processMessagesFromServer();
	//	if(this.clientIndex == Constants.UNASSIGNED) return;
	//	processInputs(delta, updateIndex);
	//
	//	Entity entity = stupidEngine.getEntityByID(clientIndex);
	//	//if(entity != null)
	//	//	System.out.println("id:x,y - " + clientIndex + " "+ entity.getX() + " "+ entity.getY());
	//	//render();
	//}

	public void processInputs(float delta){
		if(currentInputRequest.getPlayerIndex() == Constants.UNASSIGNED) {
			//currentInput = new InputStateBuffer();
			currentInputRequest = new ClientInputRequest();
			currentInputRequest.setPlayerIndex(this.clientIndex);
			return;
		}
		//compute dt
		//package up inputs -> currInput
		//send to server
		//apply input (curr input)
		//add curr input to pending input
		//make a new curr input
		currentInputRequest.setUpdateIndex(this.getUpdateIndex());
		sendCurrentInputToServer(currentInputRequest);
		if(currentInputRequest.getPlayerIndex() < 0 )
			throw new RuntimeException("the playerindex should be >=0 here: "
				+ currentInputRequest.getPlayerIndex() + ", "
				+ this.getClientIndex()
			);
		engine.applyInputRequest(currentInputRequest);
		engine.update(delta, true);
		//engine.setLastProcessedInput();
		pendingInput.add(currentInputRequest);

		currentInputRequest = new ClientInputRequest();
		currentInputRequest.setPlayerIndex(this.clientIndex);


	}
	public void processMessagesFromServer(){
		//Temporarily, we'll assume that all of the server messages get to us in
		//  the order they were sent in. Which is probably obviously false.
		LinkedList<WorldStateBroadcast> tempB = new LinkedList<WorldStateBroadcast>();
		synchronized (serverUpdates){
			for(WorldStateBroadcast broadcast : serverUpdates){
				engine.processServerState(broadcast, pendingInput);

				//tempB.add(broadcast);
			}
			serverUpdates.clear();
			//for(WorldStateBroadcast broadcast : tempB){
			//	serverUpdates.remove(broadcast);
			//}
		}
	}
	private void sendCurrentInputToServer(ClientInputRequest curr){

		if(!kryoclient.isConnected()){
			System.out.println(">>>>>da fuck wasn't I connected?");
			try {
				kryoclient.reconnect();
			}catch (IOException ex){
				System.out.println(">>>>>>>>>>Shit an IOE reconnecting");
			}
			if(!kryoclient.isConnected())
				System.out.println(">>>>>>>>>>FUCK THIS");
		}
		int rc = kryoclient.sendUDP(curr);
		System.out.println("sent CIR "
			+ rc + ", "
			+ curr.getUpdateIndex() + ", "
			+ curr.getInputUpdateContents() + ", "
		);

		if(rc == 0){
			if (failureCount++ >= 10)throw new RuntimeException("I'm gonna kill myself now");
		}
	}
	int failureCount = 0;

	public ClientInputRequest getCurrentInput() {
		return currentInputRequest;
	}

	public long getUpdateIndex(){
		return ((long)(totalTime / timeIncrement));
	}
	public int getClientIndex() {
		return clientIndex;
	}
}