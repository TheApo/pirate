package com.apogames.pirate.game.treasure.ai;

import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;

public class Easy extends PiratePlayer {

    public String getName() {
        return "Einfach";
    }

    @Override
    public Result placeGuess(Tile[][] level, Rule rule, Information info) {
        int resultX = -1;
        int resultY = -1;
        int count = 0;

        while (resultX < 0) {
            count += 1;
            resultX = (int)(Math.random() * level[0].length);
            resultY = (int)(Math.random() * level.length);

            if ((level[resultY][resultX] == null || level[resultY][resultX].hasIncorrectGuess()) && count < 1000) {
                resultX = -1;
            }
        }

        int counterHere = 0;
        int nextPlayer = (int)(Math.random() * info.getMaxPlayer());
        while ((nextPlayer == info.getPlayer() || level[resultY][resultX].getCorrectGuesses()[nextPlayer]) && counterHere < 10) {
            nextPlayer += 1;
            counterHere += 1;
            if (nextPlayer >= info.getMaxPlayer()) {
                nextPlayer = 0;
            }
        }

        if (counterHere >= 10) {
            nextPlayer = info.getPlayer();
        }

        return new Result(resultX, resultY, nextPlayer);
    }

    @Override
    public Result placeWrongMarker(Tile[][] level, Rule rule, Information info) {
        int resultX = -1;
        int resultY = -1;

        int count = 0;

        while (resultX < 0) {
            count += 1;
            resultX = (int)(Math.random() * level[0].length);
            resultY = (int)(Math.random() * level.length);

            if ((level[resultY][resultX] == null || level[resultY][resultX].hasIncorrectGuess() || rule.getSolution(level)[resultY][resultX]) && (count < 1000)) {
                resultX = -1;
            }
        }

        return new Result(resultX, resultY);
    }
}
