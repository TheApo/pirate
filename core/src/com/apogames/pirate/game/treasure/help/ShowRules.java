package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.common.Localization;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.Tile;
import com.apogames.pirate.game.treasure.enums.ExtraObjective;
import com.apogames.pirate.game.treasure.enums.HelpShow;
import com.apogames.pirate.game.treasure.enums.TileColor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.HashSet;

public class ShowRules {

    private boolean visible;

    private ArrayList<TileColor> colors;
    private ArrayList<ExtraObjective> objectives;
    private ArrayList<ExtraObjective> animals;

    private HelpShow currentShow;

    public ShowRules() {
        this.currentShow = HelpShow.NOTICE;
    }

    public ArrayList<TileColor> getColors() {
        return colors;
    }

    public ArrayList<ExtraObjective> getObjectives() {
        return objectives;
    }

    public ArrayList<ExtraObjective> getAnimals() {
        return animals;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setLevel(Tile[][] level) {
        HashSet<TileColor> hashSetColor = new HashSet<>();
        HashSet<ExtraObjective> hashSetObjectives = new HashSet<>();
        HashSet<ExtraObjective> hashSetAnimals = new HashSet<>();

        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null) {
                    if (level[y][x].getColor() != null && level[y][x].getColor() != TileColor.BLACK) {
                        hashSetColor.add(level[y][x].getColor());
                    }
                    ExtraObjective obj = level[y][x].getObjective();
                    if (obj != null) {
                        if (isAnimal(obj)) {
                            hashSetAnimals.add(obj);
                        } else {
                            hashSetObjectives.add(obj);
                        }
                    }
                }
            }
        }
        this.colors = new ArrayList<>(hashSetColor);
        this.objectives = new ArrayList<>(hashSetObjectives);
        this.animals = new ArrayList<>(hashSetAnimals);
    }

    private static boolean isAnimal(ExtraObjective objective) {
        return objective == ExtraObjective.BEARS || objective == ExtraObjective.RED_PANDA
                || objective == ExtraObjective.WHITE_SHEEP || objective == ExtraObjective.BLACK_SHEEP;
    }

    /** Comma-separated, localised names for a list of animals. */
    public static String joinAnimalNames(ArrayList<ExtraObjective> animals) {
        return joinObjectiveNames(animals);
    }

    /** Comma-separated, localised names for a list of objectives. */
    public static String joinObjectiveNames(ArrayList<ExtraObjective> list) {
        StringBuilder s = new StringBuilder();
        for (ExtraObjective o : list) {
            if (s.length() > 0) s.append(", ");
            s.append(o.localizedName());
        }
        return s.toString();
    }

    /** Comma-separated, localised names for a list of tile colours. */
    public static String joinColorNames(ArrayList<TileColor> list) {
        StringBuilder s = new StringBuilder();
        for (TileColor c : list) {
            if (s.length() > 0) s.append(", ");
            s.append(c.localizedName());
        }
        return s.toString();
    }

    public void nextShow(int add) {
        for (int i = 0; i < HelpShow.values().length; i++) {
            if (this.currentShow == HelpShow.values()[i]) {
                if (i == 0 && add < 0) {
                    this.currentShow = HelpShow.values()[HelpShow.values().length - 1];
                    return;
                }
                if (i == HelpShow.values().length - 1 && add > 0) {
                    this.currentShow = HelpShow.values()[0];
                    return;
                }
                this.currentShow = HelpShow.values()[i + add];
                return;
            }
        }
    }

    public void render(MainPanel mainPanel) {
        int changeX = 700;
        mainPanel.spriteBatch.draw(AssetLoader.scrollWon, Constants.GAME_WIDTH/2f - changeX/2f, 10, changeX, Constants.GAME_HEIGHT - 110);

        if (this.currentShow == HelpShow.RULES) {
            this.showRules(mainPanel);
        } else if (this.currentShow == HelpShow.NOTICE) {
            this.showNotice(mainPanel);
        }
    }

    private void drawLines(MainPanel mainPanel, String[] lines, float x, float startY, int lineHeight, BitmapFont font) {
        for (int i = 0; i < lines.length; i++) {
            mainPanel.drawString(lines[i], x, startY + i * lineHeight, Constants.COLOR_BLACK, font, DrawString.MIDDLE, false, false);
        }
    }

    private void showRules(MainPanel mainPanel) {
        mainPanel.drawString(Localization.get("rules.title"), Constants.GAME_WIDTH/2f, 30, Constants.COLOR_BLACK, AssetLoader.font40, DrawString.MIDDLE, false, false);

        String[] body = Localization.get("rules.body").split(";");
        drawLines(mainPanel, body, Constants.GAME_WIDTH/2f, 117, 20, AssetLoader.font20);
    }

    private void showNotice(MainPanel mainPanel) {
        mainPanel.drawString(Localization.get("rules.notice"), Constants.GAME_WIDTH/2f, 30, Constants.COLOR_BLACK, AssetLoader.font40, DrawString.MIDDLE, false, false);
        mainPanel.drawString(Localization.get("rules.different_rule_types"), Constants.GAME_WIDTH/2f, 117, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        int startY = 180;
        drawLines(mainPanel, Localization.get("rules.notice.backgrounds").split(";"), Constants.GAME_WIDTH/2f, startY, 20, AssetLoader.font20);
        mainPanel.drawString(Localization.get("rules.notice.backgrounds_list"), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 5; i++) {
            mainPanel.spriteBatch.draw(AssetLoader.tiles[i], Constants.GAME_WIDTH/2f - 75 + i * 30, startY + 70, 30, 295f / 256f * 30);
        }

        startY = 280;
        drawLines(mainPanel, Localization.get("rules.notice.near_background").split(";"), Constants.GAME_WIDTH/2f, startY, 20, AssetLoader.font20);
        mainPanel.drawString(Localization.get("rules.notice.backgrounds_list"), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 5; i++) {
            mainPanel.spriteBatch.draw(AssetLoader.tiles[i], Constants.GAME_WIDTH/2f - 75 + i * 30, startY + 70, 30, 295f / 256f * 30);
        }

        startY = 380;
        drawLines(mainPanel, Localization.get("rules.notice.habitat").split(";"), Constants.GAME_WIDTH/2f, startY, 20, AssetLoader.font20);

        mainPanel.drawString(joinAnimalNames(this.animals), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        int animalIdx = 0;
        for (ExtraObjective animal : this.animals) {
            mainPanel.spriteBatch.draw(AssetLoader.animals[animal.getAssetNumber()][0], Constants.GAME_WIDTH/2f - this.animals.size() * 20 + animalIdx * 40, startY + 70, 40, 40);
            animalIdx += 1;
        }

        startY = 480;
        drawLines(mainPanel, Localization.get("rules.notice.objective").split(";"), Constants.GAME_WIDTH/2f, startY, 20, AssetLoader.font20);

        mainPanel.drawString(joinObjectiveNames(this.objectives), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        int i = 0;
        for (ExtraObjective objective: this.objectives) {
            mainPanel.spriteBatch.draw(AssetLoader.objectives[0][objective.getAssetNumber()], Constants.GAME_WIDTH/2f - this.objectives.size() * 15 + i * 30, startY + 70, 30, 295f / 256f * 30);
            i += 1;
        }

        startY = 580;
        drawLines(mainPanel, Localization.get("rules.notice.color").split(";"), Constants.GAME_WIDTH/2f, startY, 20, AssetLoader.font20);

        mainPanel.drawString(joinColorNames(this.colors), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        for (i = 0; i < this.colors.size(); i++) {
            mainPanel.spriteBatch.draw(AssetLoader.objectives[this.colors.get(i).getAssetNumber()][0], Constants.GAME_WIDTH/2f - this.colors.size() * 35 + i * 70, startY + 70, 30, 295f / 256f * 30);
            mainPanel.spriteBatch.draw(AssetLoader.objectives[this.colors.get(i).getAssetNumber()][1], Constants.GAME_WIDTH/2f - this.colors.size() * 35 + 30 + i * 70, startY + 70, 30, 295f / 256f * 30);
        }

        startY = 710;
        drawLines(mainPanel, Localization.get("rules.notice.hard").split(";"), Constants.GAME_WIDTH/2f, startY, 20, AssetLoader.font15);
    }
}
