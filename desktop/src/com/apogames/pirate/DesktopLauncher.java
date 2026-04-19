package com.apogames.pirate;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(60);
		config.setTitle(Constants.PROGRAM_NAME);
		config.useVsync(true);
		config.setWindowedMode(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

//		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//
//		config.title = Constants.PROGRAM_NAME;
//		config.samples = 1;
//		config.width = Constants.GAME_WIDTH;
//		config.height = Constants.GAME_HEIGHT;
//		config.forceExit = true;
//		config.fullscreen = false;
//		config.vSyncEnabled = true;
//		config.foregroundFPS = 60;

		new Lwjgl3Application(new Pirate(), config);
	}
}
