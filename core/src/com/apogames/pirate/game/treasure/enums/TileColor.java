package com.apogames.pirate.game.treasure.enums;

import com.apogames.pirate.common.Localization;

public enum TileColor {

    GREEN(0),
    WHITE(1),
    YELLOW(2),
    RED(3),
    BLUE(4),
    BLACK(5);

    private final int assetNumber;

    TileColor(final int assetNumber) {
        this.assetNumber = assetNumber;
    }

    public int getAssetNumber() {
        return assetNumber;
    }

    /** Human-readable, localised colour name for UI ("gruen" / "green" / …). */
    public String localizedName() {
        return Localization.get("tilecolor." + name().toLowerCase());
    }
}
