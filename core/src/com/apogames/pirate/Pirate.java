package com.apogames.pirate;

import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.Game;
import com.apogames.pirate.game.MainPanel;

public class Pirate extends Game {

	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new MainPanel());
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

	public void resume() {
		super.resume();
		AssetLoader.load();
	}
}
