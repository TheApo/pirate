package com.apogames.pirate.game.treasure.enums;

public enum Background {

    GRAS(3),
    MOUNTAIN(2),
    FOREST(1),
    WATER(0),
    DESERT(4),
    ICE(6),
    LAVA_HOT(7);

    private final int assetNumber;

    Background(int assetNumber) {
        this.assetNumber = assetNumber;
    }

    public int getAssetNumber() {
        return assetNumber;
    }

    public static Background getRandomBackground() {
        return values()[(int)(Math.random() * values().length)];
    }
}
