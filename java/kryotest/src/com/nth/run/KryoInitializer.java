package com.nth.run;

import com.esotericsoftware.kryo.Kryo;
import com.nth.entities.*;
import com.nth.requests.*;
import com.nth.game.*;
import com.nth.util.ClientInputState;

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
		target.register(int[].class);

		target.register(WorldStateBroadcast.class);
		target.register(ClientInputState.class);

		target.register(Entity.class);

		target.register(Player.class);


		target.register(ClientDisconnectRequest.class);
		target.register(NewPlayerRequest.class);
		target.register(PlayerIndexResponse.class);
		target.register(QuitRequest.class);



	}
}
