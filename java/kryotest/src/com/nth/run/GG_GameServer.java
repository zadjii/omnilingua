package com.nth.run;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.nth.entities.Entity;
import com.nth.game.StupidEngine;
import com.nth.requests.*;
import com.nth.util.ClientInputState;
import com.nth.util.Constants;
import com.nth.util.UtilF;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 12/4/13
 * Time: 1:14 PM
 */
public class GG_GameServer {

	private static final int TCP_PORT = 50223;
	private static final int UDP_PORT = 51224;

	private static Server server;
	private int nextClientID = 0;

	private static boolean exitIsRequested = false;

	////////////////////////////// Time Management /////////////////////////////
	private static float timeIncrement = 1.0f / 60.0f;
	private float totalTime = 0;
	private long lastTime;
	private float timeAccumulator = 0.0f;
	////////////////////////////// Input Queue /////////////////////////////
	private final List<ClientInputState> inputRequests = Collections.synchronizedList(new LinkedList<ClientInputState>());
	//private LinkedList<InputRequest> networkInputRequestsBuffer = new LinkedList<InputRequest>();


	private StupidEngine stupidEngine;

	public GG_GameServer(){
//		this.engine = new SimpleGameServerEngine();
	}

	public static void main(String[] args) {

		final GG_GameServer instance = new GG_GameServer();
		instance.startServer();

		instance.stupidEngine = new StupidEngine();
		instance.stupidEngine.playerID = Constants.SERVER;

		Thread updateThread = new Thread(new Runnable() {
			@Override
			public void run() {

//				lastTime = UtilF.getTimeMillis();

//				Entity ent = new Entity();
//				ent.setDXDYDZ(.1f,.1f,.1f);
//				instance.engine.getEntities().add(ent);
//				System.out.println((1.0f / 60.0f));
//				Scanner inScanner = new Scanner(System.in);

				while (!exitIsRequested){
					instance.update();
				}
			}
		});
		updateThread.setDaemon(true);
		updateThread.start();

		//For handling commandline input
		Scanner inScanner = new Scanner(System.in);
		while (!exitIsRequested){
			if(inScanner.hasNext()){
				String input = inScanner.nextLine();
				//instance.handleInput(input);
				System.out.println("you typed:\"" + input + "\"");
			}
			//do nothing
		}
		System.out.println("Server commencing shutdown");
		server.close();
		System.exit(0);
	}//End of main


	private void startServer(){
		System.out.println("Beginning simple game server on " +
				"TCP:" + TCP_PORT + " UDP:" + UDP_PORT);
		try {
			server = new Server();
			KryoInitializer.initializeKryo(server.getKryo());
			server.start();
			server.bind(TCP_PORT, UDP_PORT);
			server.addListener(new Listener() {
				public void received (Connection connection, Object object) {
					handleConnection(connection,object);
				}
			});
		} catch (IOException e) {
			System.err.println("Server Encountered an Error");
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			exitIsRequested = true;
		}
		System.out.println("Server successfully started");
	}

	private void handleConnection(final Connection connection, Object object){
//		if (object instanceof String) {
//		    System.out.println("got a connection " + ((String)object) + "\t\t\t:" + System.nanoTime());
//		}


		if(object instanceof NewPlayerRequest){
			NewPlayerRequest msg = (NewPlayerRequest)object;
			PlayerIndexResponse resp = new PlayerIndexResponse();
			resp.index = nextClientID++;
			Entity newb = new Entity(resp.index);
			newb.setWidth(12.0f);
			newb.setHeight((float) (Math.random() * 10 + 5));
			stupidEngine.entities.add(newb);
			System.out.println("added Client " + resp.index + " " + newb.getID());
			connection.sendTCP(resp);
		}
		else if(object instanceof ClientInputState){
			ClientInputState data = (ClientInputState)object;
			synchronized (inputRequests){
				inputRequests.add(data);
			}
			System.out.println("c,ui=" + data.clientIndex + ","+ data.updateIndex + ":"+ data.input);
			//networkInputRequestsBuffer.add(data);
			//if (data.keys.length > 0) {
			//	System.out.print("Input " + data.updateIndex + ":");
			//	for (int i = 0; i < data.keys.length; i++) {
			//		System.out.print(data.keys[i] + ", " + data.states[i] + "; ");
			//	}
			//	System.out.println();
			//}
		}
		else if (object instanceof QuitRequest){
			//exitIsRequested = true;
			System.out.println("Quit requested by a client");
		}
		else{
			System.out.println(object);
		}

	}

	private void update(){
		//*//I'm pretty certain I want this time code, but the GG tutorial
		//has nothing like it in the server.
		long currTime = UtilF.getTimeMillis();
		float frameTime = currTime - lastTime;
		lastTime = currTime;
		frameTime  *= (1.0f / 1000.0f);
		timeAccumulator += frameTime;
		while ( timeAccumulator >= timeIncrement ){
			//currState().update(timeIncrement);
			serverUpdate(timeIncrement);
			timeAccumulator -= timeIncrement;
			totalTime += timeIncrement;
		}
		//currState().render(currShader());
		//*/

		//render();

	}
	private void serverUpdate(float delta){
		processInputs(delta);
		sendWorldState();
	}

	private void processInputs(float delta){
		//List<InputRequest> inputRequestsCopy = (List<InputRequest>) inputRequests.clone();

		LinkedList<ClientInputState> tempI = new LinkedList<ClientInputState>();
		synchronized (inputRequests){
			for (ClientInputState input : inputRequests){
				if(!validateInput(input)){
					tempI.add(input);
					continue;
				}
				stupidEngine.update(delta, input, false);
				Entity entity = stupidEngine.getEntityByID(input.clientIndex);
				if(entity != null){
					entity.lastInputProcessed = input.updateIndex;
				}
				//tempI.add(input);

			}
			inputRequests.clear();
			//for (InputRequest inputRequest : tempI){
			//	inputRequests.remove(inputRequest);
			//}
		}

	}
	private boolean validateInput(ClientInputState input){
		return true;
	}
	private void sendWorldState(){
		if(stupidEngine != null)
			server.sendToAllUDP(stupidEngine.packageEngine());
	}

}
