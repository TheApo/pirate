package com.apogames.pirate;

import com.apogames.pirate.common.Localization;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.util.Locale;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Localization.getInstance().setLocale(Locale.getDefault());

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(60);
		config.setTitle(Constants.PROGRAM_NAME);
		config.useVsync(true);
		config.setWindowedMode(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

		new Lwjgl3Application(new Pirate(), config);
	}
}
