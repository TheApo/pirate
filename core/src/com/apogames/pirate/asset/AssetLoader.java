/*
 * Copyright (c) 2005-2020 Dirk Aporius <dirk.aporius@gmail.com>
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

package com.apogames.pirate.asset;

import com.apogames.pirate.entity.NineSlice;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Asset loader.
 */
public class AssetLoader {

	private static Texture backgroundTexture;
	public static TextureRegion backgroundTextureRegion;

	private static Texture mirrorTexture;
	public static TextureRegion dpad;

	private static Texture leftButtonTexture;
	public static TextureRegion[] leftButton;

	private static Texture rightButtonTexture;
	public static TextureRegion[] rightButton;

	private static Texture quitButtonTexture;
	public static TextureRegion[] quitButton;

	private static Texture yesButtonTexture;
	public static TextureRegion[] yesButton;

	private static Texture playButtonTexture;
	public static TextureRegion[] playButton;

	private static Texture helpButtonTexture;
	public static TextureRegion[] helpButton;

	private static Texture ruleButtonTexture;
	public static TextureRegion[] ruleButton;

	private static Texture treasureButtonTexture;
	public static TextureRegion[] treasureButton;

	private static Texture tutorialButtonTexture;
	public static TextureRegion[] tutorialButton;

	private static Texture playAgainButtonTexture;
	public static TextureRegion[] playAgainButton;

	private static Texture gameTitleTexture;
	public static TextureRegion gameTitle;

	private static Texture tilesTexture;
	public static TextureRegion[] tiles;

	public static TextureRegion arrow;

	private static Texture objectivesTexture;
	public static TextureRegion[][] objectives;

	private static Texture animalsTexture;
	public static TextureRegion[][] animals;

	private static Texture[] playerButtonTexture;
	public static TextureRegion[][] playerButton;

	private static Texture gameHudTexture;
	public static TextureRegion gameHud;

	public static Texture gameInfoTexture;
	public static TextureRegion gameInfo;
	public static NineSlice hudInfoSlice;

	private static Texture tilesOverlayTexture;
	public static TextureRegion[] tilesOverlay;

	private static Texture hudAskTexture;
	public static TextureRegion hudAsk;

	private static Texture scrollTexture;
	public static TextureRegion scroll;

	private static Texture scrollWonTexture;
	public static TextureRegion scrollWon;

	private static Texture treasureTexture;
	public static TextureRegion treasure;

	private static Texture starTexture;
	public static TextureRegion star;

	private static Texture mouseCursorTexture;
	public static TextureRegion mouseCursor;

	public static BitmapFont font40;
	public static BitmapFont font20;
	public static BitmapFont font15;
	public static BitmapFont font25;
	public static BitmapFont font30;

	public static void load() {
		backgroundTexture = new Texture(Gdx.files.internal("background.png"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		backgroundTextureRegion = new TextureRegion(backgroundTexture, 0, 0, 1024, 900);
		backgroundTextureRegion.flip(false, true);

		mirrorTexture = new Texture(Gdx.files.internal("mirror.png"));
		mirrorTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		dpad = new TextureRegion(mirrorTexture, 0, 75, 74, 73);
		dpad.flip(false, true);

		leftButtonTexture = new Texture(Gdx.files.internal("pirate/button_left.png"));
		leftButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		leftButton = new TextureRegion[3];
		for (int x = 0; x < leftButton.length; x++) {
			leftButton[x] = new TextureRegion(leftButtonTexture, x * 544, 0, 544, 523);
			leftButton[x].flip(false, true);
		}

		rightButtonTexture = new Texture(Gdx.files.internal("pirate/button_right.png"));
		rightButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		rightButton = new TextureRegion[3];
		for (int x = 0; x < rightButton.length; x++) {
			rightButton[x] = new TextureRegion(rightButtonTexture, x * 544, 0, 544, 523);
			rightButton[x].flip(false, true);
		}

		quitButtonTexture = new Texture(Gdx.files.internal("pirate/button_quit.png"));
		quitButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		quitButton = new TextureRegion[3];
		for (int x = 0; x < quitButton.length; x++) {
			quitButton[x] = new TextureRegion(quitButtonTexture, x * 396, 0, 396, 374);
			quitButton[x].flip(false, true);
		}

		yesButtonTexture = new Texture(Gdx.files.internal("pirate/button_yes.png"));
		yesButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		yesButton = new TextureRegion[3];
		for (int x = 0; x < yesButton.length; x++) {
			yesButton[x] = new TextureRegion(yesButtonTexture, x * 396, 0, 396, 374);
			yesButton[x].flip(false, true);
		}

		playButtonTexture = new Texture(Gdx.files.internal("pirate/button_play.png"));
		playButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		playButton = new TextureRegion[3];
		for (int x = 0; x < playButton.length; x++) {
			playButton[x] = new TextureRegion(playButtonTexture, x * 544, 0, 544, 246);
			playButton[x].flip(false, true);
		}

		helpButtonTexture = new Texture(Gdx.files.internal("pirate/button_showHint.png"));
		helpButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		helpButton = new TextureRegion[3];
		for (int x = 0; x < helpButton.length; x++) {
			helpButton[x] = new TextureRegion(helpButtonTexture, x * 562, 0, 562, 554);
			helpButton[x].flip(false, true);
		}

		ruleButtonTexture = new Texture(Gdx.files.internal("pirate/button_showRules.png"));
		ruleButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		ruleButton = new TextureRegion[3];
		for (int x = 0; x < ruleButton.length; x++) {
			ruleButton[x] = new TextureRegion(ruleButtonTexture, x * 562, 0, 562, 554);
			ruleButton[x].flip(false, true);
		}

		treasureButtonTexture = new Texture(Gdx.files.internal("pirate/button_treasure.png"));
		treasureButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		treasureButton = new TextureRegion[3];
		for (int x = 0; x < treasureButton.length; x++) {
			treasureButton[x] = new TextureRegion(treasureButtonTexture, x * 562, 0, 562, 554);
			treasureButton[x].flip(false, true);
		}

		tutorialButtonTexture = new Texture(Gdx.files.internal("pirate/button_tutorial.png"));
		tutorialButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		tutorialButton = new TextureRegion[3];
		for (int x = 0; x < tutorialButton.length; x++) {
			tutorialButton[x] = new TextureRegion(tutorialButtonTexture, x * 562, 0, 562, 554);
			tutorialButton[x].flip(false, true);
		}

		playAgainButtonTexture = new Texture(Gdx.files.internal("pirate/button_playAgain.png"));
		playAgainButtonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		playAgainButton = new TextureRegion[3];
		for (int x = 0; x < playAgainButton.length; x++) {
			playAgainButton[x] = new TextureRegion(playAgainButtonTexture, x * 562, 0, 562, 554);
			playAgainButton[x].flip(false, true);
		}

		gameTitleTexture = new Texture(Gdx.files.internal("pirate/game_title.png"));
		gameTitleTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		gameTitle = new TextureRegion(gameTitleTexture, 0, 0, 1024, 162);
		gameTitle.flip(false, true);

		tilesTexture = new Texture(Gdx.files.internal("tiles_256_295.png"));
		tilesTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		tiles = new TextureRegion[8];
		for (int x = 0; x < tiles.length; x++) {
			tiles[x] = new TextureRegion(tilesTexture, x * 256, 0, 256, 295);
			tiles[x].flip(false, true);
		}

		objectivesTexture = new Texture(Gdx.files.internal("extraTiles_256_295.png"));
		objectivesTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		objectives = new TextureRegion[6][4];
		for (int y = 0; y < objectives.length; y++) {
			for (int x = 0; x < objectives[0].length; x++) {
				objectives[y][x] = new TextureRegion(objectivesTexture, x * 256, y * 295, 256, 295);
				objectives[y][x].flip(false, true);
			}
		}

		arrow = new TextureRegion(objectivesTexture, 982, 0, 42, 42);
		arrow.flip(false, true);

		playerButtonTexture = new Texture[5];
		playerButtonTexture[0] = new Texture(Gdx.files.internal("pirate/button_player_one.png"));
		playerButtonTexture[0].setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		playerButtonTexture[1] = new Texture(Gdx.files.internal("pirate/button_player_two.png"));
		playerButtonTexture[1].setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		playerButtonTexture[2] = new Texture(Gdx.files.internal("pirate/button_player_three.png"));
		playerButtonTexture[2].setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		playerButtonTexture[3] = new Texture(Gdx.files.internal("pirate/button_player_four.png"));
		playerButtonTexture[3].setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		playerButtonTexture[4] = new Texture(Gdx.files.internal("pirate/button_player_five.png"));
		playerButtonTexture[4].setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		playerButton = new TextureRegion[5][3];
		for (int y = 0; y < playerButton.length; y++) {
			for (int x = 0; x < playerButton[0].length; x++) {
				playerButton[y][x] = new TextureRegion(playerButtonTexture[y], x * 437, 0, 437, 444);
				playerButton[y][x].flip(false, true);
			}
		}

		gameHudTexture = new Texture(Gdx.files.internal("pirate/hud_2.png"));
		gameHudTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		gameHud = new TextureRegion(gameHudTexture, 0, 0, 800, 124);
		gameHud.flip(false, true);

		gameInfoTexture = new Texture(Gdx.files.internal("pirate/hud_info.png"));
		gameInfoTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		gameInfo = new TextureRegion(gameInfoTexture, 0, 0, 283, 450);
		gameInfo.flip(false, true);
		// 9-slice: left/top corner 49 px, right corner 47 px, bottom 49 px; flipped Y matches gameInfo.
		hudInfoSlice = new NineSlice(gameInfoTexture, 0, 0, 283, 450, 49, 47, 49, 49, true);

		tilesOverlayTexture = new Texture(Gdx.files.internal("tiles_overlay_256_295.png"));
		tilesOverlayTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		tilesOverlay = new TextureRegion[6];
		for (int x = 0; x < tilesOverlay.length; x++) {
			tilesOverlay[x] = new TextureRegion(tilesOverlayTexture, x * 256, 0, 256, 295);
			tilesOverlay[x].flip(false, true);
		}

		hudAskTexture = new Texture(Gdx.files.internal("pirate/hud_ask.png"));
		hudAskTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		hudAsk = new TextureRegion(hudAskTexture, 0, 0, 1379, 370);
		hudAsk.flip(false, true);

		scrollTexture = new Texture(Gdx.files.internal("pirate/scroll_note.png"));
		scrollTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		scroll = new TextureRegion(scrollTexture, 0, 0, 1000, 554);
		scroll.flip(false, true);

		starTexture = new Texture(Gdx.files.internal("pirate/star.png"));
		starTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		star = new TextureRegion(starTexture, 0, 0, 439, 419);
		star.flip(false, true);

		scrollWonTexture = new Texture(Gdx.files.internal("pirate/scroll_note_won.png"));
		scrollWonTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		scrollWon = new TextureRegion(scrollWonTexture, 0, 0, 700, 771);
		scrollWon.flip(false, true);

		treasureTexture = new Texture(Gdx.files.internal("pirate/treasure.png"));
		treasureTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		treasure = new TextureRegion(treasureTexture, 0, 0, 634, 484);
		treasure.flip(false, true);

		mouseCursorTexture = new Texture(Gdx.files.internal("pirate/cursor.png"));
		mouseCursorTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		mouseCursor = new TextureRegion(mouseCursorTexture, 0, 0, 493, 691);
		mouseCursor.flip(false, true);

		animalsTexture = new Texture(Gdx.files.internal("animals.png"));
		animalsTexture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		animals = new TextureRegion[4][4];
		for (int x = 0; x < animals[0].length; x++) {
			animals[0][x] = new TextureRegion(animalsTexture, x * 60, 0, 60, 50);
			animals[0][x].flip(false, true);
			animals[1][x] = new TextureRegion(animalsTexture, x * 50, 50, 50, 50);
			animals[1][x].flip(false, true);
			int mirrored = 3 - x;
			animals[2][mirrored] = new TextureRegion(animalsTexture, x * 64, 100, 64, 64);
			animals[2][mirrored].flip(false, true);
			animals[3][mirrored] = new TextureRegion(animalsTexture, x * 64, 164, 64, 64);
			animals[3][mirrored].flip(false, true);
		}

		font40 = new BitmapFont(Gdx.files.internal("pirate/fonts/pirate40.fnt"), Gdx.files.internal("pirate/fonts/pirate40.png"), true);
		font20 = new BitmapFont(Gdx.files.internal("pirate/fonts/pirate20.fnt"), Gdx.files.internal("pirate/fonts/pirate20.png"), true);
		font15 = new BitmapFont(Gdx.files.internal("pirate/fonts/pirate15.fnt"), Gdx.files.internal("pirate/fonts/pirate15.png"), true);
		font25 = new BitmapFont(Gdx.files.internal("pirate/fonts/pirate25.fnt"), Gdx.files.internal("pirate/fonts/pirate25.png"), true);
		font30 = new BitmapFont(Gdx.files.internal("pirate/fonts/pirate30.fnt"), Gdx.files.internal("pirate/fonts/pirate30.png"), true);
	}

	public static void dispose() {
		backgroundTexture.dispose();
		mirrorTexture.dispose();
		leftButtonTexture.dispose();
		rightButtonTexture.dispose();
		quitButtonTexture.dispose();
		yesButtonTexture.dispose();
		playButtonTexture.dispose();
		helpButtonTexture.dispose();
		ruleButtonTexture.dispose();
		treasureButtonTexture.dispose();
		tutorialButtonTexture.dispose();
		playAgainButtonTexture.dispose();
		gameTitleTexture.dispose();
		gameInfoTexture.dispose();
		tilesTexture.dispose();
		objectivesTexture.dispose();
		gameHudTexture.dispose();
		tilesOverlayTexture.dispose();
		hudAskTexture.dispose();
		scrollTexture.dispose();
		scrollWonTexture.dispose();
		starTexture.dispose();
		treasureTexture.dispose();
		mouseCursorTexture.dispose();
		animalsTexture.dispose();
		for (Texture texture : playerButtonTexture) {
			texture.dispose();
		}

		font40.dispose();
		font30.dispose();
		font25.dispose();
		font20.dispose();
		font15.dispose();
//        click.dispose();
	}

}

