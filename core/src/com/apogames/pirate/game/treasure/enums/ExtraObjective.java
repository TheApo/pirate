package com.apogames.pirate.game.treasure.enums;

import com.apogames.pirate.common.Localization;

public enum ExtraObjective {

    COIN(2),
    SWORDS(0),
    STAR(1),

    BEARS(1),
    RED_PANDA(0),
    WHITE_SHEEP(2),
    BLACK_SHEEP(3);

    private final int assetNumber;

    ExtraObjective(final int assetTile) {
        this.assetNumber = assetTile;
    }

    public int getAssetNumber() {
        return assetNumber;
    }

    public boolean isAnimal() {
        return this == BEARS || this == RED_PANDA || this == WHITE_SHEEP || this == BLACK_SHEEP;
    }

    /** Human-readable, localised name for UI ("Baer" / "Bear" / …). */
    public String localizedName() {
        String prefix = isAnimal() ? "animal." : "objective.";
        return Localization.get(prefix + name().toLowerCase());
    }

    public static ExtraObjective getRandomObjective() {
        return values()[(int)(Math.random() * values().length)];
    }
}
