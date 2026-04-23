package com.apogames.pirate.game.treasure.ai;

import com.apogames.pirate.common.Localization;
import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;

public abstract class PiratePlayer {

    public boolean isHuman() {
        return false;
    }

    public void init() {

    }

    public abstract Result placeGuess(Tile[][] level, Rule rule, Information info);

    public abstract Result placeWrongMarker(Tile[][] level, Rule rule, Information info);

    public String getName() {
        return Localization.get("ai.generic");
    }

    /**
     * Finds any tile on the board that could still receive a guess marker.
     * Used by every AI as a last-resort fallback so its {@code placeGuess} /
     * {@code placeWrongMarker} methods never return invalid coordinates.
     *
     * Preference order: a tile that is not null AND not already ruled out by
     * anyone; else the first non-null tile; else (0,0).
     *
     * @return int[2] with [x, y].
     */
    protected static int[] findGuessableTile(Tile[][] level) {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && !level[y][x].hasIncorrectGuess()) {
                    return new int[]{x, y};
                }
            }
        }
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null) {
                    return new int[]{x, y};
                }
            }
        }
        return new int[]{0, 0};
    }
}
