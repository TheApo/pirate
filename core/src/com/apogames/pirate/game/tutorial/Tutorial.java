package com.apogames.pirate.game.tutorial;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.backend.SequentiallyThinkingScreenModel;
import com.apogames.pirate.common.Localization;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.menu.Menu;
import com.apogames.pirate.game.treasure.Treasure;
import com.apogames.pirate.game.treasure.enums.ExtraObjective;
import com.apogames.pirate.game.treasure.enums.Status;
import com.apogames.pirate.game.treasure.enums.TileColor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Tutorial extends SequentiallyThinkingScreenModel {

    private SequentiallyThinkingScreenModel currentScreenModel;
    private Treasure treasure;
    private Menu menu;
    private TutorialStep currentStep;

    public Tutorial(MainPanel game) {
        super(game);
    }

    @Override
    public void setNeededButtonsVisible() {
    }

    @Override
    public void init() {
        this.currentStep = TutorialStep.GAME_START;
    }

    public void setAllScreenModels(Menu menu, Treasure treasure) {
        this.menu = menu;
        this.treasure = treasure;
        this.changeToTreasure();
        this.currentStep = TutorialStep.GAME_START;
    }

    private void changeToTreasure() {
        this.changeCurrentScreen(this.treasure);
        this.treasure.setSettings(this.menu.getPlayers(), this.menu.getDifficulty(), this.menu.getSize());
        this.treasure.setTutorial(true);
    }

    private void changeCurrentScreen(SequentiallyThinkingScreenModel screenModel) {
        this.getMainPanel().setButtonsInvisible();
        screenModel.setNeededButtonsVisible();
        screenModel.init();
        this.currentScreenModel = screenModel;
    }

    public int getSize() {
        return 1;
    }

    @Override
    public void keyButtonReleased(int keyCode, char character) {
        super.keyButtonReleased(keyCode, character);
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE
                || this.currentStep == TutorialStep.GAME_ASKING
                || this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO
                || this.currentStep == TutorialStep.GAME_ASKING_INCORRECT
                || this.currentStep == TutorialStep.GAME_RULES_BACKGROUND) {
            this.treasure.mouseMoved(mouseX, mouseY);
        }
        if (this.currentStep == TutorialStep.MENU_START) {
            this.menu.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    public void mouseButtonReleased(int mouseX, int mouseY, boolean isRightButton) {
        if (this.currentStep == TutorialStep.GAME_OVERVIEW
                || this.currentStep == TutorialStep.GAME_START
                || this.currentStep == TutorialStep.GAME_ASKING_CORRECT
                || this.currentStep == TutorialStep.GAME_ASKING_INCORRECT_2
                || this.currentStep == TutorialStep.GAME_SHOW_NOTICE
                || this.currentStep == TutorialStep.GAME_RULES_BACKGROUND
                || this.currentStep == TutorialStep.MENU_START) {
            this.currentStep = this.currentStep.getNextStep();
            if (this.currentStep == TutorialStep.MENU_PLAYERS) {
                this.quit();
            }
        } else if (this.currentStep == TutorialStep.GAME_ASKING || this.currentStep == TutorialStep.GAME_ASKING_INCORRECT) {
            this.treasure.mouseButtonReleased(mouseX, mouseY, isRightButton);
        }

        if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO) {
            this.treasure.mouseButtonReleasedTutorial();
        }
    }

    @Override
    public void mousePressed(int x, int y, boolean isRightButton) {
        if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO || this.currentStep == TutorialStep.GAME_ASKING) {
            this.treasure.mousePressed(x, y, isRightButton);
        }
        if (this.currentStep == TutorialStep.MENU_START) {
            this.menu.mousePressed(x, y, isRightButton);
        }
    }

    @Override
    public void mouseDragged(int x, int y, boolean isRightButton) {
        if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO || this.currentStep == TutorialStep.GAME_ASKING) {
            this.treasure.mouseDragged(x, y, isRightButton);
        }
    }

    @Override
    public void mouseWheelChanged(int changed) {
        if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO || this.currentStep == TutorialStep.GAME_ASKING) {
            this.treasure.mouseWheelChanged(changed);
        }
    }

    @Override
    public void mouseButtonFunction(String function) {
        super.mouseButtonFunction(function);
        if (this.currentScreenModel.equals(this.menu) && function.equals(Menu.FUNCTION_QUIT)) {
            this.quit();
        }

        if (this.currentScreenModel.equals(this.treasure)) {
            if (function.equals(Treasure.FUNCTION_BACK)) {
                this.quit();
            }

            if (this.currentStep == TutorialStep.GAME_ASKING) {
                if (function.equals(Treasure.FUNCTION_PLAYER_ONE) || function.equals(Treasure.FUNCTION_PLAYER_ONE_HUD)
                        || function.equals(Treasure.FUNCTION_PLAYER_TWO) || function.equals(Treasure.FUNCTION_PLAYER_TWO_HUD)) {
                    this.treasure.mouseButtonFunction(function);
                }
            } else if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO) {
                this.treasure.mouseButtonFunction(function);
            }
        }

        if (this.currentScreenModel.equals(this.menu) && this.currentStep == TutorialStep.MENU_START) {
            this.menu.mouseButtonFunction(function);
        }
    }

    @Override
    protected void quit() {
        if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO) {
            this.currentStep = this.currentStep.getNextStep();
            this.changeCurrentScreen(this.menu);
        } else {
            this.getMainPanel().changeToMenu();
        }
    }

    @Override
    public void doThink(float delta) {
        if (this.currentScreenModel.equals(this.treasure)
                && (this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO
                || this.currentStep == TutorialStep.GAME_ASKING
                || this.currentStep == TutorialStep.GAME_ASKING_INCORRECT
                || this.currentStep == TutorialStep.GAME_ASKING_OTHER)) {
            this.treasure.doThink(delta);
            if ((this.currentStep == TutorialStep.GAME_ASKING || this.currentStep == TutorialStep.GAME_ASKING_INCORRECT)
                    && !this.treasure.isCurrentPlayerHuman() && !this.treasure.isInformationShown()) {
                if (this.currentStep == TutorialStep.GAME_ASKING) {
                    this.currentStep = TutorialStep.GAME_ASKING_CORRECT;
                } else {
                    this.currentStep = TutorialStep.GAME_ASKING_INCORRECT_2;
                }
            } else if (this.currentStep == TutorialStep.GAME_ASKING && this.treasure.getCurrentStatus() == Status.SET_NOT) {
                this.currentStep = TutorialStep.GAME_ASKING_INCORRECT;
            } else if (this.currentStep == TutorialStep.GAME_ASKING_OTHER && this.treasure.isCurrentPlayerHuman()) {
                this.currentStep = TutorialStep.GAME_RULES_BACKGROUND;
            }
        }
    }

    @Override
    public void render() {
        if (this.currentScreenModel.equals(this.menu)) {
            this.menu.render();
        } else if (this.currentScreenModel.equals(this.treasure)) {
            this.treasure.render();
        }

        this.getMainPanel().spriteBatch.begin();
        if (this.currentStep == TutorialStep.GAME_START) {
            this.showTutorialStep(Localization.get("tutorial.step.one").split(";"));
            this.getMainPanel().spriteBatch.draw(AssetLoader.treasure, Constants.GAME_WIDTH / 2f - 112, 520, 225, 150);
        }

        if (this.currentStep == TutorialStep.GAME_OVERVIEW) {
            this.showTutorialStep(Localization.get("tutorial.step.two").split(";"));
        }

        if (this.currentStep == TutorialStep.GAME_ASKING && this.treasure.getCurrentStatus() != Status.ASK) {
            this.showTutorialStepHud(Localization.get("tutorial.step.three").split(";"));
        }

        if (this.currentStep == TutorialStep.GAME_ASKING_CORRECT) {
            this.showTutorialStep(Localization.get("tutorial.step.three_correct").split(";"));
        }

        if (this.currentStep == TutorialStep.GAME_ASKING_INCORRECT) {
            this.showTutorialStepHud(Localization.get("tutorial.step.three_incorrect").split(";"));
        }

        if (this.currentStep == TutorialStep.GAME_ASKING_INCORRECT_2) {
            this.showTutorialStep(Localization.get("tutorial.step.three_incorrect_2").split(";"));
        }

        if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE) {
            this.showTutorialStep(Localization.get("tutorial.step.x").split(";"));
            float buttonX = Constants.GAME_WIDTH / 2f - 32;
            this.getMainPanel().spriteBatch.draw(AssetLoader.helpButton[0], buttonX, 205, 64, 61);
            this.getMainPanel().spriteBatch.draw(AssetLoader.ruleButton[0], buttonX, 330, 64, 61);
            this.getMainPanel().spriteBatch.draw(AssetLoader.treasureButton[0], buttonX, 485, 64, 61);
        }

        if (this.currentStep == TutorialStep.GAME_SHOW_NOTICE_INFO) {
            this.showTutorialStepHud(Localization.get("tutorial.step.x_2").split(";"));
        }

        if (this.currentStep == TutorialStep.MENU_START) {
            this.showTutorialStepHud(Localization.get("tutorial.step.menu_start").split(";"), AssetLoader.font20);
        }

        if (this.currentStep == TutorialStep.GAME_RULES_BACKGROUND) {
            this.showRules();
        }

        this.getMainPanel().spriteBatch.end();
    }

    private void showTutorialStep(String[] tutorialSteps) {
        int width = 1000;
        this.getMainPanel().spriteBatch.draw(AssetLoader.scrollWon, Constants.GAME_WIDTH/2f - width/2f, 10, width, Constants.GAME_HEIGHT - 110);
        this.getMainPanel().drawString(Localization.get("tutorial.title"), Constants.GAME_WIDTH/2f, 30, Constants.COLOR_BLACK, AssetLoader.font40, DrawString.MIDDLE, false, false);
        for (int i = 0; i < tutorialSteps.length; i++) {
            this.getMainPanel().drawString(tutorialSteps[i], Constants.GAME_WIDTH/2f, 117 + i * 25, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        }
    }

    private void showTutorialStepHud(String[] tutorialSteps) {
        this.showTutorialStepHud(tutorialSteps, AssetLoader.font25);
    }

    private void showTutorialStepHud(String[] tutorialSteps, BitmapFont font) {
        int width = 1000;
        this.getMainPanel().spriteBatch.draw(AssetLoader.gameHud, Constants.GAME_WIDTH/2f - width/2f, 0, width, 35 + tutorialSteps.length * 25);
        for (int i = 0; i < tutorialSteps.length; i++) {
            this.getMainPanel().drawString(tutorialSteps[i], Constants.GAME_WIDTH/2f, 17 + i * 23, Constants.COLOR_WHITE, font, DrawString.MIDDLE, false, false);
        }
    }

    private void showRules() {
        int width = 1000;
        this.getMainPanel().spriteBatch.draw(AssetLoader.scrollWon, Constants.GAME_WIDTH/2f - width/2f, 10, width, Constants.GAME_HEIGHT - 110);
        this.getMainPanel().drawString(Localization.get("rules.different_rule_types"), Constants.GAME_WIDTH/2f, 117, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);

        String[] rules = Localization.get("tutorial.step.background_rules").split(";");

        int startY = 180;
        this.getMainPanel().drawString(rules[0], Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[1], Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[2], Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        for (int i = 0; i < 5; i++) {
            this.getMainPanel().spriteBatch.draw(AssetLoader.tiles[i], Constants.GAME_WIDTH/2f - 75 + i * 30, startY + 76, 30, 295f / 256f * 30);
        }

        startY = 280;
        this.getMainPanel().drawString(rules[3], Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[4], Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[5], Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        for (int i = 0; i < 5; i++) {
            this.getMainPanel().spriteBatch.draw(AssetLoader.tiles[i], Constants.GAME_WIDTH/2f - 75 + i * 30, startY + 76, 30, 295f / 256f * 30);
        }

        startY = 380;
        this.getMainPanel().drawString(rules[6], Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[7], Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[8], Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        for (int i = 0; i < 2; i++) {
            this.getMainPanel().spriteBatch.draw(AssetLoader.objectives[5][i + 2], Constants.GAME_WIDTH/2f - 70 + i * 105, startY + 76, 35, 295f / 256f * 35);
        }

        startY = 480;
        this.getMainPanel().drawString(rules[9], Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[10], Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);

        StringBuilder s = new StringBuilder();
        for (ExtraObjective objective : this.treasure.getShowRules().getObjectives()) {
            s.append(objective.name()).append(", ");
        }
        if (s.length() > 0) {
            s = new StringBuilder(s.substring(0, s.length() - 2));
        }
        this.getMainPanel().drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        int idx = 0;
        for (ExtraObjective objective : this.treasure.getShowRules().getObjectives()) {
            this.getMainPanel().spriteBatch.draw(AssetLoader.objectives[0][objective.getAssetNumber()], Constants.GAME_WIDTH/2f - 45 + idx * 30, startY + 76, 30, 295f / 256f * 30);
            idx += 1;
        }

        startY = 580;
        this.getMainPanel().drawString(rules[11], Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[12], Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);

        s = new StringBuilder();
        for (TileColor color : this.treasure.getShowRules().getColors()) {
            s.append(color.name()).append(", ");
        }
        if (s.length() > 0) {
            s = new StringBuilder(s.substring(0, s.length() - 2));
        }
        this.getMainPanel().drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        for (int i = 0; i < this.treasure.getShowRules().getColors().size(); i++) {
            TileColor color = this.treasure.getShowRules().getColors().get(i);
            this.getMainPanel().spriteBatch.draw(AssetLoader.objectives[color.getAssetNumber()][0], Constants.GAME_WIDTH/2f - this.treasure.getShowRules().getColors().size() * 35 + i * 70, startY + 76, 30, 295f / 256f * 30);
            this.getMainPanel().spriteBatch.draw(AssetLoader.objectives[color.getAssetNumber()][1], Constants.GAME_WIDTH/2f - this.treasure.getShowRules().getColors().size() * 35 + 30 + i * 70, startY + 76, 30, 295f / 256f * 30);
        }

        startY = 710;
        this.getMainPanel().drawString(rules[13], Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[14], Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        this.getMainPanel().drawString(rules[15], Constants.GAME_WIDTH/2f, startY + 40, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
    }

    @Override
    public void drawOverlay() {
    }

    @Override
    public void dispose() {
    }
}
