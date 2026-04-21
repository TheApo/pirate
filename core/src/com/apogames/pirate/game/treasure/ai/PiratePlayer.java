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
}
