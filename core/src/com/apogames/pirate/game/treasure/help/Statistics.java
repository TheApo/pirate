package com.apogames.pirate.game.treasure.help;

public class Statistics {

    private final int maxRounds;

    private int currentRuns = 0;

    private int[] winners = new int[5];

    private boolean showAnalyse;

    public Statistics(int maxRounds) {
        this.maxRounds = maxRounds;

        this.currentRuns = 0;
        this.showAnalyse = true;
    }

    public boolean isShowAnalyse() {
        return showAnalyse;
    }

    public void setShowAnalyse(boolean showAnalyse) {
        this.showAnalyse = showAnalyse;
    }

    public boolean isFinished() {
        return currentRuns >= maxRounds;
    }

    public void addWinner(int player) {
        this.winners[player] += 1;
        this.currentRuns += 1;
    }

    public int[] getWinners() {
        return winners;
    }
}
