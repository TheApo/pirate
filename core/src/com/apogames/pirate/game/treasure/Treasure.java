package com.apogames.pirate.game.treasure;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.backend.SequentiallyThinkingScreenModel;
import com.apogames.pirate.common.Localization;
import com.apogames.pirate.entity.ApoButton;
import com.apogames.pirate.entity.ApoEntity;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.ai.Information;
import com.apogames.pirate.game.treasure.ai.Perfect;
import com.apogames.pirate.game.treasure.ai.PiratePlayer;
import com.apogames.pirate.game.treasure.ai.Result;
import com.apogames.pirate.game.treasure.create.LevelCreate;
import com.apogames.pirate.game.treasure.create.RuleCreate;
import com.apogames.pirate.game.treasure.enums.Status;
import com.apogames.pirate.game.treasure.help.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Treasure extends SequentiallyThinkingScreenModel {

    private final int STATISTIC_RUNS = 0;
    private static final int NO_ACTION_MESSAGE_TIME = 30000;
    private static final int BLINK_TIME = 800;

    public static final String FUNCTION_BACK = "treasure_X";
    public static final String FUNCTION_HELP = "HELP";
    public static final String FUNCTION_TREASURE = "TREASURE";
    public static final String FUNCTION_RULES = "RULES";

    public static final String FUNCTION_YES = "yes";
    public static final String FUNCTION_NO = "no";

    public static final String FUNCTION_PLAYER_ONE = "one";
    public static final String FUNCTION_PLAYER_TWO = "two";
    public static final String FUNCTION_PLAYER_THREE = "three";
    public static final String FUNCTION_PLAYER_FOUR = "four";
    public static final String FUNCTION_PLAYER_FIVE = "five";

    public static final String FUNCTION_PLAYER_ONE_HUD = "hud_one";
    public static final String FUNCTION_PLAYER_TWO_HUD = "hud_two";
    public static final String FUNCTION_PLAYER_THREE_HUD = "hud_three";
    public static final String FUNCTION_PLAYER_FOUR_HUD = "hud_four";
    public static final String FUNCTION_PLAYER_FIVE_HUD = "hud_five";

    public static final String FUNCTION_RULES_LEFT = "rules_left";
    public static final String FUNCTION_RULES_RIGHT = "rules_right";
    public static final String FUNCTION_PLAY_AGAIN = "play_again";
    public static final String FUNCTION_NEXT_PLAYER = "next_player";
    public static final String FUNCTION_GAMELOG = "gamelog";
    public static final String FUNCTION_HINTS = "hints";

    private final boolean[] keys = new boolean[256];

    private final Statistics statistics = new Statistics(STATISTIC_RUNS);

    private final PiratePlayer humanPlayerSupport = new Perfect();

    private int playerCount = 3;

	private int difficulty = 0;
    private int size = 0;

    private Tile[][] level;

    private Rule[] rules;

    private int currentPlayer = 0;
    private int wonPlayer = 0;

    private final LevelView view = new LevelView();
    private final TileGridRenderer tileGridRenderer = new TileGridRenderer(view);
    private final RightHudRenderer rightHudRenderer = new RightHudRenderer();

    private int oldMouseX = 0;
    private int oldMouseY = 0;

    private boolean tutorial = false;
    private boolean isPressed = false;
    private boolean dragged = false;

    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private int currentRound = 1;
    private int mapPressStartX = -1;
    private int mapPressStartY = -1;
    private static final int DESELECT_CLICK_THRESHOLD_PX = 5;
    private static final int MAP_DRAG_THRESHOLD_PX = 8;

    private static final int HINTS_BUTTON_HEIGHT = 61;
    private final GameLogPanel gameLogPanel = new GameLogPanel(10, 80, 566, 550);
    private final HintsPanel hintsPanel = new HintsPanel(
            10,
            Constants.GAME_HEIGHT - HINTS_BUTTON_HEIGHT - 10 - 10 - 550,
            1144, 550);
    private final ApoEntity hintsButtonRegion = new ApoEntity(
            10, Constants.GAME_HEIGHT - HINTS_BUTTON_HEIGHT - 10, 64, HINTS_BUTTON_HEIGHT);

    private int noActionTime = 0;
    private int blinkTime = 0;
    private boolean blink = false;

    private int countRules = 0;

    private Status currentStatus;
    private Status oldStatus;

    private int curOverLevelX = -1;
    private int curOverLevelY = -1;
    private int curPickLevelX = -1;
    private int curPickLevelY = -1;

    private PiratePlayer[] players;

    private final ShowTimeInformation information = new ShowTimeInformation();
    private final ShowMoreInformation moreInformation = new ShowMoreInformation();
    private final ShowRules showRules = new ShowRules();
    private final ScrollToTile scrollToTile = new ScrollToTile(this);

    private String curTask;

    private boolean showWon = true;
    private Result lastResult;

    private Rule[] allPossibleRules = null;

    private boolean showBackQuestion = false;

    public Treasure(final MainPanel game) {
        super(game);
    }

	public void setSettings(ArrayList<PiratePlayer> players, int difficulty, int size) {
        int playerCount = 3;
        for (int i = 3; i < players.size(); i++) {
                if (players.get(i) != null) {
                    playerCount += 1;
                }
        }
		this.playerCount = playerCount;
		this.difficulty = difficulty;
        this.size = size;

        this.newLevel();

        countRules = 0;
        this.newRules();

        this.players = new PiratePlayer[playerCount];
        for (int i = 0; i < playerCount; i++) {
            this.players[i] = players.get(i);
            this.players[i].init();
        }
        this.humanPlayerSupport.init();

        showAskButtons(false);
        updateHintsButtonVisibility();
	}

    private void newRules() {
        boolean hard = this.difficulty > 0;
        this.rules = new Rule[this.playerCount];
        for (int i = 0; i < this.rules.length; i++) {
            Rule randomRule = RuleCreate.createRandomRule(this.level, hard);
            while (isInRules(randomRule, i)) {
                randomRule = RuleCreate.createRandomRule(this.level, hard);
            }
            this.rules[i] = randomRule;
        }

        this.countRules += 1;
        if (this.countRules > 1000) {
            this.newLevel();
        } else {
            int count = 0;
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    boolean correct = true;
                    for (Rule rule : this.rules) {
                        if (!rule.getSolution(this.level)[y][x]) {
                            correct = false;
                            break;
                        }
                    }
                    if (correct) {
                        count += 1;
                    }
                }
            }
            if (count != 1) {
                newRules();
            }
        }

        this.allPossibleRules = null;

        this.setStatus(Status.SET_QUESTION);
        for (Tile[] tiles : this.level) {
            for (int x = 0; x < this.level[0].length; x++) {
                if (tiles[x] != null) {
                    tiles[x].resetGuessed();
                    tiles[x].setEntities(view.tileSize());
                }
            }
        }

        if (this.players != null) {
            for (PiratePlayer player : this.players) {
                player.init();
            }
            this.humanPlayerSupport.init();
        }

        this.showRules.setLevel(this.level);
        if (this.showRules.isVisible()) {
            changeVisibilityForShowRules();
        }
        getMainPanel().getButtonByFunction(FUNCTION_PLAY_AGAIN).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_NEXT_PLAYER).setVisible(false);
        this.changeShowBackQuestion(false);
        this.noActionTime = 0;
        this.blinkTime = 0;
        this.blink = false;
    }

    private boolean isInRules(Rule randomRule, int maxI) {
        for (int i = 0; i < maxI; i++) {
            boolean sameRule = true;
            boolean[][] solutionRule = this.rules[i].getSolution(this.level);
            boolean[][] solutionRandomRule = randomRule.getSolution(this.level);
            for (int y = 0; y < this.level.length; y++) {
                for (int x = 0; x < this.level[0].length; x++) {
                    if (solutionRule[y][x] != solutionRandomRule[y][x]) {
                        sameRule = false;
                        break;
                    }
                }
            }
            if (sameRule) {
                return true;
            }
        }
        return false;
    }

    public void setNeededButtonsVisible() {
    	getMainPanel().getButtonByFunction(FUNCTION_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_HELP).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_RULES).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_GAMELOG).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_HINTS).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_PLAY_AGAIN).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_NEXT_PLAYER).setVisible(false);
        showAskButtons(false);
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    private void repositionAskTopButtons() {
        String askText = Localization.get("hud.ask_which_pirate");
        Constants.glyphLayout.setText(AssetLoader.font20, askText);
        float textEnd = Constants.GAME_WIDTH / 2f + Constants.glyphLayout.width / 2f;
        int buttonWidth = 50;
        int gap = 15;
        int startX = (int)(textEnd + 20);
        String[] funcs = {FUNCTION_PLAYER_ONE, FUNCTION_PLAYER_TWO, FUNCTION_PLAYER_THREE, FUNCTION_PLAYER_FOUR, FUNCTION_PLAYER_FIVE};
        for (int i = 0; i < funcs.length; i++) {
            getMainPanel().getButtonByFunction(funcs[i]).setX(startX + i * (buttonWidth + gap));
        }
    }

    public void setTutorial(boolean tutorial) {
        this.tutorial = tutorial;
    }

    public boolean isCurrentPlayerHuman() {
        return this.players[this.currentPlayer].isHuman();
    }

    public boolean isInformationShown() {
        return this.moreInformation.getTime() > 0;
    }

    public ShowRules getShowRules() {
        return showRules;
    }

    public void showAskButtons(boolean visible) {
        if (visible) {
            repositionAskTopButtons();
        }
        getMainPanel().getButtonByFunction(FUNCTION_PLAYER_ONE).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_PLAYER_ONE_HUD).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_PLAYER_TWO).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_PLAYER_TWO_HUD).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_PLAYER_THREE).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_PLAYER_THREE_HUD).setVisible(visible);
        if (this.rules != null && this.rules.length > 3) {
            getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FOUR).setVisible(visible);
            getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FOUR_HUD).setVisible(visible);
            if (this.rules.length > 4) {
                getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FIVE).setVisible(visible);
                getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FIVE_HUD).setVisible(visible);
            }
        }

        switch (this.currentPlayer) {
            case 0 : getMainPanel().getButtonByFunction(FUNCTION_PLAYER_ONE).setVisible(false); getMainPanel().getButtonByFunction(FUNCTION_PLAYER_ONE_HUD).setVisible(false); break;
            case 1 : getMainPanel().getButtonByFunction(FUNCTION_PLAYER_TWO).setVisible(false); getMainPanel().getButtonByFunction(FUNCTION_PLAYER_TWO_HUD).setVisible(false); break;
            case 2 : getMainPanel().getButtonByFunction(FUNCTION_PLAYER_THREE).setVisible(false); getMainPanel().getButtonByFunction(FUNCTION_PLAYER_THREE_HUD).setVisible(false); break;
            case 3 : getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FOUR).setVisible(false); getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FOUR_HUD).setVisible(false); break;
            case 4 : getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FIVE).setVisible(false); getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FIVE_HUD).setVisible(false); break;
        }
    }
    
    @Override
    public void init() {
        if (getGameProperties() == null) {
        	setGameProperties(new TreasurePreferences(this));
            loadProperties();
        }
        LevelCreate createLevel = new LevelCreate(this.size, this.difficulty > 0);
        this.level = createLevel.getLevel();

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        
        setMenuButtonVisible(false);

        this.currentPlayer = 0;
        this.setStatus(Status.SET_QUESTION);

        view.center(this.level);

        this.tutorial = false;
        this.setInformationForStatus();
	}

    public LevelView getView() { return view; }

    public int getChangeX() { return view.getChangeX(); }
    public int getChangeY() { return view.getChangeY(); }
    public void setChangeX(int v) { view.setChangeX(v); }
    public void setChangeY(int v) { view.setChangeY(v); }
    public int getCurTileSize() { return view.getCurTileSize(); }

    @Override
    public void keyPressed(int keyCode, char character) {
        super.keyPressed(keyCode, character);

        keys[keyCode] = true;
    }

    @Override
    public void keyButtonReleased(int keyCode, char character) {
        super.keyButtonReleased(keyCode, character);

        keys[keyCode] = false;

        if (keyCode == Input.Keys.T) {
            this.newLevel();
        }
        if (keyCode == Input.Keys.R) {
            this.countRules = 0;
            this.newRules();
            this.setInformationForStatus();
        }
        if (keyCode == Input.Keys.C) {
            tileGridRenderer.setShowSolution(!tileGridRenderer.isShowSolution());
        }
        if (keyCode == Input.Keys.G) {
            tileGridRenderer.setShowCoords(!tileGridRenderer.isShowCoords());
        }
        if (keyCode == Input.Keys.N) {
            this.nextPlayer(1);
        }
    }

    private void newLevel() {
        LevelCreate createLevel = new LevelCreate(this.size, this.difficulty > 0);
        this.level = createLevel.getLevel();
        this.countRules = 0;
        this.currentPlayer = 0;
        this.currentRound = 1;
        gameLogPanel.clear();
        this.newRules();
        this.setInformationForStatus();
        view.center(this.level);
    }

    private void nextPlayer(int add) {
        int prev = this.currentPlayer;
        this.currentPlayer += add;
        if (this.currentPlayer < 0) {
            this.currentPlayer = this.rules.length - 1;
        } else if (this.currentPlayer >= this.rules.length) {
            this.currentPlayer = 0;
        }
        if (this.currentStatus != Status.WON) {
            if (add > 0 && this.currentPlayer < prev) {
                this.currentRound += 1;
            }
            this.setStatus(Status.SET_QUESTION);
            this.setInformationForStatus();
            if (this.rules[this.currentPlayer].isOut()) {
                this.nextPlayer(1);
            }
        }
    }

    public void mouseMoved(int mouseX, int mouseY) {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
        if (this.currentStatus != Status.ASK) {
            Vector2 mousePosition = view.screenToTile(this.level, mouseX, mouseY);
            if (mousePosition != null) {
                this.curOverLevelX = (int)(mousePosition.x);
                this.curOverLevelY = (int)(mousePosition.y);
            } else {
                this.curOverLevelX = -1;
                this.curOverLevelY = -1;
            }
        }
        gameLogPanel.updateHover(mouseX, mouseY);
    }

    private void updateHintsButtonVisibility() {
        ApoButton btn = getMainPanel().getButtonByFunction(FUNCTION_HINTS);
        if (btn == null) {
            return;
        }
        boolean currentIsHuman = this.players != null
                && this.currentPlayer >= 0 && this.currentPlayer < this.players.length
                && this.players[this.currentPlayer] != null
                && this.players[this.currentPlayer].isHuman();
        // Also visible after the game is won so the player can review whether
        // the puzzle was uniquely solvable.
        boolean visible = currentIsHuman || this.currentStatus == Status.WON;
        btn.setVisible(visible);
        if (!visible) {
            hintsPanel.setOpen(false);
        }
    }

    private void refreshHintsRows() {
        hintsPanel.rebuild(this.level, this.rules, this.players,
                getAllPossibleRules(), this.currentPlayer, this.playerCount);
    }

    public void mouseButtonReleasedTutorial() {
        this.isPressed = false;
        this.dragged = false;
        this.curPickLevelX = -1;
        this.curPickLevelY = -1;
    }

    public void mouseButtonReleased(int mouseX, int mouseY, boolean isRightButton) {
        this.noActionTime = 0;
        this.isPressed = false;
        boolean curDragged = this.dragged;
        int mapStartX = this.mapPressStartX;
        int mapStartY = this.mapPressStartY;
        this.dragged = false;
        this.mapPressStartX = -1;
        this.mapPressStartY = -1;
        this.oldMouseX = -1;
        this.oldMouseY = -1;

        if (hintsPanel.onMouseReleased(mouseX, mouseY)) {
            return;
        }
        if (gameLogPanel.onMouseReleased(mouseX, mouseY)) {
            return;
        }

        // Click outside the hints panel (and not on its button) closes it.
        if (hintsPanel.isOpen() && !curDragged && !isRightButton
                && !hintsPanel.isInside(mouseX, mouseY) && !hintsButtonRegion.intersects(mouseX, mouseY)) {
            hintsPanel.setOpen(false);
            return;
        }

        // Short tap on the map clears any pinned log-entry highlight.
        if (mapStartX >= 0 && mapStartY >= 0 && gameLogPanel.getSelectedIndex() >= 0) {
            int dx = mouseX - mapStartX;
            int dy = mouseY - mapStartY;
            if (dx * dx + dy * dy < DESELECT_CLICK_THRESHOLD_PX * DESELECT_CLICK_THRESHOLD_PX) {
                gameLogPanel.clearSelection();
            }
        }

        if (this.currentStatus == Status.WON && !isRightButton) {
            this.showWon = !this.showWon;
            return;
        }

        if (this.showRules.isVisible()) {
            changeVisibilityForShowRules();
            return;
        }

        if (showBackQuestion) {
            return;
        }

        if (this.currentStatus != Status.ASK && !isRightButton && !curDragged && this.players[this.currentPlayer].isHuman()) {
            this.curPickLevelX = -1;
            this.curPickLevelY = -1;

            Vector2 mousePosition = view.screenToTile(this.level, mouseX, mouseY);

            if (mousePosition != null) {
                this.curPickLevelX = (int)(mousePosition.x);
                this.curPickLevelY = (int)(mousePosition.y);
                if (this.currentStatus == Status.SET_QUESTION) {
                    if (this.level[this.curPickLevelY][this.curPickLevelX] != null && !this.level[this.curPickLevelY][this.curPickLevelX].hasIncorrectGuess()) {
                        this.setStatus(Status.ASK);
                        if (this.players[this.currentPlayer].isHuman()) {
                            Information information = new Information(this.currentPlayer, this.playerCount, getAllPossibleRules());
                            Result result = this.humanPlayerSupport.placeGuess(level, this.rules[this.currentPlayer], information);
                            System.out.println("[Logiker] würde Spalte=" + result.getX() + " Zeile=" + result.getY() + " nehmen und Pirat " + (result.getAskPlayer() + 1) + " fragen.");
                        }
                        showAskButtons(true);
                    } else if (this.level[this.curPickLevelY][this.curPickLevelX] != null && this.level[this.curPickLevelY][this.curPickLevelX].hasIncorrectGuess()) {
                        this.setMoreInformationForError(Localization.get("info.cannot_be_here"));
                    }
                } else if (this.currentStatus == Status.SET_NOT) {
                    if (this.level[this.curPickLevelY][this.curPickLevelX] != null &&
                            !this.level[this.curPickLevelY][this.curPickLevelX].hasIncorrectGuess() &&
                            !this.rules[this.currentPlayer].getSolution(this.level)[this.curPickLevelY][this.curPickLevelX]) {
                        this.level[this.curPickLevelY][this.curPickLevelX].getIncorrectGuesses()[this.currentPlayer] = true;
                        gameLogPanel.addEntry(new GameLogEntry(GameLogEntry.Type.PLACE_NOT, this.currentPlayer, -1, this.curPickLevelX, this.curPickLevelY, this.currentRound));
                        setMoreInformation(Status.ASK);
                        this.nextPlayer(1);
                    } else {
                        this.setMoreInformationForError(Localization.get("info.choose_not_here"));
                    }
                } else if (this.currentStatus == Status.TREASURE) {
                    checkForWin();
                }
            }
        } else if (this.currentStatus == Status.ASK && mouseY > 120 && this.players[this.currentPlayer].isHuman()) {
            this.setStatus(Status.SET_QUESTION);
            this.showAskButtons(false);

        } else if (this.curPickLevelX != -1) {
            this.curPickLevelX = -1;
            this.curPickLevelY = -1;
        }
    }

    private void changeVisibilityForShowRules() {
        this.showRules.setVisible(!this.showRules.isVisible());
        this.showRules.setLevel(this.level);
        showButtonsForRules(!this.showRules.isVisible());
    }

    private void checkForWin(int x, int y) {
        boolean win = true;
        for (int i = 0; i < this.rules.length; i++) {
            Rule rule = this.rules[i];
            if (!rule.getSolution(this.level)[y][x]) {
                this.level[y][x].getIncorrectGuesses()[i] = true;
                win = false;
            } else {
                this.level[y][x].getCorrectGuesses()[i] = true;
            }
        }
        gameLogPanel.addEntry(new GameLogEntry(
                win ? GameLogEntry.Type.TREASURE_FOUND : GameLogEntry.Type.TREASURE_WRONG,
                this.currentPlayer, -1, x, y, this.currentRound));
        if (win) {
            getMainPanel().getButtonByFunction(FUNCTION_RULES).setVisible(false);
            getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(false);
            getMainPanel().getButtonByFunction(FUNCTION_PLAY_AGAIN).setVisible(true);
            getMainPanel().getButtonByFunction(FUNCTION_NEXT_PLAYER).setVisible(true);
            this.setStatus(Status.WON);
            showWon = true;
        }
    }

    public void mousePressed(int x, int y, boolean isRightButton) {
        this.noActionTime = 0;
        this.mouseMoved(x, y);
        if (hintsPanel.onMousePressed(x, y) || gameLogPanel.onMousePressed(x, y)) {
            this.isPressed = true;
            this.oldMouseX = x;
            this.oldMouseY = y;
            return;
        }
        this.mapPressStartX = x;
        this.mapPressStartY = y;
        if (((isRightButton || !this.dragged) && !this.isPressed) || this.oldMouseX < 0) {
            this.isPressed = true;
            this.oldMouseX = x;
            this.oldMouseY = y;
        }
    }

    public void mouseDragged(int x, int y, boolean isRightButton) {
        if (!this.isPressed || this.oldMouseX < 0) {
            return;
        }
        int deltaX = x - this.oldMouseX;
        int deltaY = y - this.oldMouseY;

        if (hintsPanel.onMouseDragged(deltaY) || gameLogPanel.onMouseDragged(deltaY)) {
            this.oldMouseX = x;
            this.oldMouseY = y;
            return;
        }

        if (!this.dragged) {
            if (deltaX * deltaX + deltaY * deltaY < MAP_DRAG_THRESHOLD_PX * MAP_DRAG_THRESHOLD_PX) {
                return;
            }
        }

        view.pan(this.level, deltaX, deltaY);
        this.oldMouseX = x;
        this.oldMouseY = y;
        this.dragged = true;
    }

    private void changeShowBackQuestion(boolean showBack) {
        this.showBackQuestion = showBack;

        getMainPanel().getButtonByFunction(FUNCTION_YES).setVisible(showBack);
        getMainPanel().getButtonByFunction(FUNCTION_NO).setVisible(showBack);
    }

    @Override
    public void mouseButtonFunction(String function) {
        super.mouseButtonFunction(function);
        if (showBackQuestion) {
            switch (function) {
                case Treasure.FUNCTION_YES:
                    quit();
                    break;
                case Treasure.FUNCTION_NO:
                    this.changeShowBackQuestion(false);
                    break;
            }
            return;
        }
        switch (function) {
            case Treasure.FUNCTION_BACK:
                if (this.currentStatus == Status.WON) {
                    quit();
                } else {
                    this.changeShowBackQuestion(true);
                }
                break;
            case Treasure.FUNCTION_HELP:
                tileGridRenderer.setShowHelp(!tileGridRenderer.isShowHelp());
                break;
            case Treasure.FUNCTION_PLAYER_ONE:
            case Treasure.FUNCTION_PLAYER_ONE_HUD:
                this.checkPlayerAndSet(0);
                break;
            case Treasure.FUNCTION_PLAYER_TWO:
            case Treasure.FUNCTION_PLAYER_TWO_HUD:
                this.checkPlayerAndSet(1);
                break;
            case Treasure.FUNCTION_PLAYER_THREE:
            case Treasure.FUNCTION_PLAYER_THREE_HUD:
                this.checkPlayerAndSet(2);
                break;
            case Treasure.FUNCTION_PLAYER_FOUR:
            case Treasure.FUNCTION_PLAYER_FOUR_HUD:
                this.checkPlayerAndSet(3);
                break;
            case Treasure.FUNCTION_PLAYER_FIVE:
            case Treasure.FUNCTION_PLAYER_FIVE_HUD:
                this.checkPlayerAndSet(4);
                break;
            case Treasure.FUNCTION_TREASURE:
                showAskButtons(false);
                if (this.currentStatus != Status.TREASURE) {
                    this.oldStatus = this.currentStatus;
                    this.setStatus(Status.TREASURE);
                } else {
                    this.setStatus(oldStatus);
                }
                setInformationForStatus();
                break;
            case Treasure.FUNCTION_RULES:
                changeVisibilityForShowRules();
                break;
            case Treasure.FUNCTION_RULES_LEFT:
                this.showRules.nextShow(-1);
                break;
            case Treasure.FUNCTION_RULES_RIGHT:
                this.showRules.nextShow(1);
                break;
            case Treasure.FUNCTION_PLAY_AGAIN:
                this.newLevel();
                break;
            case Treasure.FUNCTION_NEXT_PLAYER:
                this.nextPlayer(1);
                break;
            case Treasure.FUNCTION_GAMELOG:
                if (gameLogPanel.isOpen()) {
                    gameLogPanel.setOpen(false);
                } else {
                    hintsPanel.setOpen(false);
                    gameLogPanel.setOpen(true);
                }
                break;
            case Treasure.FUNCTION_HINTS:
                if (hintsPanel.isOpen()) {
                    hintsPanel.setOpen(false);
                } else {
                    gameLogPanel.setOpen(false);
                    refreshHintsRows();
                    hintsPanel.setOpen(true);
                }
                break;
        }
    }

    private void showButtonsForRules(boolean visible) {
        getMainPanel().getButtonByFunction(FUNCTION_HELP).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_BACK).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_RULES_LEFT).setVisible(!visible);
        getMainPanel().getButtonByFunction(FUNCTION_RULES_RIGHT).setVisible(!visible);
    }

    private void checkPlayerAndSet(int player) {
        this.showAskButtons(false);
        this.setStatus(Status.SET_QUESTION);
        boolean askYes = this.rules[player].getSolution(this.level)[this.curPickLevelY][this.curPickLevelX];
        gameLogPanel.addEntry(new GameLogEntry(
                askYes ? GameLogEntry.Type.ASK_YES : GameLogEntry.Type.ASK_NO,
                this.currentPlayer, player, this.curPickLevelX, this.curPickLevelY, this.currentRound));
        if (!askYes) {
            this.setStatus(Status.SET_NOT);
            getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(false);
            this.level[this.curPickLevelY][this.curPickLevelX].getIncorrectGuesses()[player] = true;
            this.setInformationForStatus();
            this.setMoreInformation(Status.SET_NOT);

            if (this.players[this.currentPlayer].isHuman()) {
                Information information = new Information(this.currentPlayer, this.playerCount, getAllPossibleRules());
                Result result = this.humanPlayerSupport.placeWrongMarker(level, rules[currentPlayer], information);
                System.out.println("[Logiker] würde den NICHT-Marker auf Spalte=" + result.getX() + " Zeile=" + result.getY() + " setzen.");
            }
        } else {
            getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(true);
            this.level[this.curPickLevelY][this.curPickLevelX].getCorrectGuesses()[player] = true;
            if (this.level[this.curPickLevelY][this.curPickLevelX].hasOnlyCorrectGuess(this.playerCount)) {
                this.checkForWin(this.curPickLevelX, this.curPickLevelY);
            } else {
                this.setMoreInformation(Status.SET_QUESTION);
                this.nextPlayer(1);
            }
        }
    }

    private void setMoreInformation(Status status) {
        int time = noHumanLeft() ? 1 : Constants.WAIT_TIME_MORE;

        this.setPositionForMoreInformation();
        if (status == Status.SET_QUESTION) {
            this.moreInformation.setTimer(time, Localization.get("info.could_be_treasure"));
        } else if (status == Status.SET_NOT) {
            this.moreInformation.setTimer(time, Localization.get("info.no_treasure_here"));
        } else if (status == Status.ASK) {
            this.moreInformation.setTimer(time, Localization.get("info.surely_no_treasure"));
        }
    }

    private void setPositionForMoreInformation() {
        int halfTile = view.tileSize() / 2;
        int px = view.tileScreenX(this.curPickLevelX, this.curPickLevelY) + halfTile;
        int py = view.tileScreenY(this.curPickLevelY) - 70;
        if (py < Constants.GAME_HEIGHT - 200) {
            py += (int) view.tileHeight() + 70;
        }
        if (px < 300) {
            px = 300;
        } else if (px + 300 > Constants.GAME_WIDTH) {
            px = Constants.GAME_WIDTH - 300;
        }
        this.moreInformation.setPosition(px, py);
    }

    private void setMoreInformationForError(String error) {
        this.setPositionForMoreInformation();
        this.moreInformation.setTimer(Constants.WAIT_TIME_MORE,error);
    }

    private void setInformationForStatus() {
        if (this.currentStatus == Status.SET_NOT) {
            this.information.setTimer(Constants.WAIT_TIME_LONGER, Localization.get("info.no_treasure_here_arr"), Localization.get("info.place_marker_hint"));
            logLogicianSuggestionForNotMarker();
        } else if (this.currentStatus == Status.TREASURE) {
            this.information.setTimer(Constants.WAIT_TIME_LONGER, Localization.get("info.where_treasure_hidden"));
        } else {
            this.information.setTimer(Constants.WAIT_TIME, Localization.format("task.players_turn", this.currentPlayer + 1));
            logLogicianSuggestionForGuess();
        }
    }

    private boolean isCurrentHumanReady() {
        return this.players != null
                && this.rules != null
                && this.currentPlayer >= 0
                && this.currentPlayer < this.players.length
                && this.players[this.currentPlayer] != null
                && this.players[this.currentPlayer].isHuman();
    }

    private void logLogicianSuggestionForGuess() {
        if (!isCurrentHumanReady()) {
            return;
        }
        Information info = new Information(this.currentPlayer, this.playerCount, getAllPossibleRules());
        Result result = this.humanPlayerSupport.placeGuess(this.level, this.rules[this.currentPlayer], info);
        if (result.isWantToSolve()) {
            System.out.println("[Logiker] weiss: der Schatz liegt auf Spalte=" + result.getX() + " Zeile=" + result.getY() + ". Jetzt behaupten!");
        } else {
            System.out.println("[Logiker] würde Spalte=" + result.getX() + " Zeile=" + result.getY() + " nehmen und Pirat " + (result.getAskPlayer() + 1) + " fragen.");
        }
    }

    private void logLogicianSuggestionForNotMarker() {
        if (!isCurrentHumanReady()) {
            return;
        }
        Information info = new Information(this.currentPlayer, this.playerCount, getAllPossibleRules());
        Result result = this.humanPlayerSupport.placeWrongMarker(this.level, this.rules[this.currentPlayer], info);
        System.out.println("[Logiker] würde den NICHT-Marker auf Spalte=" + result.getX() + " Zeile=" + result.getY() + " setzen.");
    }

    private void setStatus(Status status) {
        this.noActionTime = 0;
        this.currentStatus = status;
        getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(true);
        showAskButtons(false);
        updateHintsButtonVisibility();
        if (this.currentStatus == Status.SET_QUESTION) {
            this.curTask = Localization.get("task.find_treasure");
        } else if (this.currentStatus == Status.ASK) {
            this.curTask = Localization.get("task.ask_whom");
            getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(false);
            showAskButtons(true);
        } else if (this.currentStatus == Status.SET_NOT) {
            this.curTask = Localization.get("task.place_marker");
            getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(false);
        } else if (this.currentStatus == Status.TREASURE) {
            this.curTask = Localization.get("task.where_treasure");
        } else if (this.currentStatus == Status.WON) {
            getMainPanel().getButtonByFunction(FUNCTION_TREASURE).setVisible(false);
            this.wonPlayer = this.currentPlayer + 1;
            this.curTask = Localization.format("task.pirate_wins", this.wonPlayer);
        }
    }

    public void mouseWheelChanged(int changed) {
        if (hintsPanel.onMouseWheel(this.lastMouseX, this.lastMouseY, changed)) return;
        if (gameLogPanel.onMouseWheel(this.lastMouseX, this.lastMouseY, changed)) return;
        view.zoom(changed);
    }

    @Override
    protected void quit() {
		getMainPanel().changeToMenu();
    }

    @Override
    public void doThink(float delta) {
        if (this.currentStatus == Status.WON && this.noHumanInGame()) {
            if (!this.statistics.isFinished()) {
                System.out.println("Und gewonnen hat "+this.currentPlayer+" an Position "+this.curPickLevelX+" "+this.curPickLevelY);
                this.statistics.addWinner(this.currentPlayer);
                this.newLevel();
                return;
            } else {
                if (this.statistics.isShowAnalyse()) {
                    this.statistics.addWinner(this.currentPlayer);
                    System.out.println();
                    System.out.println("Auswertung:");
                    for (int i = 0; i < this.statistics.getWinners().length; i++) {
                        System.out.println("Pirat " + (i + 1) + ": " + this.statistics.getWinners()[i]);
                    }
                    System.out.println();
                    this.statistics.setShowAnalyse(false);
                }
            }
        }
        if (this.information.getTime() > 0) {
            this.information.doThink(delta);
        }
        if (this.scrollToTile.getGoalX() >= 0) {
            this.scrollToTile.doThink(delta);
            if (this.currentStatus != Status.WON && !this.players[this.currentPlayer].isHuman() && this.scrollToTile.getGoalX() < 0) {
                if (this.currentStatus == Status.SET_QUESTION) {
//                    GridPoint2 point = getPositionForPlayer(this.lastResult.getAskPlayer());
//                    this.scrollToTile.moveMouseToPosition(point.x, point.y);
//                    this.setStatus(Status.ASK);
                    this.checkPlayerAndSet(this.lastResult.getAskPlayer());
                } else if (this.currentStatus == Status.TREASURE) {
                    this.checkForWin();
                } else if (this.currentStatus == Status.ASK) {
                    this.checkPlayerAndSet(this.lastResult.getAskPlayer());
                } else if (this.currentStatus == Status.SET_NOT) {
                    if (this.level[this.curPickLevelY][this.curPickLevelX] != null) {
                        this.level[this.curPickLevelY][this.curPickLevelX].getIncorrectGuesses()[this.currentPlayer] = true;
                        gameLogPanel.addEntry(new GameLogEntry(GameLogEntry.Type.PLACE_NOT, this.currentPlayer, -1, this.curPickLevelX, this.curPickLevelY, this.currentRound));
                    }
                    this.setMoreInformation(Status.ASK);
                    this.nextPlayer(1);
                }
            }
        } else if (this.moreInformation.getTime() > 0) {
            this.moreInformation.doThink(delta);
        } else if (this.currentStatus != Status.WON && !this.players[this.currentPlayer].isHuman()) {
            Information information = new Information(this.currentPlayer, this.playerCount, getAllPossibleRules());
            if (this.currentStatus == Status.SET_QUESTION) {
                Result result = this.players[this.currentPlayer].placeGuess(level, this.rules[this.currentPlayer], information);
                this.lastResult = result;
                this.curPickLevelX = result.getX();
                this.curPickLevelY = result.getY();
                if (result.isWantToSolve()) {
                    this.setStatus(Status.TREASURE);
                }
                this.scrollToTile.scrollToPosition(this.curPickLevelX, this.curPickLevelY);
            } else if (this.currentStatus == Status.SET_NOT) {
                Result result = this.players[this.currentPlayer].placeWrongMarker(level, this.rules[this.currentPlayer], information);
                this.curPickLevelX = result.getX();
                this.curPickLevelY = result.getY();
                this.scrollToTile.scrollToPosition(this.curPickLevelX, this.curPickLevelY);
            }
        } else if (this.currentStatus != Status.WON) {
            thinkBlink(delta);
        }

        for (Tile[] tiles : this.level) {
            for (int x = 0; x < this.level[0].length; x++) {
                if (tiles[x] != null) {
                    tiles[x].doThink(delta, view.tileSize());
                }
            }
        }

        int value = (int)(-2 - view.getCurTileSize() * 0.75f);
        if (this.keys[Input.Keys.LEFT] || this.keys[Input.Keys.A]) {
            view.pan(this.level, -value, 0);
        } else if (this.keys[Input.Keys.RIGHT] || this.keys[Input.Keys.D]) {
            view.pan(this.level, value, 0);
        }

        if (this.keys[Input.Keys.UP] || this.keys[Input.Keys.W]) {
            view.pan(this.level, 0, -value);
        } else if (this.keys[Input.Keys.DOWN] || this.keys[Input.Keys.S]) {
            view.pan(this.level, 0, value);
        }
    }

    private void thinkBlink(float delta) {
        this.noActionTime = (int)(this.noActionTime + delta);
        if (this.noActionTime > NO_ACTION_MESSAGE_TIME) {
            this.blinkTime = (int)(this.blinkTime + delta);
            if (this.blinkTime > BLINK_TIME) {
                this.blinkTime = 0;
                this.blink = !this.blink;
            }
        }
    }

    private void checkForWin() {
        this.checkForWin(this.curPickLevelX, this.curPickLevelY);
        if (this.currentStatus != Status.WON) {
            this.rules[this.currentPlayer].setOut(true);
            this.nextPlayer(+1);
        } else {
            for (int i = 0; i < this.playerCount; i++) {
                this.level[this.curPickLevelY][this.curPickLevelX].getCorrectGuesses()[i] = true;
            }
        }
    }

    private GridPoint2 getPositionForPlayer(int askPlayer) {
        GridPoint2 point = new GridPoint2(-1, -1);

        ApoButton button = getMainPanel().getButtonByFunction(FUNCTION_PLAYER_ONE);
        if (askPlayer == 1) {
            button = getMainPanel().getButtonByFunction(FUNCTION_PLAYER_TWO);
        } else if (askPlayer == 2) {
            button = getMainPanel().getButtonByFunction(FUNCTION_PLAYER_THREE);
        } else if (askPlayer == 3) {
            button = getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FOUR);
        } else if (askPlayer == 4) {
            button = getMainPanel().getButtonByFunction(FUNCTION_PLAYER_FIVE);
        }

        point.x = (int)button.getXMiddle();
        point.y = (int)(button.getY() + button.getHeight()/2f);

        return point;
    }

    private Rule[] getAllPossibleRules() {
        if (this.allPossibleRules == null) {
            this.allPossibleRules = RuleCreate.getAllRules(this.level, this.difficulty > 0);
        }
        return this.allPossibleRules;
    }

    public boolean noHumanLeft() {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].isHuman() && !this.rules[i].isOut()) {
                return false;
            }
        }
        return true;
    }

    public boolean noHumanInGame() {
        for (PiratePlayer player : this.players) {
            if (player.isHuman()) {
                return false;
            }
        }
        return true;
    }

	@Override
    public void render() {
        tileGridRenderer.render(getMainPanel(), this.level, this.rules,
                this.currentStatus, this.currentPlayer, this.playerCount);

        getMainPanel().spriteBatch.begin();
        if (this.curOverLevelX >= 0) {
            tileGridRenderer.drawTileOverlay(getMainPanel(), this.curOverLevelX, this.curOverLevelY, 5);
        }
        GameLogEntry highlightEntry = getCurrentLogHighlight();
        if (highlightEntry != null) {
            tileGridRenderer.drawTileOverlay(getMainPanel(),
                    highlightEntry.getTileX(), highlightEntry.getTileY(), 5);
        }

        int bottomHudWidth = 735;
        int bottomHudX = (Constants.GAME_WIDTH - bottomHudWidth) / 2;
        this.getMainPanel().spriteBatch.draw(AssetLoader.gameHud, bottomHudX, Constants.GAME_HEIGHT - 90 - 5, bottomHudWidth, 90);
        if (!this.showRules.isVisible()) {
            boolean blinkHidesStar = this.noActionTime >= NO_ACTION_MESSAGE_TIME && this.blink;
            rightHudRenderer.renderSprites(getMainPanel(), this.players, this.playerCount,
                    this.currentPlayer, blinkHidesStar);
        }

        String s;
        String[] textSplit = this.rules[0].getTextSplit(this.getMainPanel().getGlyphLayout(), AssetLoader.font20);
        if (this.currentStatus == Status.WON) {
            textSplit = this.rules[this.currentPlayer].getTextSplit(this.getMainPanel().getGlyphLayout(), AssetLoader.font20);
        }
        for (int i = 0; i < textSplit.length; i++) {
            s = textSplit[i];
            this.getMainPanel().drawString(s, bottomHudX + bottomHudWidth / 2f, Constants.GAME_HEIGHT - 90 + 10 + i * 30, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.MIDDLE, false, false);
        }


        if (this.showRules.isVisible()) {
            this.showRules.render(this.getMainPanel());
        } else {
            s = this.curTask;
            if (this.noActionTime >= NO_ACTION_MESSAGE_TIME && this.blink) {
                this.getMainPanel().drawString(s, Constants.GAME_WIDTH - AssetLoader.gameInfo.getRegionWidth()/2f - 10, Constants.GAME_HEIGHT - AssetLoader.gameInfo.getRegionHeight() - 135 - 60 + 10, Constants.COLOR_RED, AssetLoader.font15, DrawString.MIDDLE, false, false);
            } else {
                this.getMainPanel().drawString(s, Constants.GAME_WIDTH - AssetLoader.gameInfo.getRegionWidth()/2f - 10, Constants.GAME_HEIGHT - AssetLoader.gameInfo.getRegionHeight() - 135 - 60 + 10, Constants.COLOR_WHITE, AssetLoader.font15, DrawString.MIDDLE, false, false);
            }
        }

        if (this.currentStatus == Status.ASK) {
            this.getMainPanel().spriteBatch.draw(AssetLoader.gameHud, 10,10, Constants.GAME_WIDTH - 20, 80);
            s = Localization.get("hud.ask_which_pirate");
            this.getMainPanel().drawString(s, Constants.GAME_WIDTH / 2f, 32, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.MIDDLE, false, false);
        } else if (this.currentStatus == Status.WON && this.showWon) {
            float wonCenterX = Constants.GAME_WIDTH / 2f;
            float wonX = wonCenterX - 350;
            this.getMainPanel().spriteBatch.draw(AssetLoader.scrollWon, wonX, 10);
            this.getMainPanel().spriteBatch.draw(AssetLoader.treasure, wonX + 120, 310, 450, 300);

            s = Localization.get("hud.congratulations");
            this.getMainPanel().drawString(s, wonCenterX, 130, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);

            s = Localization.format("hud.pirate_won", this.wonPlayer);
            this.getMainPanel().drawString(s, wonCenterX, 160, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);

            s = Localization.get("hud.found_treasure");
            this.getMainPanel().drawString(s, wonCenterX, 240, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);


        } else {
            if (!this.tutorial && this.information.getTime() > 0) {
                this.information.render(this.getMainPanel());
            }
            if (this.moreInformation.getTime() > 0) {
                this.moreInformation.render(this.getMainPanel());
                tileGridRenderer.drawTileOverlay(getMainPanel(), this.curPickLevelX, this.curPickLevelY, 5);
            }
        }

        if (this.scrollToTile.isScrollMouse() || this.scrollToTile.getGoalX() >= 0) {
            getMainPanel().spriteBatch.draw(AssetLoader.mouseCursor, this.scrollToTile.getMouseX(), this.scrollToTile.getMouseY(), 30, 45);
        }

        if (this.showBackQuestion) {
            int width = 400;
            int height = 130;
            this.getMainPanel().spriteBatch.draw(AssetLoader.gameHud, Constants.GAME_WIDTH/2f - width/2f,Constants.GAME_HEIGHT/2f - height/2f, width, height);

            s = Localization.get("hud.back_to_menu");
            this.getMainPanel().drawString(s, Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT/2f - 45, Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, false, false);
        }

        for (ApoButton button : this.getMainPanel().getButtons()) {
            button.render(this.getMainPanel(), 0, 0, false);
        }

        gameLogPanel.render(getMainPanel());
        hintsPanel.render(getMainPanel());

		getMainPanel().spriteBatch.end();

        if (!this.showRules.isVisible()) {
            rightHudRenderer.renderOutStrikes(getMainPanel(), this.rules, this.playerCount);
        }

        renderLogHighlightOutline();
        renderCurrentPlayerBorder();
    }

    private static final int LOG_HIGHLIGHT_OUTLINE_WIDTH = 6;

    private GameLogEntry getCurrentLogHighlight() {
        if (!gameLogPanel.isOpen()) return null;
        int idx = gameLogPanel.getHighlightEntryIndex();
        if (idx < 0 || idx >= gameLogPanel.entries().size()) return null;
        return gameLogPanel.entries().get(idx);
    }

    private void renderLogHighlightOutline() {
        GameLogEntry entry = getCurrentLogHighlight();
        if (entry == null) return;
        int tileSize = view.tileSize();
        int ox = view.tileScreenX(entry.getTileX(), entry.getTileY());
        int oy = view.tileScreenY(entry.getTileY());
        float h = view.tileHeight();
        float half = tileSize / 2f;
        float[] vx = {ox + half, ox + tileSize, ox + tileSize, ox + half, ox,          ox};
        float[] vy = {oy,        oy + 0.25f * h, oy + 0.75f * h, oy + h,   oy + 0.75f * h, oy + 0.25f * h};
        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        getMainPanel().getRenderer().setColor(Constants.COLOR_YELLOW[0], Constants.COLOR_YELLOW[1], Constants.COLOR_YELLOW[2], 1f);
        for (int i = 0; i < vx.length; i++) {
            int j = (i + 1) % vx.length;
            getMainPanel().getRenderer().rectLine(vx[i], vy[i], vx[j], vy[j], LOG_HIGHLIGHT_OUTLINE_WIDTH);
        }
        getMainPanel().getRenderer().end();
    }

    private static final int PLAYER_BORDER_WIDTH = 6;

    private void renderCurrentPlayerBorder() {
        if (this.players == null || this.currentPlayer < 0 || this.currentPlayer >= Constants.PLAYER_COLORS.length) {
            return;
        }
        float[] color = Constants.PLAYER_COLORS[this.currentPlayer];
        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        getMainPanel().getRenderer().setColor(color[0], color[1], color[2], 1f);
        getMainPanel().getRenderer().rect(0, 0, Constants.GAME_WIDTH, PLAYER_BORDER_WIDTH);
        getMainPanel().getRenderer().rect(0, Constants.GAME_HEIGHT - PLAYER_BORDER_WIDTH, Constants.GAME_WIDTH, PLAYER_BORDER_WIDTH);
        getMainPanel().getRenderer().rect(0, 0, PLAYER_BORDER_WIDTH, Constants.GAME_HEIGHT);
        getMainPanel().getRenderer().rect(Constants.GAME_WIDTH - PLAYER_BORDER_WIDTH, 0, PLAYER_BORDER_WIDTH, Constants.GAME_HEIGHT);
        getMainPanel().getRenderer().end();
    }

    public void drawOverlay() {
    }

    @Override
    public void dispose() {
    }
}
