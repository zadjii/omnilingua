package com.nth.kryogdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nth.kryogdx.game.KryoGDX;


public class DesktopLauncher {
	private static final short FULLSCREEN = 0;
	private static final short GALAXY_S3 = 1;
	private static final short NEXUS_ONE = 2;
	private static final short EVO3D = 3;
	private static final short BIG_WINDOW = 4;
	private static final short SMALL_PHONE = 5;

	// choose size here
	private static final short DEBUG_SIZE = NEXUS_ONE;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		if (DEBUG_SIZE == FULLSCREEN) {
			cfg.width = 1920;
			cfg.height = 1080;
			cfg.fullscreen = true;
		} else if (DEBUG_SIZE == GALAXY_S3) {
			cfg.width = 1280;
			cfg.height = 720;
			cfg.fullscreen = false;
		} else if (DEBUG_SIZE == EVO3D) {
			cfg.width = 960;
			cfg.height = 540;
			cfg.fullscreen = false;
		} else if (DEBUG_SIZE == NEXUS_ONE) {
			cfg.width = 800;
			cfg.height = 480;
			cfg.fullscreen = false;
		} else if (DEBUG_SIZE == SMALL_PHONE) {
			cfg.width = 480;
			cfg.height = 320;
			cfg.fullscreen = false;
		} else if (DEBUG_SIZE == BIG_WINDOW) {
			cfg.width = 1440;
			cfg.height = 900;
			cfg.fullscreen = false;
		}

		//cfg.useGL20 = true;
		cfg.samples = KryoGDX.MSAA_NUM_SAMPLES;
		cfg.resizable = true;
//		cfg.vSyncEnabled = false; // Setting to false disables vertical sync
//		cfg.foregroundFPS = 0;// Setting to 0 disables foreground fps throttling
//		cfg.backgroundFPS = 0;// Setting to 0 disables background fps throttling

		new LwjglApplication(new KryoGDX(), cfg);
	}
}
