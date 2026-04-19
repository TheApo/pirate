package com.apogames.pirate.game.menu;

import com.apogames.pirate.Constants;
import com.apogames.pirate.backend.GameProperties;
import com.apogames.pirate.backend.SequentiallyThinkingScreenModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class MenuPreferences extends GameProperties {

	private final String DATE_YEAR = "lastCorrectDateYear";
	private final String DATE_MONTH = "lastCorrectDateMonth";
	private final String DATE_DAY = "lastCorrectDateDay";
	private final String LANGUAGE = "lastLanguage";
	
	public MenuPreferences(SequentiallyThinkingScreenModel mainPanel) {
		super(mainPanel);
	}

	@Override
	public Preferences getPreferences() {
		return Gdx.app.getPreferences("MenuPreferences");
	}

	public void writeLevel() {
		getPref().putInteger(DATE_YEAR, Constants.YEAR);
		getPref().putInteger(DATE_MONTH, Constants.MONTH);
		getPref().putInteger(DATE_DAY, Constants.DAY);
		getPref().putString(LANGUAGE, Constants.REGION);

		getPref().flush();
	}
	
	public void readLevel() {
		Constants.YEAR = getPref().getInteger(DATE_YEAR, 2021);
		Constants.MONTH = getPref().getInteger(DATE_MONTH, 12);
		Constants.DAY = getPref().getInteger(DATE_DAY, 1);
		Constants.REGION = getPref().getString(LANGUAGE, Constants.REGION);
	}

}
