package com.apogames.pirate.game.treasure.ai;

import com.apogames.pirate.common.Localization;
import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;

public class Human extends PiratePlayer {

    public String getName() {
        return Localization.get("ai.human");
    }

    public boolean isHuman() {
        return true;
    }

    @Override
    public Result placeGuess(Tile[][] level, Rule rule, Information info) {
        return null;
    }

    @Override
    public Result placeWrongMarker(Tile[][] level, Rule rule, Information info) {
        return null;
    }
}
