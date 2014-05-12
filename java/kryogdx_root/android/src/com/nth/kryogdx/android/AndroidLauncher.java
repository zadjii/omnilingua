package com.nth.kryogdx.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nth.kryogdx.game.KryoGDX;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();


		//cfg.useGL20 = true; // It looks like 20 is the only supported mode now
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		cfg.numSamples = KryoGDX.MSAA_NUM_SAMPLES;//enable N times anti aliasing

		initialize(new KryoGDX(), cfg);
	}
}
