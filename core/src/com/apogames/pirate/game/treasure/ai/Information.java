package com.apogames.pirate.game.treasure.ai;

import com.apogames.pirate.game.treasure.Rule;

public class Information {

    private int player;
    private int maxPlayer;

    private Rule[] allPossibleRules;

    public Information(int player, int maxPlayer, Rule[] allPossibleRules) {
        this.player = player;
        this.maxPlayer = maxPlayer;
        this.allPossibleRules = allPossibleRules;
    }

    public int getPlayer() {
        return player;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public Rule[] getAllPossibleRules() {
        return allPossibleRules;
    }
}
