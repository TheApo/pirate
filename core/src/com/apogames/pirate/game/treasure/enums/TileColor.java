package com.apogames.pirate.game.treasure.enums;

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
}
