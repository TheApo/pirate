package com.apogames.pirate.game.treasure.enums;

public enum ExtraObjective {

    COIN(2),
    SWORDS(0),
    STAR(1),

    BEARS(2),
    RED_PANDA(3);

    private final int assetNumber;

    ExtraObjective(final int assetTile) {
        this.assetNumber = assetTile;
    }

    public int getAssetNumber() {
        return assetNumber;
    }

    public static ExtraObjective getRandomObjective() {
        return values()[(int)(Math.random() * values().length)];
    }
}
