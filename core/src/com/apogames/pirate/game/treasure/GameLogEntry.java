package com.apogames.pirate.game.treasure;

public class GameLogEntry {

    public enum Type {
        ASK_YES,
        ASK_NO,
        PLACE_NOT,
        TREASURE_FOUND,
        TREASURE_WRONG
    }

    private final Type type;
    private final int actorPlayer;
    private final int targetPlayer;
    private final int tileX;
    private final int tileY;
    private final int round;

    public GameLogEntry(Type type, int actorPlayer, int targetPlayer, int tileX, int tileY, int round) {
        this.type = type;
        this.actorPlayer = actorPlayer;
        this.targetPlayer = targetPlayer;
        this.tileX = tileX;
        this.tileY = tileY;
        this.round = round;
    }

    public Type getType() {
        return type;
    }

    public int getActorPlayer() {
        return actorPlayer;
    }

    public int getTargetPlayer() {
        return targetPlayer;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public int getRound() {
        return round;
    }
}
