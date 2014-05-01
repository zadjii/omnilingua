package com.nth.run;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nth.entities.Entity;
import com.nth.game.StupidEngine;
import com.nth.requests.*;
import com.nth.util.ClientInputState;
import com.nth.util.Constants;
import com.nth.util.UtilF;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 12/4/13
 * Time: 4:22 PM
 */
public class GG_DisplayClient extends BasicGame {

	private static final int TCP_PORT = 50223;
	private static final int UDP_PORT = 51224;

	private Client client;
	private Connection serverConnection;

	private boolean exitIsRequested = false;

	private int clientIndex = Constants.UNASSIGNED;

	private StupidEngine stupidEngine;

	////////////////////////////// Time Management /////////////////////////////
	//public static float timeIncrement = .5f;//1.0f / 60.0f;
	public static float timeIncrement = 1.0f / 60.0f;
	private float totalTime = 0;
	private long lastTime;
	private float timeAccumulator = 0.0f;
	private long updateIndex = -1;

	///////////////////////////// Inputs /////////////////////////////
	private Input input;
	//private final List<InputStateBuffer> pendingInput = Collections.synchronizedList(new LinkedList<InputStateBuffer>());
	private final List<ClientInputState> pendingInput = Collections.synchronizedList(new LinkedList<ClientInputState>());
	//private InputStateBuffer currentInput;
	private ClientInputState currentInput;


	//////////////// Server broadcasts
	private final List<WorldStateBroadcast> serverUpdates = Collections.synchronizedList(new LinkedList<WorldStateBroadcast>());


	public GG_DisplayClient(String gamename){
		super(gamename);

	}
	public static void main(String[] args) {
		GG_DisplayClient instance = new GG_DisplayClient("Multiplayer Display Client");

		instance.stupidEngine = new StupidEngine();

		instance.startClient();
		//instance.currentInput = new InputStateBuffer();
		instance.currentInput = new ClientInputState();
//		Scanner inScanner = new Scanner(System.in);
//		while (!exitIsRequested){
//			if(inScanner.hasNext()){
//				String input = inScanner.nextLine();
//				handleInput(input);
//			}
//			//do nothing
//		}
		try{
			AppGameContainer appgc;
			appgc = new AppGameContainer(instance);
			appgc.setDisplayMode(640, 480, false);
			appgc.setAlwaysRender(true);
			appgc.setTargetFrameRate(60);

			instance.lastTime = UtilF.getTimeMillis();
			instance.input = new Input(appgc.getHeight());

			instance.client.sendTCP(new NewPlayerRequest());
			appgc.start();
		}
		catch (SlickException ex){
//			Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
		}



		System.out.println("Client commencing shutdown");
		instance.client.close();
		System.exit(0);
	}
	private void startClient(){
		System.out.println("Connecting to server... TCP:" + TCP_PORT + " UDP:" + UDP_PORT);

		try {
			client = new Client();
			KryoInitializer.initializeKryo(client.getKryo());
			client.start();
			client.connect(5000, "localhost", TCP_PORT, UDP_PORT);
			client.addListener(new Listener() {
				public void received (Connection connection, Object object) {
					handleConnection(connection, object);
				}
			});
			//client.sendTCP(new NewPlayerRequest());
		} catch (IOException e) {
			System.err.println("Client Encountered an Error");
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			exitIsRequested = true;
		}
		System.out.println("Client successfully connected");
	}


	private void handleConnection(Connection connection, Object object){
//		System.out.println("recieved connection: " + object);

		if (object instanceof PlayerIndexResponse) {
			PlayerIndexResponse data = (PlayerIndexResponse)object;
			System.out.println(data + " " + data.index);
			this.clientIndex = data.index;
			stupidEngine.playerID = clientIndex;
			//this.currentInput = new InputStateBuffer();
			this.currentInput = new ClientInputState();

			this.currentInput.clientIndex = clientIndex;
		}
		if (object instanceof WorldStateBroadcast) {
			//System.out.println(object);
			WorldStateBroadcast data = (WorldStateBroadcast)object;
			//System.out.println(data.text);
			//System.out.println("WU: " + data.entities.size());
			//printEntities(data.entities);
			//System.out.println();
			synchronized (serverUpdates){
				serverUpdates.add(data);
			}
		}
	}

	private void printEntities(List<Entity> entities){
		System.out.print("entities[\n");
		for(Entity e : entities){
			System.out.print("\t"+e.getID()+":("+e.getX()+","+e.getY()+","+e.getZ()+"):\t("+e.getDX()+","+e.getDY()+","+e.getDZ()+"),\n");
		}
		System.out.print("]\n");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		//To change body of implemented methods use File | Settings | File Templates.

		gc.getInput().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(int k, char c) {
				//currentInput.addInput(k, InputStateBuffer.PRESSED);
				currentInput.pressKey(k);
			}
			@Override
			public void keyReleased(int k, char c) {
				//currentInput.addInput(k, InputStateBuffer.RELEASED);
				currentInput.releaseKey(k);
			}
			@Override
			public void setInput(Input input) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
			@Override
			public boolean isAcceptingInput() {
				return true;  //To change body of implemented methods use File | Settings | File Templates.
			}
			@Override
			public void inputEnded() {
				//To change body of implemented methods use File | Settings | File Templates.
			}
			@Override
			public void inputStarted() {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
		gc.getInput().addMouseListener(new MouseListener() {
			@Override
			public void mouseWheelMoved(int i) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void mouseClicked(int i, int i2, int i3, int i4) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void mousePressed(int i, int i2, int i3) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void mouseReleased(int i, int i2, int i3) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void mouseMoved(int i, int i2, int i3, int i4) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void mouseDragged(int i, int i2, int i3, int i4) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void setInput(Input input) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public boolean isAcceptingInput() {
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void inputEnded() {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void inputStarted() {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
	}

	@Override
	public void update(GameContainer gameContainer, int i) throws SlickException {
		//To change body of implemented methods use File | Settings | File Templates.


		//*//I'm pretty certain I want this time code, but the GG tutorial
		//has nothing like it in the server.
		long currTime = UtilF.getTimeMillis();
		float frameTime = currTime - lastTime;
		lastTime = currTime;
		frameTime  *= (1.0f / 1000.0f);
		timeAccumulator += frameTime;
		while ( timeAccumulator >= timeIncrement ){
			//System.out.println("total time = " + (totalTime) );
			//System.out.println("update index = " + (long)(totalTime / timeIncrement) + "," + updateIndex);

			//currState().update(timeIncrement);
			//if((updateIndex + 1) != (long)((float)totalTime * timeIncrement)){
			//	System.err.println("This was unexpected....");
			//	System.err.println("  (updateIndex + 1) = " + (updateIndex + 1));
			//	System.err.println("  (long)((float)totalTime * timeIncrement) = " + (long)((float)totalTime * timeIncrement));
			//}
			updateIndex = (long)(totalTime / timeIncrement);

			clientUpdate(timeIncrement, updateIndex);
			//System.out.println(updateIndex + ", " + pendingInput.size() + ", " + serverUpdates.size());
			timeAccumulator -= timeIncrement;
			totalTime += timeIncrement;
			//updateIndex++;

			//while(updateIndex > 2){
			//
			//}
			//updateIndex = totalTime / timeIncrement;

		}

		//currState().render(currShader());
		//*/

		// System.out.println(Display.isCloseRequested());
		if(Display.isCloseRequested()){
			ClientDisconnectRequest req = new ClientDisconnectRequest();
			req.index = this.clientIndex;
			client.sendUDP(req);
		}
	}

	private void clientUpdate(float delta, long updateIndex){
		processMessagesFromServer();
		if(this.clientIndex == Constants.UNASSIGNED) return;
		processInputs(delta, updateIndex);

		Entity entity = stupidEngine.getEntityByID(clientIndex);
		//if(entity != null)
		//	System.out.println("id:x,y - " + clientIndex + " "+ entity.getX() + " "+ entity.getY());
		//render();
	}

	private void processInputs(float delta, long updateIndex){
		if(currentInput.clientIndex == Constants.UNASSIGNED) {
			//currentInput = new InputStateBuffer();
			currentInput = new ClientInputState();
			currentInput.clientIndex = this.clientIndex;
			return;
		}
		int[] keysTests = new int[]{
				Input.KEY_Q,
				Input.KEY_W,
				Input.KEY_E,
				Input.KEY_A,
				Input.KEY_S,
				Input.KEY_D
		};
		//compute dt
		//package up inputs -> currInput
		//send to server
		//apply input (curr input)
		//add curr input to pending input
		//make a new curr input
		currentInput.updateIndex  = ((long)(totalTime / timeIncrement));
		sendCurrentInputToServer(currentInput);

		stupidEngine.update(delta, currentInput, true);

		pendingInput.add(currentInput);

		currentInput = new ClientInputState(currentInput);


	}

	private void processMessagesFromServer(){
		//Collections.sort(serverUpdates, new Comparator<WorldStateBroadcast>() {
		//	@Override
		//	public int compare(WorldStateBroadcast one, WorldStateBroadcast two) {
		//		if(one.lastUpdateIndex < two.lastUpdateIndex )return -1;
		//		else if(one.lastUpdateIndex < two.lastUpdateIndex )return 0;
		//		else return 1;
		//	}
		//});
		//Temporarily, we'll assume that all of the server messages get to us in
		//  the order they were sent in. Which is obviously false.

		LinkedList<WorldStateBroadcast> tempB = new LinkedList<WorldStateBroadcast>();
		synchronized (serverUpdates){
			for(WorldStateBroadcast broadcast : serverUpdates){
				stupidEngine.processServerState(broadcast, pendingInput);
				tempB.add(broadcast);
			}
			for(WorldStateBroadcast broadcast : tempB){
				serverUpdates.remove(broadcast);
			}
		}
	}

	private void sendCurrentInputToServer(ClientInputState curr){
		//client.sendTCP(curr.toRequest());//is this it? fo real?
		client.sendUDP(curr);
		//System.out.print("sent " + curr.updateIndex + ":");
		//for(InputState s: curr.inputs){
		//	System.out.print(s.button + ", " + s.state + "; ");
		//}
		//System.out.println();
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.white);
		for (int i = 0; i < 128; i++) {
			g.fillRect(i * 16, 0, 1, gc.getHeight());
			g.fillRect(0, i * 16, gc.getWidth(), 1);
		}
		//if(stupidEngine != null)
		for (Entity e: stupidEngine.getEntities()){
			if(e.getID() == this.clientIndex)g.setColor(Color.cyan);
			else g.setColor(Color.red);
			g.fillRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		}
	}
}
