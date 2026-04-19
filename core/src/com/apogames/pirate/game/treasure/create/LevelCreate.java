package com.apogames.pirate.game.treasure.create;

import com.apogames.pirate.game.treasure.Tile;

public class LevelCreate {

    private Tile[][] level;

    public LevelCreate(int size, boolean hard) {
        boolean big = size == 1;

        int levelX = big ? 21 : 15;
        this.level = new Tile[12][levelX];
        LevelMiniMap levelMiniMap = new LevelMiniMap();
        Tile[][] level = levelMiniMap.getLevel();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                this.level[y][x + 3] = level[y][x];
            }
        }

        levelMiniMap.createRandomLevelMiniMap();
        Tile[][] nextLevel = levelMiniMap.getLevel();
        for (int y = 0; y < nextLevel.length; y++) {
            for (int x = 0; x < nextLevel[0].length; x++) {
                if (nextLevel[y][x] != null) {
                    this.level[y][x + 6] = nextLevel[y][x];
                }
            }
        }

        levelMiniMap.createRandomLevelMiniMap();
        nextLevel = levelMiniMap.getLevel();
        for (int y = 0; y < nextLevel.length; y++) {
            for (int x = 0; x < nextLevel[0].length; x++) {
                if (nextLevel[y][x] != null) {
                    this.level[y][x + 9] = nextLevel[y][x];
                }
            }
        }
        if (big) {
            for (int i = 0; i < 2; i++) {
                levelMiniMap.createRandomLevelMiniMap();
                nextLevel = levelMiniMap.getLevel();
                for (int y = 0; y < nextLevel.length; y++) {
                    for (int x = 0; x < nextLevel[0].length; x++) {
                        if (nextLevel[y][x] != null) {
                            this.level[y][x + 12 + i * 3] = nextLevel[y][x];
                        }
                    }
                }
            }
        }

        levelMiniMap.createRandomLevelMiniMap();
        nextLevel = levelMiniMap.getLevel();
        for (int y = 0; y < nextLevel.length; y++) {
            for (int x = 0; x < nextLevel[0].length; x++) {
                if (nextLevel[y][x] != null) {
                    this.level[y + 6][x] = nextLevel[y][x];
                }
            }
        }

        levelMiniMap.createRandomLevelMiniMap();
        nextLevel = levelMiniMap.getLevel();
        for (int y = 0; y < nextLevel.length; y++) {
            for (int x = 0; x < nextLevel[0].length; x++) {
                if (nextLevel[y][x] != null) {
                    this.level[y + 6][x + 3] = nextLevel[y][x];
                }
            }
        }
        levelMiniMap.createRandomLevelMiniMap();
        nextLevel = levelMiniMap.getLevel();
        for (int y = 0; y < nextLevel.length; y++) {
            for (int x = 0; x < nextLevel[0].length; x++) {
                if (nextLevel[y][x] != null) {
                    this.level[y + 6][x + 6] = nextLevel[y][x];
                }
            }
        }

        if (big) {
            for (int i = 0; i < 2; i++) {
                levelMiniMap.createRandomLevelMiniMap();
                nextLevel = levelMiniMap.getLevel();
                for (int y = 0; y < nextLevel.length; y++) {
                    for (int x = 0; x < nextLevel[0].length; x++) {
                        if (nextLevel[y][x] != null) {
                            this.level[y + 6][x + 9 + i * 3] = nextLevel[y][x];
                        }
                    }
                }
            }
        }

        LevelExtraObjectives extraObjectives = new LevelExtraObjectives(this.level, hard);

        this.level = extraObjectives.getLevel();
    }

    public Tile[][] getLevel() {
        return level;
    }
}
