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

    /**
     * Colours that may appear on objective tiles. Excludes {@link #GREEN} —
     * too close to the grass/forest backgrounds, hard to spot — and
     * {@link #BLACK}, which is reserved for animals.
     */
    public static final TileColor[] OBJECT_COLORS = { WHITE, YELLOW, RED, BLUE };

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
