package com.apogames.pirate.game.treasure.ai;

import com.apogames.pirate.common.Localization;
import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.GridPoint3;

import java.util.ArrayList;

public class Perfect extends PiratePlayer {

    private ArrayList<ArrayList<Rule>> possiblePlayerRules = new ArrayList<>();
    private int foundPlayer = -1;

    public String getName() {
        return Localization.get("ai.perfect");
    }

    public void init() {
    }

    @Override
    public Result placeGuess(Tile[][] level, Rule rule, Information info) {
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

        if (solutionClear) {
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    if (level[y][x] == null || level[y][x].hasIncorrectGuess()) {
                        continue;
                    }
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
        if (solutionClear) {
            while (resultX < 0) {
                count += 1;
                resultX = (int) (Math.random() * level[0].length);
                resultY = (int) (Math.random() * level.length);

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
        } else {
            foundPlayer = -1;
            GridPoint3 bestPointToAsk = getNextBestPointToAsk(level, info.getPlayer());
            resultX = bestPointToAsk.x;
            resultY = bestPointToAsk.y;
            if (foundPlayer < 0) {
                if (bestPointToAsk.z < 0) {
                    possiblePlayersAsSolution = getPossibleSolutionsForPoint(resultX, resultY, level, info.getPlayer());
                } else {
                    possiblePlayersAsSolution.add(bestPointToAsk.z);
                }
            } else {
                return new Result(resultX, resultY, true);
            }
        }

        if (possiblePlayersAsSolution.size() == 0) {
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

    private GridPoint3 getNextBestPointToAsk(Tile[][] level, int currentPlayer) {
        GridPoint3 point = new GridPoint3(-1, -1, -1);
        GridPoint3 pointOnlyOne = onlyOneSolutionLeft(level);
        if (pointOnlyOne != null) {
            this.foundPlayer = 1;
            return pointOnlyOne;
        }
        ArrayList<GridPoint3> bestCandidates = new ArrayList<>();
        int bestDistance = Integer.MAX_VALUE;
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && !level[y][x].hasIncorrectGuess()) {
                    boolean possibleCorrect = true;
                    int bestPlayer = -1;
                    int counter = 0;
                    boolean possibleFoundOnlyOne = true;
                    for (int i = 0; i < this.possiblePlayerRules.size(); i++) {
                        int curBest = 0;
                        int foundPossible = 0;
                        boolean possibleRuleCorrect = false;
                        if (i != currentPlayer && possiblePlayerRules.get(i).size() > 1) {
                            ArrayList<Rule> rules = possiblePlayerRules.get(i);
                            for (Rule rule : rules) {
                                if (rule.getSolution(level)[y][x] && !level[y][x].hasIncorrectGuess()) {
                                    curBest += 1;
                                }
                                if (rule.getSolution(level)[y][x] && level[y][x].getCorrectGuesses()[i] && isSolutionForOne(level, x, y)) {
                                    possibleRuleCorrect = true;
                                }
                                if (rule.getSolution(level)[y][x] && isSolutionForOne(level, x, y)) {
                                    foundPossible += 1;
                                }
                            }
                            if (!possibleRuleCorrect) {
                                possibleCorrect = false;
                            }
                            int currentDistance = Math.abs(curBest - rules.size() / 2);
                            if (currentDistance < bestDistance) {
                                bestCandidates.clear();
                                bestCandidates.add(new GridPoint3(x, y, bestPlayer));
                                bestDistance = currentDistance;
                            } else if (currentDistance == bestDistance) {
                                bestCandidates.add(new GridPoint3(x, y, bestPlayer));
                            }
                        }
                        if (foundPossible != 1) {
                            possibleFoundOnlyOne = false;
                        } else {
                            counter += 1;
                        }
                    }

                    if (possibleFoundOnlyOne) {
                        point.x = x;
                        point.y = y;
                        this.foundPlayer = 1;
                        System.out.println("Einzige Lösung die möglich ist!");
                        return point;
                    }
                    if (possibleCorrect) {
                        point.x = x;
                        point.y = y;
                        this.foundPlayer = 1;
                        return point;
                    }
                }
            }
        }
        if (!bestCandidates.isEmpty()) {
            GridPoint3 chosen = bestCandidates.get((int)(Math.random() * bestCandidates.size()));
            point.x = chosen.x;
            point.y = chosen.y;
            point.z = chosen.z;
            return point;
        }
        // Fallback — no scored candidates (e.g. every remaining opponent's
        // possible-rule list was filtered to 0 or 1 entries). Still pick ANY
        // guessable tile so the caller always gets a valid target.
        int[] xy = findGuessableTile(level);
        point.x = xy[0];
        point.y = xy[1];
        return point;
    }

    private GridPoint3 onlyOneSolutionLeft(Tile[][] level) {
        int counter = 0;
        for (ArrayList<Rule> possiblePlayerRule : this.possiblePlayerRules) {
            if (possiblePlayerRule.size() == 1) {
                counter += 1;
            }
        }
        if (counter != this.possiblePlayerRules.size() - 1) {
            return null;
        }
        boolean[][] currentSolution = new boolean[level.length][level[0].length];
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && !level[y][x].hasIncorrectGuess()) {
                    currentSolution[y][x] = true;
                    for (ArrayList<Rule> possiblePlayerRule : this.possiblePlayerRules) {
                        if (possiblePlayerRule.size() == 1) {
                            if (!possiblePlayerRule.get(0).getSolution(level)[y][x]) {
                                currentSolution[y][x] = false;
                            }
                        }
                    }
                }
            }
        }

        Rule onlyRule = null;
        for (ArrayList<Rule> possiblePlayerRule : this.possiblePlayerRules) {
            if (possiblePlayerRule.size() != 1) {
                for (Rule curRule : possiblePlayerRule) {
                    int counterForRule = 0;
                    for (int y = 0; y < level.length; y++) {
                        for (int x = 0; x < level[0].length; x++) {
                            if (level[y][x] != null && curRule.getSolution(level)[y][x] && currentSolution[y][x]) {
                                counterForRule += 1;
                            }
                        }
                    }
                    if (counterForRule == 1) {
                        if (onlyRule == null) {
                            onlyRule = curRule;
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
        if (onlyRule != null) {
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    if (level[y][x] != null && onlyRule.getSolution(level)[y][x] && currentSolution[y][x]) {
                        return new GridPoint3(x, y, -1);
                    }
                }
            }
        }
        return null;
    }

    private boolean isSolutionForOne(Tile[][] level, int x, int y) {
        for (ArrayList<Rule> possiblePlayerRule : this.possiblePlayerRules) {
            if (possiblePlayerRule.size() == 1) {
                if (!possiblePlayerRule.get(0).getSolution(level)[y][x]) {
                    return false;
                }
            }
        }
        return true;
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
                    if (allPossibleRule.getText().equals(rule.getText())) {
                        continue;
                    }
                    boolean couldBe = true;
                    int counter = 0;
                    for (int y = 0; y < level.length; y++) {
                        for (int x = 0; x < level[0].length; x++) {
                            if (level[y][x] != null) {
                                boolean[][] solution = allPossibleRule.getSolution(level);
                                if (solution[y][x] && level[y][x].getIncorrectGuesses()[i]) {
                                    couldBe = false;
                                    break;
                                }
                                if (!solution[y][x] && level[y][x].getCorrectGuesses()[i]) {
                                    couldBe = false;
                                    break;
                                }
                                if (solution[y][x] && rule.getSolution(level)[y][x]) {
                                    counter += 1;
                                }
                            }
                        }
                        if (!couldBe) {
                            break;
                        }
                    }
                    if (counter == 0) {
                        couldBe = false;
                    }
                    if (couldBe) {
                        rules.add(allPossibleRule);
                    }
                }
            }
            System.out.print("Spieler "+(i+1)+": "+rules.size()+" - ");
//            for (Rule curRule : rules) {
//                System.out.print(curRule.getText()+",   ");
//            }
            System.out.println();
            this.possiblePlayerRules.add(rules);
        }
        System.out.println();
    }

    @Override
    public Result placeWrongMarker(Tile[][] level, Rule rule, Information info) {
        GridPoint2 gridPoint2 = fillPossibleRulesForOur(level, rule, info);
        return new Result(gridPoint2.x, gridPoint2.y);
    }

    private GridPoint2 fillPossibleRulesForOur(Tile[][] level, Rule myRule, Information info) {
        ArrayList<Rule> rules = new ArrayList<>();
        Rule[] allPossibleRules = info.getAllPossibleRules();
        for (Rule allPossibleRule : allPossibleRules) {
            boolean couldBe = true;
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    if (level[y][x] != null) {
                        boolean[][] solution = allPossibleRule.getSolution(level);
                        if (solution[y][x] && level[y][x].getIncorrectGuesses()[info.getPlayer()]) {
                            couldBe = false;
                            break;
                        }
                        if (!solution[y][x] && level[y][x].getCorrectGuesses()[info.getPlayer()]) {
                            couldBe = false;
                            break;
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

        GridPoint2 point = new GridPoint2(-1, -1);
        int max = 0;
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && !myRule.getSolution(level)[y][x]) {
                    if (point.x < 0 && !level[y][x].hasIncorrectGuess()) {
                        point.x = x;
                        point.y = y;
                    }
                    int incorrect = 0;
                    int correct = 0;
                    for (Rule rule : rules) {
                        if (!rule.getSolution(level)[y][x] && !level[y][x].hasIncorrectGuess()) {
                            incorrect += 1;
                        }
                        if (rule.getSolution(level)[y][x] && !level[y][x].hasIncorrectGuess()) {
                            correct += 1;
                        }
                    }
                    if (incorrect > max) {
                        point.x = x;
                        point.y = y;
                        max = incorrect;
                    } else if (correct > max) {
                        point.x = x;
                        point.y = y;
                        max = correct;
                    }
                }
            }
        }
        return point;
    }

}
