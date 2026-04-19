package com.apogames.pirate.game.treasure.ai;

public class Result {

    private int x;
    private int y;

    private int askPlayer;

    private boolean wantToSolve;

    public Result(final int x, final int y) {
        this.x = x;
        this.y = y;

        this.askPlayer = -1;

        this.wantToSolve = false;
    }

    public Result(final int x, final int y, final int askPlayer) {
        this.x = x;
        this.y = y;

        this.askPlayer = askPlayer;

        this.wantToSolve = false;
    }

    public Result(final int x, final int y, final boolean wantToSolve) {
        this.x = x;
        this.y = y;

        this.askPlayer = -1;

        this.wantToSolve = wantToSolve;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAskPlayer() {
        return askPlayer;
    }

    public boolean isWantToSolve() {
        return wantToSolve;
    }
}
