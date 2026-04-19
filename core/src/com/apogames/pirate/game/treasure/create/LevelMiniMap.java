package com.apogames.pirate.game.treasure.create;

import com.apogames.pirate.game.treasure.Tile;
import com.apogames.pirate.game.treasure.enums.Background;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;

public class LevelMiniMap {

    private Tile[][] level;

    public LevelMiniMap() {
        this.createRandomLevelMiniMap();
    }

    public void createRandomLevelMiniMap() {
        this.level = new Tile[6][6];

        final boolean[][] isPartOfLevel = new boolean[6][6];
        for (int y = 0; y < this.level.length; y++) {
            for (int x = 0; x < this.level[0].length; x++) {
                isPartOfLevel[y][x] = true;
                if ((x * 2 + y < 5) ||
                    (x * 2 + y > 10)) {
                    isPartOfLevel[y][x] = false;
                }
            }
        }

        ArrayList<Integer> possibleTiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            possibleTiles.add(i);
        }
        if (Math.random() * 100 < 90) {
            possibleTiles.remove((int)(Math.random() * possibleTiles.size()));
        }
        int maxTiles = possibleTiles.size();

        this.addTile(isPartOfLevel, possibleTiles, maxTiles, 0);

        this.fillEmptyTile(isPartOfLevel);
    }

    private void fillEmptyTile(boolean[][] isPartOfLevel) {
        int[] countBackground = new int[6];
        for (int y = 0; y < this.level.length; y++) {
            for (int x = 0; x < this.level[0].length; x++) {
                if (isPartOfLevel[y][x] && this.level[y][x] != null) {
                    countBackground[this.level[y][x].getBackground().getAssetNumber()] += 1;
                }
            }
        }
        int[][] levelCount = new int[6][6];
        for (int y = 0; y < this.level.length; y++) {
            for (int x = 0; x < this.level[0].length; x++) {
                if (isPartOfLevel[y][x] && this.level[y][x] != null) {
                    levelCount[y][x] = countBackground[this.level[y][x].getBackground().getAssetNumber()];
                }
            }
        }
        for (int y = 0; y < this.level.length; y++) {
            for (int x = 0; x < this.level[0].length; x++) {
                if (isPartOfLevel[y][x] && levelCount[y][x] == 0) {
                    int min = 1000;
                    Vector2 point = new Vector2();
                    int left = getCountFor(x - 1, y, levelCount);
                    if (left > 0 && left < min) {
                        point = new Vector2(x-1, y);
                        min = left;
                    }
                    int right = getCountFor(x + 1, y, levelCount);
                    if (right > 0 && right < min) {
                        point = new Vector2(x+1, y);
                        min = right;
                    }
                    int up = getCountFor(x, y - 1, levelCount);
                    if (up > 0 && up < min) {
                        point = new Vector2(x, y - 1);
                        min = up;
                    }
                    int down = getCountFor(x, y + 1, levelCount);
                    if (down > 0 && down < min) {
                        point = new Vector2(x, y + 1);
                        min = down;
                    }
                    int rightUp = getCountFor(x + 1, y - 1, levelCount);
                    if (rightUp > 0 && rightUp < min) {
                        point = new Vector2(x + 1, y - 1);
                        min = rightUp;
                    }
                    int leftDown = getCountFor(x - 1, y + 1, levelCount);
                    if (leftDown > 0 && leftDown < min) {
                        point = new Vector2(x - 1, y + 1);
                    }
                    if (this.level[(int)point.y][(int)point.x] != null) {
                        Background background = this.level[(int) point.y][(int) point.x].getBackground();
                        this.level[y][x] = new Tile(background);
                        fillEmptyTile(isPartOfLevel);
                    }
                    return;
                }
            }
        }
    }

    private int getCountFor(int x, int y, int[][] levelCount) {
        if (x < 0 || y < 0 || x >= levelCount[0].length || y >= levelCount.length) {
            return -1;
        }
        return levelCount[y][x];
    }

    private void addTile(boolean[][] isPartOfLevel, ArrayList<Integer> possibleTiles, int maxTiles, int count) {
        if (possibleTiles.size() == 0 || count > 1000) {
            return;
        }

        int tileBackground = possibleTiles.remove((int)(Math.random() * possibleTiles.size()));

        if (!fillWithBackground(isPartOfLevel, Background.values()[tileBackground], possibleTiles.size(), maxTiles)) {
            possibleTiles.add(tileBackground);
            for (int y = 0; y < this.level.length; y++) {
                for (int x = 0; x < this.level[0].length; x++) {
                    if (this.level[y][x] != null && this.level[y][x].getBackground() == Background.values()[tileBackground]) {
                        this.level[y][x] = null;
                    }
                }
            }
        }
        addTile(isPartOfLevel, possibleTiles, maxTiles, count + 1);
    }

    private boolean fillWithBackground(boolean[][] isPartOfLevel, Background background, int tilesLeft, int maxTiles) {
        int minGoal = 3;
        if ((Math.random() * 100 > 15 && maxTiles < 5) || (Math.random() * 100 > 70)) {
            minGoal = 4;
            if (Math.random() * 100 > 90) {
                minGoal = 5;
            }
        }
        if (tilesLeft == 0) {
            minGoal = 2;
        }

        Vector2 freePoint = getFreePoint(isPartOfLevel);
        if (!isPartOfLevel[(int)freePoint.y][(int)freePoint.x] || this.level[(int)freePoint.y][(int)freePoint.x] != null) {
            return false;
        }
        boolean[][] visited = new boolean[6][6];
        int neighbors = getNeighbors(1, (int)freePoint.x, (int)freePoint.y, visited, isPartOfLevel);
        if (neighbors >= minGoal) {
            fillTile((int)freePoint.x, (int)freePoint.y, background, visited, minGoal);

            return true;
        }

        return false;
    }

    private void fillTile(int x, int y, Background background, boolean[][] visited, int goal) {
        int countForBackground = getCountForBackground(background);
        if (countForBackground >= goal) {
            return;
        }
        boolean[][] backupVisited = new boolean[6][6];
        this.level[y][x] = new Tile(background);
        ArrayList<Vector2> positions = new ArrayList<>();

        if (isFreeNeighbor(x - 1, y, visited, backupVisited)) {
            positions.add(new Vector2(x - 1, y));
        }
        if (isFreeNeighbor(x + 1, y, visited, backupVisited)) {
            positions.add(new Vector2(x + 1, y));
        }
        if (isFreeNeighbor(x, y - 1, visited, backupVisited)) {
            positions.add(new Vector2(x, y - 1));
        }
        if (isFreeNeighbor(x, y + 1, visited, backupVisited)) {
            positions.add(new Vector2(x, y + 1));
        }
        if (isFreeNeighbor(x + 1, y - 1, visited, backupVisited)) {
            positions.add(new Vector2(x + 1, y - 1));
        }
        if (isFreeNeighbor(x - 1, y + 1, visited, backupVisited)) {
            positions.add(new Vector2(x - 1, y + 1));
        }

        Collections.shuffle(positions);
        for (Vector2 point : positions) {
            fillTile((int)(point.x), (int)(point.y), background, visited, goal);
        }
    }

    private int getCountForBackground(Background background) {
        int count = 0;
        for (int y = 0; y < this.level.length; y++) {
            for (int x = 0; x < this.level[0].length; x++) {
                if (this.level[y][x] != null && this.level[y][x].getBackground() == background) {
                    count += 1;
                }
            }
        }
        return count;
    }

    private int getNeighbors(int size, int x, int y, boolean[][] visited, boolean[][] isPartOfLevel) {
        visited[y][x] = true;
        if (isFreeNeighbor(x - 1, y, isPartOfLevel, visited)) {
            return getNeighbors(size + 1, x-1, y, visited, isPartOfLevel);
        }
        if (isFreeNeighbor(x + 1, y, isPartOfLevel, visited)) {
            return getNeighbors(size + 1, x+1, y, visited, isPartOfLevel);
        }
        if (isFreeNeighbor(x, y - 1, isPartOfLevel, visited)) {
            return getNeighbors(size + 1, x, y-1, visited, isPartOfLevel);
        }
        if (isFreeNeighbor(x, y + 1, isPartOfLevel, visited)) {
            return getNeighbors(size + 1, x, y+1, visited, isPartOfLevel);
        }
        if (isFreeNeighbor(x + 1, y - 1, isPartOfLevel, visited)) {
            return getNeighbors(size + 1, x + 1, y - 1, visited, isPartOfLevel);
        }
        if (isFreeNeighbor(x - 1, y + 1, isPartOfLevel, visited)) {
            return getNeighbors(size + 1, x - 1, y + 1, visited, isPartOfLevel);
        }

        return size;
    }

    private Vector2 getFreePoint(boolean[][] isPartOfLevel) {
        int x = (int)(Math.random() * isPartOfLevel[0].length);
        int y = (int)(Math.random() * isPartOfLevel.length);
        int startX = x;
        int startY = y;
        while (!isPartOfLevel[y][x] || this.level[y][x] != null) {
            x += 1;
            if (x >= isPartOfLevel[0].length) {
                x = 0;
                y += 1;
                if (y >= isPartOfLevel.length) {
                    y = 0;
                }
            }
            if (startX == x && startY == y) {
                break;
            }
        }
        return new Vector2(x, y);
    }

    private boolean isFreeNeighbor(int x, int y, boolean[][] isPartOfLevel, boolean[][] visited) {
        if (x < 0 || y < 0 || x >= isPartOfLevel[0].length || y >= isPartOfLevel.length) {
            return false;
        }
        return isPartOfLevel[y][x] && this.level[y][x] == null && !visited[y][x];
    }

    public Tile[][] getLevel() {
        return level;
    }
}
