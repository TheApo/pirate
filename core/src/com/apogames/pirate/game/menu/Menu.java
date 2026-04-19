package com.apogames.pirate.game.menu;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.backend.SequentiallyThinkingScreenModel;
import com.apogames.pirate.entity.ApoButton;
import com.apogames.pirate.entity.ApoButtonLanguage;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.ai.*;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class Menu extends SequentiallyThinkingScreenModel {

    public static final String FUNCTION_QUIT = "X";
	public static final String FUNCTION_PLAYER_ONE = "playerOne";
	public static final String FUNCTION_PLAYER_TWO = "playerTwo";
	public static final String FUNCTION_PLAYER_THREE = "playerThree";
	public static final String FUNCTION_PLAYER_FOUR = "playerFour";
	public static final String FUNCTION_PLAYER_FIVE = "playerFive";
	public static final String FUNCTION_LEFT_DIFFICULTY = "leftDifficulty";
	public static final String FUNCTION_RIGHT_DIFFICULTY = "rightDifficulty";
	public static final String FUNCTION_LEFT_SIZE = "leftSize";
	public static final String FUNCTION_RIGHT_SIZE = "rightSize";
    public static final String FUNCTION_LANGUAGE = "menu_language";

	private static final int MAX_DIFFICULTY = 1;
	private static final int MIN_DIFFICULTY = 0;

	private int difficulty = MIN_DIFFICULTY;

	private int size = MIN_DIFFICULTY;

	private ArrayList<PiratePlayer> players;
	private ArrayList<PiratePlayer> possiblePlayers;
	private ArrayList<Integer> currentPlayerBot;

    public Menu(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
    	getMainPanel().getButtonByFunction(FUNCTION_QUIT).setVisible(true);
    	if (Constants.IS_HTML) {
    		getMainPanel().getButtonByFunction(FUNCTION_QUIT).setVisible(false);	
    	}
		getMainPanel().getButtonByFunction(FUNCTION_PLAYER_ONE).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_PLAYER_TWO).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_PLAYER_THREE).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FOUR).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FIVE).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_LEFT_DIFFICULTY).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_RIGHT_DIFFICULTY).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_LEFT_SIZE).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_RIGHT_SIZE).setVisible(true);
		getMainPanel().getButtonByFunction(FUNCTION_PLAY).setVisible(true);
    }
    
    @Override
    public void init() {
		ApoButtonLanguage language = (ApoButtonLanguage)(getMainPanel().getButtonByFunction(FUNCTION_LANGUAGE));
        if (getGameProperties() == null) {
        	setGameProperties(new MenuPreferences(this));
            loadProperties();
        }

		if (this.players == null) {
			this.players = new ArrayList<>();
			this.possiblePlayers = new ArrayList<>();
			possiblePlayers.add(new Easy());
			possiblePlayers.add(new Medium());
			possiblePlayers.add(new Hard());
			possiblePlayers.add(new Perfect());
			possiblePlayers.add(null);
			possiblePlayers.add(new Human());

			this.currentPlayerBot = new ArrayList<>();
			this.setPiratePlayer(0, 5);
			this.setPiratePlayer(1, 0);
			this.setPiratePlayer(2, 1);
			this.setPiratePlayer(3, 4);
			this.setPiratePlayer(4, 4);
		}

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        
        setMenuButtonVisible(false);
	}

	private void setPiratePlayer(int player, int playerBot) {
		if (this.players.size() <= player) {
			this.players.add(this.possiblePlayers.get(playerBot));
			this.currentPlayerBot.add(playerBot);
		} else {
			this.players.set(player, this.possiblePlayers.get(playerBot));
			this.currentPlayerBot.set(player, playerBot);
		}
	}

	public ArrayList<PiratePlayer> getPlayers() {
		ArrayList<PiratePlayer> players = new ArrayList<>();
		for (int i = 0; i < this.players.size(); i++) {
			switch (this.currentPlayerBot.get(i)) {
				case 0 : players.add(new Easy()); break;
				case 1 : players.add(new Medium()); break;
				case 2 : players.add(new Hard()); break;
				case 3 : players.add(new Perfect()); break;
				case 4 : players.add(null); break;
				case 5 : players.add(new Human()); break;
			}
		}
		return players;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getSize() {
		return size;
	}

	@Override
    public void keyButtonReleased(int keyCode, char character) {
        super.keyButtonReleased(keyCode, character);
    }

    @Override
    public void mouseButtonFunction(String function) {
        super.mouseButtonFunction(function);
        if (function.equals(Menu.FUNCTION_QUIT)) {
			getMainPanel().quitGame();
        } else if (function.equals(Menu.FUNCTION_LANGUAGE)) {
        	changeLanguage();
        } else if (function.equals(Menu.FUNCTION_PLAYER_ONE)) {
			this.changePlayer(0);
		} else if (function.equals(Menu.FUNCTION_PLAYER_TWO)) {
			this.changePlayer(1);
		} else if (function.equals(Menu.FUNCTION_PLAYER_THREE)) {
			this.changePlayer(2);
		} else if (function.equals(Menu.FUNCTION_PLAYER_FOUR)) {
			this.changePlayer(3);
		} else if (function.equals(Menu.FUNCTION_PLAYER_FIVE)) {
			this.changePlayer(4);
		} else if (function.equals(Menu.FUNCTION_LEFT_DIFFICULTY)) {
			this.changeDifficulty(-1);
		} else if (function.equals(Menu.FUNCTION_RIGHT_DIFFICULTY)) {
			this.changeDifficulty(1);
		} else if (function.equals(Menu.FUNCTION_LEFT_SIZE)) {
			this.changeSize(-1);
		} else if (function.equals(Menu.FUNCTION_RIGHT_SIZE)) {
			this.changeSize(1);
		} else if (function.equals(FUNCTION_PLAY)) {
			getMainPanel().changeToGame();
		}
    }

	private void changePlayer(int player) {
		int index = this.currentPlayerBot.get(player);
		index += 1;

		if (index < this.possiblePlayers.size() && player < 3 && this.possiblePlayers.get(index) == null) {
			index += 1;
		}
		if (index >= this.possiblePlayers.size() || (player != 0 && index + 1 >= this.possiblePlayers.size())) {
			index = 0;
		}

		this.players.set(player, this.possiblePlayers.get(index));
		this.currentPlayerBot.set(player, index);

		if (player == 3 && this.possiblePlayers.get(index) == null) {
			this.players.set(4, this.possiblePlayers.get(index));
			this.currentPlayerBot.set(4, index);
		}
		if (player == 4 && this.possiblePlayers.get(index) != null && this.players.get(3) == null) {
			this.players.set(3, this.possiblePlayers.get(0));
			this.currentPlayerBot.set(3, 0);
		}
	}

	private void changeDifficulty(int add) {
		this.difficulty += add;
		if (this.difficulty < MIN_DIFFICULTY) {
			this.difficulty = MAX_DIFFICULTY;
		} else if (this.difficulty > MAX_DIFFICULTY) {
			this.difficulty = MIN_DIFFICULTY;
		}
	}

	private void changeSize(int add) {
		this.size += add;
		if (this.size < MIN_DIFFICULTY) {
			this.size = MAX_DIFFICULTY;
		} else if (this.size > MAX_DIFFICULTY) {
			this.size = MIN_DIFFICULTY;
		}
	}

	private void changeLanguage() {
		ApoButtonLanguage language = (ApoButtonLanguage)(getMainPanel().getButtonByFunction(FUNCTION_LANGUAGE));
		language.changeText();
		if (language.getText().equals(ApoButtonLanguage.FUNCTION_ENGLISH)) {
			Constants.setLanguage("en");
		} else {
			Constants.setLanguage("de");
		}
		getGameProperties().writeLevel();
	}


    @Override
    protected void quit() {
        Gdx.app.exit();
    }

    @Override
    public void doThink(float delta) {
    	if (Constants.IS_DATE_SET) {
    		Constants.IS_DATE_SET = false;
    		getGameProperties().writeLevel();
    	}
    }

	@Override
    public void render() {
		getMainPanel().spriteBatch.begin();

		this.getMainPanel().spriteBatch.draw(AssetLoader.backgroundTextureRegion, 0,0);
		this.getMainPanel().spriteBatch.draw(AssetLoader.gameInfo, Constants.GAME_WIDTH/2f - 300, 380, 600, 475);

		this.getMainPanel().spriteBatch.draw(AssetLoader.gameHud, 10, 225, Constants.GAME_WIDTH - 20, 155);

		String s = Constants.PROGRAM_NAME;
		this.getMainPanel().drawString(s, Constants.GAME_WIDTH / 2f, 65, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, false, false);

		s = Constants.STRING_PLAYER;
		this.getMainPanel().drawString(s, Constants.GAME_WIDTH / 2f + 5, 238, Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, false, false);

		for (int i = 0; i < this.players.size(); i++) {
			s = "x";
			if (this.players.get(i) != null) {
				s = this.players.get(i).getName();
			}
			this.getMainPanel().drawString(s, Constants.GAME_WIDTH * (0.1f + i * 0.2f), 275, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.MIDDLE, false, false);
		}

		s = Constants.STRING_DIFFICULTY;
		this.getMainPanel().drawString(s, Constants.GAME_WIDTH / 2f + 5, 428, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.MIDDLE, false, false);

		s = Constants.STRING_DIFFICULTY_TEXT[this.difficulty];
		this.getMainPanel().drawString(s, Constants.GAME_WIDTH / 2f + 5, 485, Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, false, false);

		s = Constants.STRING_SIZE;
		this.getMainPanel().drawString(s, Constants.GAME_WIDTH / 2f + 5, 550, Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, false, false);

		s = Constants.STRING_SIZE_TEXT[this.size];
		this.getMainPanel().drawString(s, Constants.GAME_WIDTH / 2f + 5, 610, Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, false, false);

		for (ApoButton button : this.getMainPanel().getButtons()) {
			button.render(this.getMainPanel(), 0, 0);
		}

		getMainPanel().spriteBatch.end();
    }

//	        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
//			Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

    public void drawOverlay() {
    }

    @Override
    public void dispose() {
    }
}
