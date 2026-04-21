package com.apogames.pirate;

import android.os.Bundle;

import com.apogames.pirate.common.Localization;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.Locale;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constants.IS_ANDROID = true;
		setTitle(Constants.PROGRAM_NAME);
		Localization.getInstance().setLocale(Locale.getDefault());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		initialize(new Pirate(), config);
	}
}
