package com.nth.kryogdx.util;

import com.esotericsoftware.kryo.Kryo;
import com.nth.kryogdx.entities.*;
import com.nth.kryogdx.entities.Character;
import com.nth.kryogdx.game.*;

import com.nth.kryogdx.personas.*;
import com.nth.kryogdx.requests.*;
import com.nth.kryogdx.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 12/4/13
 * Time: 1:19 PM
 */
public class KryoInitializer {
	public static void initializeKryo(Kryo target){

		target.register(LinkedList.class);
		target.register(ArrayList.class);
		target.register(HashMap.class);
		//target.register(String.class);
		target.register(short[].class);
		target.register(short[][].class);
		target.register(int[].class);
		target.register(int[][].class);
		target.register(float[].class);
		target.register(float[][].class);
		target.register(double[].class);
		target.register(double[][].class);
		target.register(boolean[].class);
		target.register(boolean[][].class);

		target.register(WorldStateBroadcast.class);
		target.register(ClientInputState.class);
		target.register(ClientInputRequest.class);

		target.register(Entity.class);
		target.register(Character.class);
		target.register(Human.class);

		target.register(PersonaMap.class);
		target.register(Persona.class);
		target.register(Player.class);


		//target.register(ClientDisconnectRequest.class);
		target.register(PlayerConnectRequest.class);
		target.register(PlayerIndexResponse.class);
		//target.register(MapMessage.class);
		target.register(MapPieceMessage.class);
		target.register(ClientSetupAck.class);
		//target.register(QuitRequest.class);



	}
}
