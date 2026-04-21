/*
 * Copyright (c) 2005-2013 Dirk Aporius <dirk.aporius@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.apogames.pirate.game;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.entity.ApoButton;
import com.apogames.pirate.entity.ApoButtonImageWithThree;
import com.apogames.pirate.entity.ApoButtonLanguageImage;
import com.apogames.pirate.game.menu.Menu;
import com.apogames.pirate.game.treasure.Treasure;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ButtonProvider {
	
	private final MainPanel game;
	
	public ButtonProvider(MainPanel game) {
		this.game = game;
	}
	
	private float[] getRandomColor() {
		return Constants.COLORS[(int)(Math.random() * Constants.COLORS.length)];
	}

	public void init() {
		if ((this.game.getButtons() == null) || (this.game.getButtons().size() <= 0)) {
			this.game.getButtons().clear();
			
			BitmapFont font = AssetLoader.font15;
			String text = "";
			String function = Menu.FUNCTION_QUIT;
			int width = 64;
			int height = 64;
			int x = Constants.MENU_OFFSET_X + Constants.MENU_WIDTH - width - 25;
			int y = Constants.GAME_HEIGHT - height - 25;
			ApoButtonImageWithThree button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.quitButton);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(3);
			button.setFont(font);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_BACK;
			x = Constants.GAME_WIDTH - width - 25;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.quitButton);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.menu");
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_RULES;
			x = Constants.GAME_WIDTH - width * 2 - 50;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.helpButton);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.rules_description");
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAY_AGAIN;
			x = Constants.GAME_WIDTH - width * 2 - 50;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playAgainButton);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.new_level");
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_NEXT_PLAYER;
			x = Constants.GAME_WIDTH - width - 25;
			y = 180;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.rightButton);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.next_player");
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_HELP;
			x = Constants.GAME_WIDTH - width * 2 - 50;
			y = 180;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.ruleButton);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.toggle_hint");
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_TREASURE;
			x = Constants.GAME_WIDTH - width - 25;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.treasureButton);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.solve");
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_ONE;
			width = 50;
			height = 50;
			x = 550 + width;
			y = 28;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[0]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 1);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_TWO;
			x = 550 + width * 2 + 15;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[1]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 2);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_THREE;
			x = 550 + width * 3 + 2 * 15;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[2]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 3);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_FOUR;
			x = 550 + width * 4 + 3 * 15;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[3]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 4);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_FIVE;
			x = 550 + width * 5 + 4 * 15;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[4]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 5);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_ONE_HUD;
			width = 50;
			height = 50;
			x = Constants.GAME_WIDTH - AssetLoader.gameInfo.getRegionWidth() + 45;
			y = Constants.GAME_HEIGHT - AssetLoader.gameInfo.getRegionHeight() - 80 + 0 * 65;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[0]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 1);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_TWO_HUD;
			y = Constants.GAME_HEIGHT - AssetLoader.gameInfo.getRegionHeight() - 80 + 1 * 65;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[1]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 2);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_THREE_HUD;
			y = Constants.GAME_HEIGHT - AssetLoader.gameInfo.getRegionHeight() - 80 + 2 * 65;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[2]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 3);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_FOUR_HUD;
			y = Constants.GAME_HEIGHT - AssetLoader.gameInfo.getRegionHeight() - 80 + 3 * 65;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[3]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 4);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_PLAYER_FIVE_HUD;
			y = Constants.GAME_HEIGHT - AssetLoader.gameInfo.getRegionHeight() - 80 + 4 * 65;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[4]);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.ask_pirate", 5);
			this.game.getButtons().add(button);


			text = "";
			function = Treasure.FUNCTION_RULES_LEFT;
			width = 64;
			height = 61;
			x = Constants.GAME_WIDTH/2 - 280;
			y = 25;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.leftButton);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_RULES_RIGHT;
			x = Constants.GAME_WIDTH/2 + 280 - width;
			y = 25;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.rightButton);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_YES;
			width = 50;
			height = 50;
			x = Constants.GAME_WIDTH/2 - width - 10;
			y = Constants.GAME_HEIGHT/2;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.yesButton);
			this.game.getButtons().add(button);

			text = "";
			function = Treasure.FUNCTION_NO;
			x = Constants.GAME_WIDTH/2 + 10;
			y = Constants.GAME_HEIGHT/2;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.quitButton);
			this.game.getButtons().add(button);




			text = "";
			function = Menu.FUNCTION_PLAYER_ONE;
			x = (int)(Constants.MENU_OFFSET_X + Constants.MENU_WIDTH * 0.1f - width/2f);
			y = 310;
			width = 50;
			height = 50;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[0]);
			button.setFont(font);
			button.setMouseOverTextKeyBottom(AssetLoader.gameHud, "button.pirate", 1);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_PLAYER_TWO;
			x = (int)(Constants.MENU_OFFSET_X + Constants.MENU_WIDTH * 0.3f - width/2f);
			y = 310;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[1]);
			button.setFont(font);
			button.setMouseOverTextKeyBottom(AssetLoader.gameHud, "button.pirate", 2);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_PLAYER_THREE;
			x = (int)(Constants.MENU_OFFSET_X + Constants.MENU_WIDTH * 0.5f - width/2f);
			y = 310;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[2]);
			button.setFont(font);
			button.setMouseOverTextKeyBottom(AssetLoader.gameHud, "button.pirate", 3);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_PLAYER_FOUR;
			x = (int)(Constants.MENU_OFFSET_X + Constants.MENU_WIDTH * 0.7f - width/2f);
			y = 310;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[3]);
			button.setFont(font);
			button.setMouseOverTextKeyBottom(AssetLoader.gameHud, "button.pirate", 4);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_PLAYER_FIVE;
			x = (int)(Constants.MENU_OFFSET_X + Constants.MENU_WIDTH * 0.9f - width/2f);
			y = 310;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playerButton[4]);
			button.setFont(font);
			button.setMouseOverTextKeyBottom(AssetLoader.gameHud, "button.pirate", 5);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_LEFT_DIFFICULTY;
			width = 64;
			height = 61;
			x = Constants.GAME_WIDTH/2 - width - 116;
			y = 475;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.leftButton);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(3);
			button.setFont(font);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_RIGHT_DIFFICULTY;
			width = 64;
			height = 61;
			x = Constants.GAME_WIDTH/2 + 125;
			y = 475;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.rightButton);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(3);
			button.setFont(font);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_LEFT_SIZE;
			width = 64;
			height = 61;
			x = Constants.GAME_WIDTH/2 - width - 116;
			y = 600;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.leftButton);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(3);
			button.setFont(font);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_RIGHT_SIZE;
			width = 64;
			height = 61;
			x = Constants.GAME_WIDTH/2 + 125;
			y = 600;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.rightButton);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(3);
			button.setFont(font);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_PLAY;
			width = 260;
			height = 120;
			x = Constants.GAME_WIDTH/2 - width/2;
			y = 700;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.playButton);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(3);
			button.setFont(font);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_TUTORIAL;
			width = 64;
			height = 61;
			x = Constants.MENU_OFFSET_X + 10;
			y = Constants.GAME_HEIGHT - height - 10;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.tutorialButton);
			button.setFont(font);
			button.setMouseOverTextKey(AssetLoader.gameHud, "button.tutorial");
			this.game.getButtons().add(button);

			function = Menu.FUNCTION_LANGUAGE;
			width = 64;
			height = 61;
			x = Constants.MENU_OFFSET_X + 10;
			y = 10;
			ApoButtonLanguageImage languageButton = new ApoButtonLanguageImage(x, y, width, height, function, AssetLoader.helpButton);
			languageButton.setFont(AssetLoader.font25);
			languageButton.setMouseOverTextKey(AssetLoader.gameHud, "button.language");
			this.game.getButtons().add(languageButton);

//			text = "";
//			function = ChristmasTrain.START;
//			width = 60;
//			height = 65;
//			x = (ChristmasTrain.WIDTH * ChristmasTrain.SCALE)/2 - width/2;
//			y = (ChristmasTrain.HEIGHT * ChristmasTrain.SCALE) - 5 - height;
//			button = new ApoButtonImage(x, y, width, height, function, text, AssetLoader.buttonTextureRegion[0][0]);
//			button.setStroke(3);
//			button.setFont(font);
//			this.game.getButtons().add(button);

			for (int i = 0; i < this.game.getButtons().size(); i++) {
				this.game.getButtons().get(i).setBOpaque(false);
			}
		}
	}
}
