package com.apogames.pirate.game.treasure.ai;

import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;

import java.util.ArrayList;

public class Medium extends PiratePlayer {

    private ArrayList<ArrayList<Rule>> possiblePlayerRules = new ArrayList<>();

    public String getName() {
        return "Mittel";
    }

    private int maxAdd = 0;
    private int howMedium = 0;
    private int count = 0;

    public void init() {
        this.reset();
        maxAdd = (int)(Math.random() * 4);
    }

    private void reset() {
        howMedium = (int)(Math.random() * 100);
        count = 0;
    }

    @Override
    public Result placeGuess(Tile[][] level, Rule rule, Information info) {
        count += 1;
        if (count > 4) {
            this.reset();
        }
        int resultX = -1;
        int resultY = -1;
        int count = 0;

        this.fillPossiblePlayerRules(level, rule, info);

        boolean solutionClear = true;
        for (ArrayList<Rule> possiblePlayerRule : this.possiblePlayerRules) {
            if (possiblePlayerRule.size() != 1) {
                solutionClear = false;
                break;
            }
        }

        if (solutionClear && maxAdd > 0) {
            maxAdd -= 1;
        } else if (solutionClear) {
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    boolean realSolution = true;
                    for (int i = 0; i < info.getMaxPlayer(); i++) {
                        boolean[][] solution = this.possiblePlayerRules.get(i).get(0).getSolution(level);
                        if (!solution[y][x]) {
                            realSolution = false;
                        }
                    }
                    if (realSolution) {
                        return new Result(x, y, true);
                    }
                }
            }
        }

        ArrayList<Integer> possiblePlayersAsSolution = new ArrayList<>();
        while (resultX < 0) {
            count += 1;
            resultX = (int)(Math.random() * level[0].length);
            resultY = (int)(Math.random() * level.length);

            ArrayList<Integer> possibleSolutions = new ArrayList<>();
            if (level[resultY][resultX] != null && !level[resultY][resultX].hasIncorrectGuess()) {
                possibleSolutions = getPossibleSolutionsForPoint(resultX, resultY, level, info.getPlayer());
                if (possibleSolutions.size() > 0) {
                    possiblePlayersAsSolution = possibleSolutions;
                    break;
                }
            }

            if ((level[resultY][resultX] == null || level[resultY][resultX].hasIncorrectGuess()) && count < 1000 && possibleSolutions.size() <= 0) {
                resultX = -1;
            }
        }

        if (possiblePlayersAsSolution.size() == 0 || this.howMedium < 7) {
            return getResultForNoPossiblePlayer(resultX, resultY, info, level);
        }

        int counterHere = 0;
        int index = (int)(Math.random() * possiblePlayersAsSolution.size());
        int nextPlayer = possiblePlayersAsSolution.get(index);
        while ((nextPlayer == info.getPlayer() || level[resultY][resultX].getCorrectGuesses()[nextPlayer]) && counterHere < 10) {
            index += 1;
            counterHere += 1;
            if (index >= possiblePlayersAsSolution.size()) {
                index = 0;
            }
            nextPlayer = possiblePlayersAsSolution.get(index);
        }

        if (counterHere >= 10) {
            nextPlayer = info.getPlayer();
        }

        return new Result(resultX, resultY, nextPlayer);
    }

    private Result getResultForNoPossiblePlayer(int resultX, int resultY, Information info, Tile[][] level) {
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

    private ArrayList<Integer> getPossibleSolutionsForPoint(int resultX, int resultY, Tile[][] level, int currentPlayer) {
        ArrayList<Integer> players = new ArrayList<>();

        for (int i = 0; i < this.possiblePlayerRules.size(); i++) {
            if (i != currentPlayer) {
                ArrayList<Rule> rules = possiblePlayerRules.get(i);
                for (Rule rule : rules) {
                    if (rule.getSolution(level)[resultY][resultX]) {
                        players.add(i);
                        break;
                    }
                }
            }
        }

        return players;
    }


    private void fillPossiblePlayerRules(Tile[][] level, Rule rule, Information info) {
        this.possiblePlayerRules = new ArrayList<>();
        for (int i = 0; i < info.getMaxPlayer(); i++) {
            ArrayList<Rule> rules = new ArrayList<>();
            if (i == info.getPlayer()) {
                rules.add(rule);
            } else {
                Rule[] allPossibleRules = info.getAllPossibleRules();
                for (Rule allPossibleRule : allPossibleRules) {
                    boolean couldBe = true;
                    for (int y = 0; y < level.length; y++) {
                        for (int x = 0; x < level[0].length; x++) {
                            if (level[y][x] != null) {
                                boolean[][] solution = allPossibleRule.getSolution(level);
                                if (solution[y][x] && level[y][x].getIncorrectGuesses()[i]) {
                                    couldBe = false;
                                    break;
                                }
                                if (howMedium > 20) {
                                    if (!solution[y][x] && level[y][x].getCorrectGuesses()[i]) {
                                        couldBe = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!couldBe) {
                            break;
                        }
                    }
                    if (couldBe) {
                        rules.add(allPossibleRule);
                    }
                }
            }
            //System.out.print("Spieler "+(i+1)+": "+rules.size());
            //if (rules.size() > 0) {
            //    System.out.print(" "+rules.get(0).getText());
            //    System.out.println();
            //}
            this.possiblePlayerRules.add(rules);
        }
        //System.out.println();
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
