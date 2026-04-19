/*
 * Copyright (c) 2005-2017 Dirk Aporius <dirk.aporius@gmail.com>
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
 *
 */

package com.apogames.pirate.backend;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.entity.ApoButton;
import com.apogames.pirate.game.MainPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

/**
 * The type Sequentially thinking screen model.
 */
public abstract class SequentiallyThinkingScreenModel extends ScreenModel {

    public static final int STATE_MENU = 10;

    public static final String FUNCTION_RELOAD = "main_reload";
    public static final String FUNCTION_MENU = "main_menu";
    public static final String FUNCTION_PLAY = "main_play";

    public static final int FPS_THINK = 100;
    public static final int WAIT_TIME_THINK = (int) (1000.0 / (double) FPS_THINK);

    private int deltaTime = 0;

    private boolean bLeft;
    private boolean bUp;
    private boolean bDown;
    private boolean bRight;

    private float dPadX = 5;
    private float dPadY = 5;

    public int state = 0;

    private final ArrayList<ApoButton> modelButtons = new ArrayList<ApoButton>();

    private GameProperties gameProperties;

    private final ArrayList<String> solvedLevels;

    protected String[] levels;

    protected String hint;

    public SequentiallyThinkingScreenModel(MainPanel mainPanel) {
        super(mainPanel);

        dPadX = 5;
        dPadY = getHeight() - 50 - AssetLoader.dpad.getRegionHeight() * getScale();

        this.solvedLevels = new ArrayList<String>();
    }

    public void initAnalyseButtons() {
        getMainPanel().getButtonByFunction(FUNCTION_MENU).init();
        getMainPanel().getButtonByFunction(FUNCTION_PLAY).init();
        getMainPanel().getButtonByFunction(FUNCTION_RELOAD).init();
    }

    public ArrayList<String> getSolvedLevels() {
        return solvedLevels;
    }

    public void addSolvedLevel(final String solvedLevel) {
        for (String solved : solvedLevels) {
            if (solved.equals(solvedLevel)) {
                return;
            }
        }
        this.solvedLevels.add(solvedLevel);
        this.gameProperties.writeLevel();
    }

    public void loadProperties() {
        if (this.gameProperties == null) {
            this.gameProperties = getGameProperties();
        }
        if (this.gameProperties != null) {
            this.solvedLevels.clear();
            this.gameProperties.readLevel();
        }
    }

    public GameProperties getGameProperties() {
        return gameProperties;
    }

    public void setGameProperties(GameProperties gameProperties) {
        this.gameProperties = gameProperties;
    }

    public ArrayList<ApoButton> getModelButtons() {
        return modelButtons;
    }

    public void setMenuButtonVisible(final boolean visible) {
        for (int i = 0; i < this.modelButtons.size(); i++) {
            this.modelButtons.get(i).setVisible(visible);
            this.modelButtons.get(i).setSolved(false);
            if ((visible) && (isLevelSolved(i))) {
                this.modelButtons.get(i).setSolved(true);
            }
        }
        if (!visible) {
            setNeededButtonsVisible();
        } else {
            setButtonsVisible(false);
        }
    }

    public void setNeededButtonsVisible() {
        if (Constants.IS_ANDROID) {
            setButtonsVisible(true);
        }
    }

    public void setButtonsVisible(boolean visible) {

    }

    public boolean isLevelSolved(int levelInt) {
        if (levels == null) {
            return false;
        }
        if ((levelInt < 0) || (levelInt >= levels.length)) {
            return false;
        }
        for (String solvedLevel : getSolvedLevels()) {
            if (solvedLevel.equals(levels[levelInt])) {
                return true;
            }
        }
        return false;
    }

    protected void quit() {
        if (state == STATE_MENU) {
            setLevelWinButtonVisible(false);
            initAnalyseButtons();
            super.quit();
        } else {
            if (modelButtons.size() > 0) {
                setMyMenu();
            } else {
                initAnalyseButtons();
                super.quit();
            }
        }
    }

    public void setMyMenu() {
        readAndCreateNewLevel(true);
        state = STATE_MENU;
        setMenuButtonVisible(true);
    }

    public void readAndCreateNewLevel(boolean bMenu) {

    }

    @Override
    public final void think(float delta) {
        int deltaInMs = (int) (1000 * delta);
        deltaTime += deltaInMs;

        if (deltaTime > 4 * WAIT_TIME_THINK) deltaTime = WAIT_TIME_THINK;

        while (deltaTime >= WAIT_TIME_THINK) {
            deltaTime -= WAIT_TIME_THINK;
            doThink(WAIT_TIME_THINK);
        }
    }

    protected abstract void doThink(float delta);

    protected int getHeight() {
        return 0;
    }

    protected float getScale() {
        return 1;
    }

    public boolean isbLeft() {
        return bLeft && Constants.IS_ANDROID;
    }

    public boolean isbUp() {
        return bUp && Constants.IS_ANDROID;
    }

    public boolean isbDown() {
        return bDown && Constants.IS_ANDROID;
    }

    public boolean isbRight() {
        return bRight && Constants.IS_ANDROID;
    }

    private void resetDirection() {
        bLeft = false;
        bRight = false;
        bUp = false;
        bDown = false;
    }

    protected void setLevelWinButtonVisible(boolean visible) {
        getMainPanel().getButtonByFunction(FUNCTION_MENU).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_PLAY).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_RELOAD).setVisible(visible);
    }

    public void mouseMoved(int x, int y) {
        super.mouseMoved(x, y);
        for (int i = 0; i < this.modelButtons.size(); i++) {
            if (this.modelButtons.get(i).getMove(x, y)) {
                break;
            }
        }
    }

    public void mousePressed(int x, int y, boolean isRightButton) {
        super.mousePressed(x, y, isRightButton);
        for (int i = 0; i < this.modelButtons.size(); i++) {
            if (this.modelButtons.get(i).getPressed(x, y)) {
                break;
            }
        }
        if (Constants.IS_ANDROID) {
            if ((x > dPadX) && (x < dPadX + 30 * getScale()) &&
                    (y > dPadY + 22 * getScale()) && (y < dPadY + 52 * getScale())) {
                bLeft = true;
            } else if ((x > dPadX + 45 * getScale()) && (x < dPadX + 75 * getScale()) &&
                    (y > dPadY + 22 * getScale()) && (y < dPadY + 52 * getScale())) {
                bRight = true;
            } else if ((x > dPadX + 22 * getScale()) && (x < dPadX + 52 * getScale()) &&
                    (y > dPadY) && (y < dPadY + 37 * getScale())) {
                bUp = true;
            } else if ((x > dPadX + 22 * getScale()) && (x < dPadX + 52 * getScale()) &&
                    (y > dPadY + 37 * getScale()) && (y < dPadY + 76 * getScale())) {
                bDown = true;
            }
        }
    }

    public void mouseButtonReleased(int x, int y, boolean isRightButton) {
        super.mouseButtonReleased(x, y, isRightButton);
        for (ApoButton modelButton : this.modelButtons) {
            if (modelButton.getReleased(x, y)) {
                String function = modelButton.getFunction();
                mouseButtonFunction(function);
            }
        }
        resetDirection();
    }

    public void renderDPad() {
        if ((Constants.IS_ANDROID) && (state != STATE_MENU)) {
            getMainPanel().spriteBatch.begin();
            getMainPanel().spriteBatch.enableBlending();
            getMainPanel().spriteBatch.draw(AssetLoader.dpad, dPadX, dPadY, AssetLoader.dpad.getRegionWidth() * getScale(), AssetLoader.dpad.getRegionHeight() * getScale());
            getMainPanel().spriteBatch.end();
        }
    }

    public void render() {

    }

    public void renderMenu() {

    }
}
