package com.apogames.pirate.game.treasure.create;

import com.apogames.pirate.game.treasure.Tile;
import com.apogames.pirate.game.treasure.enums.Background;
import com.apogames.pirate.game.treasure.enums.ExtraObjective;
import com.apogames.pirate.game.treasure.enums.TileColor;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LevelExtraObjectives {

    private Tile[][] level;

    public LevelExtraObjectives(final Tile[][] level, boolean hard, int size) {
        this.level = level;

        this.fillWithExtra(hard, size);
    }

    private void fillWithExtra(boolean hard, int size) {
        ArrayList<Integer> extras = new ArrayList<>();
        extras.add(0);
        extras.add(1);
        extras.add(2);

        if (size <= 1) {
            extras.remove((int)(Math.random() * extras.size()));
        }

        ArrayList<TileColor> colors = new ArrayList<>(Arrays.asList(TileColor.OBJECT_COLORS));
        colors.remove((int)(Math.random() * colors.size()));
        if (!hard) {
            colors.remove((int) (Math.random() * colors.size()));
        }

        for (int extra = 0; extra < extras.size(); extra++) {
            for (int color = 0; color < colors.size(); color++) {
                int x = (int)(Math.random() * this.level[0].length);
                int y = (int)(Math.random() * this.level.length);
                while (this.level[y][x] == null || this.level[y][x].getObjective() != null) {
                    x = (int)(Math.random() * this.level[0].length);
                    y = (int)(Math.random() * this.level.length);
                }

                this.level[y][x].setObjective(ExtraObjective.values()[extras.get(extra)], colors.get(color));
            }
        }

        addAnimals(ExtraObjective.RED_PANDA, 3, 3, 2);
        addAnimals(ExtraObjective.WHITE_SHEEP, 3, 2, 2);
        if (size > 0) {
            addAnimals(ExtraObjective.BEARS, 3, 3, 2);
            if (size > 1) {
                addAnimals(ExtraObjective.BLACK_SHEEP, 3, 2, 2);
            }
        }
    }

    private void addAnimals(ExtraObjective extraObjective, int... groupSizes) {
        for (int groupSize : groupSizes) {
            int x = (int) (Math.random() * this.level[0].length);
            int y = (int) (Math.random() * this.level.length);
            while (this.level[y][x] == null || this.level[y][x].getObjective() != null || this.level[y][x].getBackground() == Background.WATER) {
                x = (int) (Math.random() * this.level[0].length);
                y = (int) (Math.random() * this.level.length);
            }
            boolean[][] visited = new boolean[this.level.length][this.level[0].length];
            fillTile(x, y, extraObjective, visited, groupSize);
        }
    }

    private void fillTile(int x, int y, ExtraObjective extraObjective, boolean[][] visited, int goal) {
        int visitedCount = getVisitedCount(visited);
        if (visitedCount >= goal) {
            return;
        }
        visited[y][x] = true;
        this.level[y][x].setObjective(extraObjective, TileColor.BLACK);
        ArrayList<Vector2> positions = new ArrayList<>();

        if (isFreeNeighbor(x - 1, y)) {
            positions.add(new Vector2(x - 1, y));
        }
        if (isFreeNeighbor(x + 1, y)) {
            positions.add(new Vector2(x + 1, y));
        }
        if (isFreeNeighbor(x, y - 1)) {
            positions.add(new Vector2(x, y - 1));
        }
        if (isFreeNeighbor(x, y + 1)) {
            positions.add(new Vector2(x, y + 1));
        }
        if (isFreeNeighbor(x + 1, y - 1)) {
            positions.add(new Vector2(x + 1, y - 1));
        }
        if (isFreeNeighbor(x - 1, y + 1)) {
            positions.add(new Vector2(x - 1, y + 1));
        }

        Collections.shuffle(positions);
        for (Vector2 point : positions) {
            fillTile((int)(point.x), (int)(point.y), extraObjective, visited, goal);
        }
    }

    private int getVisitedCount(boolean[][] visited) {
        int count = 0;
        for (int y = 0; y < visited.length; y++) {
            for (int x = 0; x < visited[0].length; x++) {
                if (visited[y][x]) {
                    count += 1;
                }
            }
        }
        return count;
    }

    private boolean isFreeNeighbor(int x, int y) {
        if (x < 0 || y < 0 || x >= this.level[0].length || y >= this.level.length) {
            return false;
        }
        return this.level[y][x] != null && this.level[y][x].getObjective() == null && this.level[y][x].getBackground() != Background.WATER;
    }

    public Tile[][] getLevel() {
        return level;
    }
}
