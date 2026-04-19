package com.apogames.pirate.game.treasure;

import com.apogames.pirate.backend.GameProperties;
import com.apogames.pirate.backend.SequentiallyThinkingScreenModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class TreasurePreferences extends GameProperties {
	
	public TreasurePreferences(SequentiallyThinkingScreenModel mainPanel) {
		super(mainPanel);
	}

	@Override
	public Preferences getPreferences() {
		return Gdx.app.getPreferences("TreasurePreferences");
	}

	public void writeLevel() {

		getPref().flush();
	}
	
	public void readLevel() {
	}

}
