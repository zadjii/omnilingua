package com.nth.kryogdx.run;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.nth.kryogdx.game.Engine;
import com.nth.kryogdx.requests.*;
import com.nth.kryogdx.util.ClientInputRequest;
import com.nth.kryogdx.util.Constants;
import com.nth.kryogdx.util.KryoInitializer;
import com.nth.kryogdx.util.UtilF;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/10/14
 * Time: 10:39 AM
 */
public class GameServer {


	private static Server kryoserver;
	private static int nextClientID = 0;
	private static boolean exitIsRequested = false;

	////////////////////////////// Time Management /////////////////////////////
	private static float timeIncrement = 1.0f / 60.0f;
	private static float totalTime = 0;
	private static long lastTime;
	private static float timeAccumulator = 0.0f;
	/////////////////////////////// Input Queue ////////////////////////////////
	private static final List<ClientInputRequest> inputRequests
		= Collections.synchronizedList(new LinkedList<ClientInputRequest>());
	/******************************* Something. *******************************/
	//private static final List<WorldStateBroadcast> worldStatesToSend
	//	= Collections.synchronizedList(new LinkedList<WorldStateBroadcast>());

	private static ArrayList<Connection> clientConnections = new ArrayList<Connection>();

	private static Engine engine;


	public static void main(String[] args) {
		Log.set(Log.LEVEL_DEBUG);

		startServer();
		engine = new Engine();
		engine.setPlayer(Constants.SERVER);
		engine.initializeEngine();
		//Thread foo = new Thread(()->{doSomething();});
		Thread updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				long currTime = UtilF.getTimeMillis();
				lastTime = currTime;
				while (!exitIsRequested){
					update();
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
			//synchronized (worldStatesToSend){
			//	for(WorldStateBroadcast b :worldStatesToSend)
			//		kryoserver.sendToAllUDP(b);
			//	worldStatesToSend.clear();
			//}
			//do nothing
		}
		System.out.println("Server commencing shutdown");
		kryoserver.close();
		System.exit(0);
	}

	private static void startServer(){
		System.out.println("Beginning simple game server on " +
			"TCP:" + Constants.TCP_PORT + " UDP:" + Constants.UDP_PORT);
		try {
			kryoserver = new Server();
			KryoInitializer.initializeKryo(kryoserver.getKryo());
			kryoserver.start();
			kryoserver.bind(Constants.TCP_PORT, Constants.UDP_PORT);
			kryoserver.addListener(new Listener() {
				public void received(Connection connection, Object object) {
					handleConnection(connection, object);
				}
			});

		} catch (IOException e) {
			System.err.println("Server Encountered an Error");
			e.printStackTrace();
			exitIsRequested = true;
		}
		System.out.println("Server successfully started");
	}

	private static void handleConnection(Connection connection, Object object){
		//System.out.println(connection.isConnected());
		//System.out.println(connection.getTcpWriteBufferSize());
		//System.out.println(connection.getRemoteAddressTCP());
		if(object instanceof PlayerConnectRequest){
			PlayerConnectRequest msg = (PlayerConnectRequest)object;
			PlayerIndexResponse resp = new PlayerIndexResponse();
			System.out.println("Recieved PCR:" + msg.getPlayerName());
			int playerIndex;
			if(engine.hasPlayer(msg.getPlayerName())){
				System.out.println("\t>Player already existed");
				playerIndex = engine.getPlayerIndex(msg.getPlayerName());
			}
			else{
				System.out.println("\t>Creating new Player");
				playerIndex = engine.createPlayer(msg.getPlayerName());
			}
			resp.setPlayerName(msg.getPlayerName());
			resp.setIndex(playerIndex);
			//new PlayerIndexResponse(playerIndex, msg.getPlayerName());
			//connection.close();
			System.out.println(connection.isConnected());
			System.out.println(connection.getTcpWriteBufferSize());
			System.out.println(connection.getRemoteAddressTCP());
			//int rc = connection.sendTCP(resp);
			System.out.println(connection.isIdle());
			int rc = connection.sendTCP(resp);
			//kryoserver.sendToTCP(connection.getID(), resp);
			//int rc = connection.sendUDP(resp);
			System.out.println(rc + "<- sent PIR " + resp.getPlayerName() + ", " + resp.getIndex());
			System.out.println(connection.isConnected());
			System.out.println(connection.getTcpWriteBufferSize());
			System.out.println(connection.getRemoteAddressTCP());
			System.out.println(connection.isIdle());

			//MapMessage mapMessage = new MapMessage();
			//mapMessage.setMap(engine.getMap());
			//rc = connection.sendTCP(mapMessage);
			//System.out.println(rc + "<- sent MM ");

			for (int i = 0; i < engine.getMap().length; i++) {

				MapPieceMessage mapPieceMessage = new MapPieceMessage();
				mapPieceMessage.setMap(i, engine.getMap()[i]);

				rc = connection.sendTCP(mapPieceMessage);
				System.out.println(rc + "<- sent MPM[" + i + "]");
			}

		}
		else if(object instanceof ClientSetupAck){
			if(!clientConnections.contains(connection))
				clientConnections.add(connection);

			System.out.println("Received a setup Ack:"
					+ "\t(" + connection.isConnected() + ","
					+ connection.getRemoteAddressUDP() + ","
					+ "<no RC>" + ","
					+ connection.isIdle() + ")"
			);
			//int rc = connection.sendUDP(new ClientSetupAck());
			//System.out.println("Sent a setup Ack:"
			//		+ "\t(" + connection.isConnected() + ","
			//		+ connection.getRemoteAddressUDP() + ","
			//		+ rc + ","
			//		+ connection.isIdle() + ")"
			//);
			//System.out.println("___________________");
		}
		else if(object instanceof ClientInputRequest){

			ClientInputRequest data = (ClientInputRequest)object;
			System.out.println("recieved CIR:"
					+ data.getPlayerIndex() + ", "
					+ data.getUpdateIndex() + ", "
					+ data.getInputUpdateContents() + ", "
			);
			synchronized (inputRequests){
				inputRequests.add(data);
			}
			//System.out.println("c,ui=" + data.clientIndex + ","+ data.updateIndex + ":"+ data.input);
			//networkInputRequestsBuffer.add(data);
			//if (data.keys.length > 0) {
			//	System.out.print("Input " + data.updateIndex + ":");
			//	for (int i = 0; i < data.keys.length; i++) {
			//		System.out.print(data.keys[i] + ", " + data.states[i] + "; ");
			//	}
			//	System.out.println();
			//}
		}
		//else if (object instanceof QuitRequest){
		//	//exitIsRequested = true;
		//	System.out.println("Quit requested by a client");
		//}
		else{
			System.out.println(object);
		}
	}


	private static void update(){
		//*//I'm pretty certain I want this time code, but the GG tutorial
		//has nothing like it in the server.
		long currTime = UtilF.getTimeMillis();
		float frameTime = currTime - lastTime;
		lastTime = currTime;
		frameTime  *= (1.0f / 1000.0f);
		timeAccumulator += frameTime;
		//System.out.println(timeAccumulator);
		while ( timeAccumulator >= timeIncrement ){
			//currState().update(timeIncrement);
			serverUpdate(timeIncrement);
			timeAccumulator -= timeIncrement;
			totalTime += timeIncrement;

		}
	}
	private static void serverUpdate(float delta){
		processInputs(timeIncrement);
		//update non-player entities?
		engine.update(delta, true);
		//System.out.println("serverUpdate");
		//System.out.println(kryoserver.getConnections().length);
		sendWorldState();
	}

	private static void processInputs(float delta){
		synchronized (inputRequests){
			for (ClientInputRequest input : inputRequests){
				if(!validateInput(input)){
					continue;
				}
				engine.applyInputRequest(input);
				//engine.update(delta, false);
			}
			inputRequests.clear();
		}
	}
	private static boolean validateInput(ClientInputRequest input){
		return true;
	}
	private static void sendWorldState(){

		//long updateIndex  = ((long)(totalTime / timeIncrement));
		//if(updateIndex % (16l*16l*16l*16l) == 0l)System.out.println(updateIndex);
		//FIXME: Why did commenting both these out fix it?
		//if(engine != null) //TODO: can I remove this?
		WorldStateBroadcast broadcast = engine.packageWorldStateBroadcast();

		for(Connection c : clientConnections) {
		//
			int rc = c.sendUDP(broadcast);
		//	//right now just kinda banking on it working on the first try.
		//	/*
		//	System.out.println("WU:\t(" + c.isConnected() + ","
		//			+ c.getRemoteAddressUDP() + ","
		//			+ rc + ","
		//			+ c.isIdle() + ")"
		//	);//*/
		//	//System.out.print(rc + ",");
		}

		//if(clientConnections.size() > 0) System.out.println();
		//kryoserver.sendToAllUDP(broadcast);
		//synchronized (worldStatesToSend) {
		//	worldStatesToSend.add(engine.packageWorldStateBroadcast());
		//}
	}


}
