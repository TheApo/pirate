package com.apogames.pirate.game.treasure;

import com.apogames.pirate.game.treasure.enums.Background;
import com.apogames.pirate.game.treasure.enums.ExtraObjective;
import com.apogames.pirate.game.treasure.enums.TileColor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;

public class Rule {

    private ArrayList<Background> backgrounds;
    private TileColor color;
    private ExtraObjective objective;
    private int distance;
    private boolean not = false;
    private String text;
    private String[] textSplit;
    private boolean[][] solution;
    private boolean out = false;

    public Rule(ArrayList<Background> backgrounds, boolean not) {
        this.not = not;
        String add = "";
        if (this.not) {
            add = "NICHT ";
        }
        this.backgrounds = backgrounds;
        this.distance = 0;
        this.text = "Der Schatz ist "+add+"auf einem der folgenden Untergruende: ";
        for (Background background : backgrounds) {
            this.text += background.name() + " oder ";
        }
        this.text = this.text.substring(0, this.text.length() - 6);
    }

    public Rule(Background background, boolean not) {
        this.not = not;
        String add = "";
        if (this.not) {
            add = "NICHT ";
        }
        this.backgrounds = new ArrayList<>();
        this.backgrounds.add(background);
        this.text = "Der Schatz liegt "+add+"in einem Umkreis von bis zu 1 Feld vom "+background.name()+" Untergrund.";
        this.distance = 1;
    }

    public Rule(ExtraObjective objective, TileColor color, int distance, boolean not) {
        this.not = not;
        this.color = color;
        this.objective = objective;
        this.distance = distance;

        String add = "";
        if (this.not) {
            add = "NICHT ";
        }
        if (this.objective == null) {
            this.text = "Der Schatz liegt "+add+"in einem Umkreis von bis zu "+this.distance+" Feldern von den "+this.color.name()+" Objekten.";
        } else if (this.objective == ExtraObjective.BEARS || this.objective == ExtraObjective.RED_PANDA) {
            this.text = "Der Schatz liegt "+add+"in einem Umkreis von bis zu "+this.distance+" Feldern von einem "+this.objective.name()+" Habitat.";
        } else {
            this.text = "Der Schatz liegt "+add+"in einem Umkreis von bis zu "+this.distance+" Feldern von einem "+this.objective.name()+" Objekt.";
        }
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public ArrayList<Background> getBackgrounds() {
        return backgrounds;
    }

    public TileColor getColor() {
        return color;
    }

    public ExtraObjective getObjective() {
        return objective;
    }

    public int getDistance() {
        return distance;
    }

    public String getText() {
        return text;
    }

    public String[] getTextSplit(GlyphLayout glyphLayout, BitmapFont font) {
        if (this.textSplit == null) {
            glyphLayout.setText(font, text);
            if (glyphLayout.width > 600) {
                int indexSpace = this.text.indexOf(" ");
                String curText = this.text.substring(0, indexSpace);
                glyphLayout.setText(font, curText);
                while (glyphLayout.width < 500) {
                    indexSpace = this.text.indexOf(" ", indexSpace + 1);
                    curText = this.text.substring(0, indexSpace);
                    glyphLayout.setText(font, curText);
                }
                this.textSplit = new String[2];
                this.textSplit[0] = curText;
                this.textSplit[1] = this.text.substring(indexSpace + 1);
            } else {
                this.textSplit = new String[1];
                this.textSplit[0] = this.text;
            }
        }
        return this.textSplit;
    }

    public void createSolution(Tile[][] level) {
        this.solution = new boolean[level.length][level[0].length];

        if (this.objective == null && this.color == null) {
            if (this.backgrounds.size() == 1) {
                this.findSolutionForOneBackground(level);
            } else {
                this.findSolutionForMoreBackgrounds(level);
            }
        } else if (this.objective != null) {
            this.findSolutionForObjective(level);
        } else {
            this.findSolutionForColor(level);
        }
    }

    private void findSolutionForObjective(Tile[][] level) {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && level[y][x].getObjective() == this.objective) {
                    this.solution[y][x] = true;
                    this.fillSolutionWithDistanceFrom(level, x, y);
                }
            }
        }
        if (this.not) {
            this.invertSolution(level);
        }
    }

    private void findSolutionForColor(Tile[][] level) {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && level[y][x].getColor() == this.color) {
                    this.solution[y][x] = true;
                    this.fillSolutionWithDistanceFrom(level, x, y);
                }
            }
        }
        if (this.not) {
            this.invertSolution(level);
        }
    }

    private void fillSolutionWithDistanceFrom(Tile[][] level, int x, int y) {
        int[][] distanceArray = new int[level.length][level[0].length];
        for (int curY = 0; curY < level.length; curY++) {
            for (int curX = 0; curX < level[0].length; curX++) {
                distanceArray[curY][curX] = -1;
                if (x == curX && curY == y) {
                    distanceArray[curY][curX] = 0;
                }
            }
        }

        this.fillSolutionWithDistanceFrom(level, x, y, 0, distanceArray);

        for (int curY = 0; curY < level.length; curY++) {
            for (int curX = 0; curX < level[0].length; curX++) {
                if (distanceArray[curY][curX] > 0) {
                    this.solution[curY][curX] = true;
                }
            }
        }
    }

    private void fillSolutionWithDistanceFrom(Tile[][] level, int x, int y, int distance, int[][] distanceArray) {
        if ((distanceArray[y][x] > 0 && distanceArray[y][x] < distance) || distance > this.distance) {
            return;
        }
        distanceArray[y][x] = distance;
        if (hasNeighbor(x - 1, y, level)) {
            this.fillSolutionWithDistanceFrom(level, x - 1, y, distance + 1, distanceArray);
        }
        if (hasNeighbor(x + 1, y, level)) {
            this.fillSolutionWithDistanceFrom(level, x + 1, y, distance + 1, distanceArray);
        }
        if (hasNeighbor(x, y + 1, level)) {
            this.fillSolutionWithDistanceFrom(level, x, y + 1, distance + 1, distanceArray);
        }
        if (hasNeighbor(x, y - 1, level)) {
            this.fillSolutionWithDistanceFrom(level, x, y - 1, distance + 1, distanceArray);
        }
        if (hasNeighbor(x + 1, y - 1, level)) {
            this.fillSolutionWithDistanceFrom(level, x + 1, y - 1, distance + 1, distanceArray);
        }
        if (hasNeighbor(x - 1, y + 1, level)) {
            this.fillSolutionWithDistanceFrom(level, x - 1, y + 1, distance + 1, distanceArray);
        }
    }

    private void findSolutionForMoreBackgrounds(Tile[][] level) {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                for (Background background : this.backgrounds) {
                    if (level[y][x] != null && level[y][x].getBackground() == background) {
                        this.solution[y][x] = true;
                        break;
                    }
                }
            }
        }
        if (this.not) {
            this.invertSolution(level);
        }
    }

    private void findSolutionForOneBackground(Tile[][] level) {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                Background background = this.backgrounds.get(0);
                if (level[y][x] != null && level[y][x].getBackground() == background) {
                    this.solution[y][x] = true;

                    if (hasNeighborOtherBackground(x - 1, y, level, level[y][x].getBackground())) {
                        this.solution[y][x - 1] = true;
                    }
                    if (hasNeighborOtherBackground(x + 1, y, level, level[y][x].getBackground())) {
                        this.solution[y][x + 1] = true;
                    }
                    if (hasNeighborOtherBackground(x, y + 1, level, level[y][x].getBackground())) {
                        this.solution[y + 1][x] = true;
                    }
                    if (hasNeighborOtherBackground(x, y - 1, level, level[y][x].getBackground())) {
                        this.solution[y - 1][x] = true;
                    }
                    if (hasNeighborOtherBackground(x + 1, y - 1, level, level[y][x].getBackground())) {
                        this.solution[y - 1][x + 1] = true;
                    }
                    if (hasNeighborOtherBackground(x - 1, y + 1, level, level[y][x].getBackground())) {
                        this.solution[y + 1][x - 1] = true;
                    }
                }
            }
        }
        if (this.not) {
            this.invertSolution(level);
        }
    }

    private void invertSolution(Tile[][] level) {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null) {
                    this.solution[y][x] = !this.solution[y][x];
                }
            }
        }
    }

    private boolean hasNeighbor(int x, int y, Tile[][] level) {
        if (x < 0 || y < 0 || x >= level[0].length || y >= level.length) {
            return false;
        }
        return level[y][x] != null;
    }

    private boolean hasNeighborOtherBackground(int x, int y, Tile[][] level, Background background) {
        if (x < 0 || y < 0 || x >= level[0].length || y >= level.length) {
            return false;
        }
        return level[y][x] != null && level[y][x].getBackground() != background;
    }

    public boolean[][] getSolution(Tile[][] level) {
        if (this.solution == null) {
            this.createSolution(level);
        }

        return solution;
    }

}
