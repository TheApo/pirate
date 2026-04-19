package com.apogames.pirate.game.treasure;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.enums.Background;
import com.apogames.pirate.game.treasure.enums.ExtraObjective;
import com.apogames.pirate.game.treasure.enums.TileColor;
import com.badlogic.gdx.math.Polygon;

public class Tile {

    private final Background background;

    private TileColor color;

    private ExtraObjective objective;

    private Polygon polygon;

    private boolean[] correct_guesses;
    private boolean[] incorrect_guesses;

    public Tile() {
        this.background = Background.getRandomBackground();
        if (Math.random() * 100 > 75) {
            this.objective = ExtraObjective.getRandomObjective();
            TileColor currentColor = TileColor.BLACK;
            if (this.objective != ExtraObjective.BEARS && this.objective != ExtraObjective.RED_PANDA) {
                currentColor = TileColor.values()[(int)(Math.random() * (TileColor.values().length - 1))];
            }
            this.color = currentColor;
        } else {
            this.color = null;
            this.objective = null;
        }
    }

    public Tile(Background background) {
        this.background = background;
        this.setObjective(null, null);

        this.resetGuessed();
    }

    public boolean hasIncorrectGuess() {
        for (boolean incorrect_guess : this.incorrect_guesses) {
            if (incorrect_guess)
                return true;
        }
        return false;
    }

    public void resetGuessed() {
        this.correct_guesses = new boolean[Constants.PLAYER_COLORS.length];
        this.incorrect_guesses = new boolean[Constants.PLAYER_COLORS.length];
    }

    public void setObjective(ExtraObjective objective, TileColor tileColor) {
        this.color = tileColor;
        this.objective = objective;
    }

    public boolean hasOnlyCorrectGuess(int maxPlayers) {
        for (int i = 0; i < this.correct_guesses.length && i < maxPlayers; i++) {
            if (!this.correct_guesses[i])
                return false;
        }
        return true;
    }

    public boolean[] getCorrectGuesses() {
        return correct_guesses;
    }

    public boolean[] getIncorrectGuesses() {
        return incorrect_guesses;
    }

    public Background getBackground() {
        return background;
    }

    public TileColor getColor() {
        return color;
    }

    public ExtraObjective getObjective() {
        return objective;
    }

    public Polygon getPolygon(int changeX, int changeY, int tileSize) {
        this.polygon = new Polygon();
        this.polygon.setVertices(new float[] {
                changeX + tileSize/2f, changeY,
                changeX + tileSize, changeY + 0.25f * 295f / 256f *  tileSize,
                changeX + tileSize, changeY + 0.75f * 295f / 256f *  tileSize,
                changeX + tileSize/2f, changeY + 295f / 256f *  tileSize,
                changeX, changeY + 0.75f * 295f / 256f *  tileSize,
                changeX, changeY + 0.25f * 295f / 256f *  tileSize});
        return this.polygon;
    }

    public void render(MainPanel mainPanel, int changeX, int changeY, int tileSize) {
        if (this.background != null) {
            mainPanel.spriteBatch.draw(AssetLoader.tiles[this.background.getAssetNumber()], changeX, changeY, tileSize, 295f / 256f * tileSize);
            if (this.objective != null) {
                mainPanel.spriteBatch.draw(AssetLoader.objectives[this.color.getAssetNumber()][this.objective.getAssetNumber()], changeX, changeY, tileSize, 295f / 256f * tileSize);
            }
        }
    }

    public void renderFilled(MainPanel mainPanel, int changeX, int changeY, int tileSize) {
        for (int i = 0; i < this.incorrect_guesses.length; i++) {
            if (this.incorrect_guesses[i]) {
                mainPanel.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], Constants.COLOR_BLACK[3]);
                mainPanel.getRenderer().rect(changeX + 0.08f * tileSize + 0.15f * tileSize * i, changeY + 295f / 256f * tileSize * (0.3f - 0.02f), tileSize * 0.14f, tileSize * 0.15f);
                mainPanel.getRenderer().setColor(Constants.PLAYER_COLORS[i][0], Constants.PLAYER_COLORS[i][1], Constants.PLAYER_COLORS[i][2], Constants.PLAYER_COLORS[i][3]);
                mainPanel.getRenderer().rect(changeX + 0.1f * tileSize + 0.15f * tileSize * i, changeY + 295f / 256f * tileSize * 0.3f, tileSize * 0.1f, tileSize * 0.1f);
            }
        }
        for (int i = 0; i < this.correct_guesses.length; i++) {
            if (this.correct_guesses[i]) {
                mainPanel.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], Constants.COLOR_BLACK[3]);
                mainPanel.getRenderer().circle(changeX + 0.1f * tileSize + 0.2f * tileSize * i, changeY + 295f / 256f * tileSize * 0.5f, tileSize * 0.095f);

                mainPanel.getRenderer().setColor(Constants.PLAYER_COLORS[i][0], Constants.PLAYER_COLORS[i][1], Constants.PLAYER_COLORS[i][2], Constants.PLAYER_COLORS[i][3]);
                mainPanel.getRenderer().circle(changeX + 0.1f * tileSize + 0.2f * tileSize * i, changeY + 295f / 256f * tileSize * 0.5f, tileSize * 0.065f);
            }
        }
    }
}
