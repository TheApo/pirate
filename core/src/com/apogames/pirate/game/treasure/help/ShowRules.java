package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.Tile;
import com.apogames.pirate.game.treasure.enums.ExtraObjective;
import com.apogames.pirate.game.treasure.enums.HelpShow;
import com.apogames.pirate.game.treasure.enums.TileColor;

import java.util.ArrayList;
import java.util.HashSet;

public class ShowRules {

    private boolean visible;

    private ArrayList<TileColor> colors;
    private ArrayList<ExtraObjective> objectives;

    private HelpShow currentShow;

    public ShowRules() {
        this.currentShow = HelpShow.NOTICE;
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

        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null) {
                    if (level[y][x].getColor() != null && level[y][x].getColor() != TileColor.BLACK) {
                        hashSetColor.add(level[y][x].getColor());
                    }
                    if (level[y][x].getObjective() != null && level[y][x].getObjective() != ExtraObjective.RED_PANDA && level[y][x].getObjective() != ExtraObjective.BEARS) {
                        hashSetObjectives.add(level[y][x].getObjective());
                    }
                }
            }
        }
        this.colors = new ArrayList<>(hashSetColor);
        this.objectives = new ArrayList<>(hashSetObjectives);
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

    private void showRules(MainPanel mainPanel) {
        StringBuilder s = new StringBuilder("Regeln");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 30, Constants.COLOR_BLACK, AssetLoader.font40, DrawString.MIDDLE, false, false);

        s = new StringBuilder("Ziel des Spiel ist es,");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 117, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);

        s = new StringBuilder("einen Schatz zu finden.");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 137, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);


        s = new StringBuilder("Leider gibt es nur ein Problem,");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 177, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("niemand weiss GENAU, wo er liegt.");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 197, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("Jeder kennt nur seinen Hinweis.");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 217, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        mainPanel.spriteBatch.draw(AssetLoader.ruleButton[0], Constants.GAME_WIDTH/2f + 210, 220, 30, 30);

        s = new StringBuilder("Die Piraten sind nacheinander dran");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 257, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("und koennen entweder");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 277, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("1.) Einen anderen Piraten befragen,");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 317, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("ob dort der Schatz liegen kann?");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 337, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("Falls ja, wird ein Kreis in seiner");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 367, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("Farbe auf das Feld gezeichnet.");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 387, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("Falls nein, wird ein Viereck in seiner");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 417, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("Farbe auf das Feld gezeichnet UND");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 437, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("der fragende Pirat muss auch ein Feld,");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 457, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("welches es nicht sein kann, bekannt geben.");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 477, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        s = new StringBuilder("2.) Behaupten, wo der Schatz liegt!");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 517, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        mainPanel.spriteBatch.draw(AssetLoader.treasureButton[0], Constants.GAME_WIDTH/2f + 210, 520, 30, 30);

        s = new StringBuilder("Wenn es korrekt ist, hat der Pirat gewonnen!");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 547, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        s = new StringBuilder("Ansonsten ist der Pirat aus dem Spiel");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 577, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("und beantwortet nur noch Fragen automatisch.");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 602, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }

    private void showNotice(MainPanel mainPanel) {
        StringBuilder s = new StringBuilder(Constants.STRING_NOTICE);
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 30, Constants.COLOR_BLACK, AssetLoader.font40, DrawString.MIDDLE, false, false);

        s = new StringBuilder(Constants.STRING_DIFFERENT_RULE);
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, 117, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        int startY = 180;
        s = new StringBuilder("Der Schatz liegt in einem der");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("zwei folgenden Untergruende:");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("WATER, FOREST, MOUNTAIN, GRAS, DESERT");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 5; i++) {
            mainPanel.spriteBatch.draw(AssetLoader.tiles[i], Constants.GAME_WIDTH/2f - 75 + i * 30, startY + 70, 30, 295f / 256f * 30);
        }

        startY = 280;
        s = new StringBuilder("Der Schatz liegt in einem Umkreis von");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("bis zu 1 Feld von einem Untergrund");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("WATER, FOREST, MOUNTAIN, GRAS, DESERT");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 5; i++) {
            mainPanel.spriteBatch.draw(AssetLoader.tiles[i], Constants.GAME_WIDTH/2f - 75 + i * 30, startY + 70, 30, 295f / 256f * 30);
        }

        startY = 380;
        s = new StringBuilder("Der Schatz liegt in einem Umkreis von");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("bis zu 2 Feldern von einem Habitat");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("BEAR oder REDPANDA");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 2; i++) {
            mainPanel.spriteBatch.draw(AssetLoader.objectives[5][i + 2], Constants.GAME_WIDTH/2f - 70 + i * 105, startY + 70, 35, 295f / 256f * 35);
        }

        startY = 480;
        s = new StringBuilder("Der Schatz liegt in einem Umkreis von");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("bis zu 2 Feldern von einem Objekt");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder();
        for (ExtraObjective objective: this.objectives) {
            s.append(objective.name()).append(", ");
        }
        if (s.length() > 0) {
            s = new StringBuilder(s.substring(0, s.length() - 2));
        }
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        int i = 0;
        for (ExtraObjective objective: this.objectives) {
            mainPanel.spriteBatch.draw(AssetLoader.objectives[0][objective.getAssetNumber()], Constants.GAME_WIDTH/2f - 45 + i * 30, startY + 70, 30, 295f / 256f * 30);
            i += 1;
        }

        startY = 580;
        s = new StringBuilder("Der Schatz liegt in einem Umkreis von");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder("bis zu 3 Feldern von einer Farbe");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        s = new StringBuilder();
        for (TileColor color: this.colors) {
            s.append(color.name()).append(", ");
        }
        if (s.length() > 0) {
            s = new StringBuilder(s.substring(0, s.length() - 2));
        }
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        for (i = 0; i < this.colors.size(); i++) {
            mainPanel.spriteBatch.draw(AssetLoader.objectives[this.colors.get(i).getAssetNumber()][0], Constants.GAME_WIDTH/2f - this.colors.size() * 35 + i * 70, startY + 70, 30, 295f / 256f * 30);
            mainPanel.spriteBatch.draw(AssetLoader.objectives[this.colors.get(i).getAssetNumber()][1], Constants.GAME_WIDTH/2f - this.colors.size() * 35 + 30 + i * 70, startY + 70, 30, 295f / 256f * 30);
        }

        startY = 710;
        s = new StringBuilder("Wenn die Levelschwierigkeitsstufe schwer ist,");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        s = new StringBuilder("dann gibt es jeden Hinweis auch als NICHT Variante,");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 20, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        s = new StringBuilder("z.B. Der Schatz liegt NICHT in einem Umkreis von ...");
        mainPanel.drawString(s.toString(), Constants.GAME_WIDTH/2f, startY + 40, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

    }
}
