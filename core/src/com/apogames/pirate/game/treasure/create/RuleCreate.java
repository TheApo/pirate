package com.apogames.pirate.game.treasure.create;

import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;
import com.apogames.pirate.game.treasure.enums.Background;
import com.apogames.pirate.game.treasure.enums.ExtraObjective;
import com.apogames.pirate.game.treasure.enums.TileColor;

import java.util.ArrayList;
import java.util.HashSet;

public class RuleCreate {

    public static Rule createRandomRule(Tile[][] level, boolean hard) {
        Rule rule;

        boolean not = false;
        if (hard && Math.random() * 100 > 50) {
            not = true;
        }
        int random = (int)(Math.random() * 100);
        if (random < 20) {
            HashSet<Background> backgroundSet = getAllBackgrounds(level);
            ArrayList<Background> backgrounds = new ArrayList<>(backgroundSet);

            ArrayList<Background> chosenBackgrounds = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Background chose = backgrounds.remove((int)(Math.random() * backgrounds.size()));
                chosenBackgrounds.add(chose);
            }
            rule = new Rule(chosenBackgrounds, not);
        } else if (random < 40) {
            HashSet<ExtraObjective> animalSet = getAllAnimals(level);
            ArrayList<ExtraObjective> animals = new ArrayList<>(animalSet);
            ExtraObjective extra = animals.get((int)(Math.random() * animals.size()));

            rule = new Rule(extra, TileColor.BLACK, 2, not);
        } else if (random < 60) {
            HashSet<ExtraObjective> allObjectives = getAllObjectives(level);
            ArrayList<ExtraObjective> objectives = new ArrayList<>(allObjectives);

            int color = (int)(Math.random() * 3);

            rule = new Rule(objectives.get((int)(Math.random() * objectives.size())), TileColor.values()[color], 2, not);
        } else if (random < 80) {
            HashSet<TileColor> colorSet = getAllColors(level);
            ArrayList<TileColor> colors = new ArrayList<>(colorSet);
            int color = (int)(Math.random() * colors.size());

            rule = new Rule(null, colors.get(color), 3, not);
        } else {
            Background background = Background.values()[(int)(Math.random() * 5)];
            rule = new Rule(background, not);
        }

        return rule;
    }

    private static HashSet<ExtraObjective> getAllObjectives(Tile[][] level) {
        HashSet<ExtraObjective> hashSet = new HashSet<>();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && level[y][x].getObjective() != null && !isAnimal(level[y][x].getObjective())) {
                    hashSet.add(level[y][x].getObjective());
                }
            }
        }

        return hashSet;
    }

    private static HashSet<ExtraObjective> getAllAnimals(Tile[][] level) {
        HashSet<ExtraObjective> hashSet = new HashSet<>();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && level[y][x].getObjective() != null && isAnimal(level[y][x].getObjective())) {
                    hashSet.add(level[y][x].getObjective());
                }
            }
        }

        return hashSet;
    }

    private static boolean isAnimal(ExtraObjective objective) {
        return objective == ExtraObjective.BEARS || objective == ExtraObjective.RED_PANDA
                || objective == ExtraObjective.WHITE_SHEEP || objective == ExtraObjective.BLACK_SHEEP;
    }

    private static HashSet<Background> getAllBackgrounds(Tile[][] level) {
        HashSet<Background> hashSet = new HashSet<>();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null) {
                    hashSet.add(level[y][x].getBackground());
                }
            }
        }

        return hashSet;
    }

    private static HashSet<TileColor> getAllColors(Tile[][] level) {
        HashSet<TileColor> hashSet = new HashSet<>();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] != null && level[y][x].getColor() != null && level[y][x].getColor() != TileColor.BLACK) {
                    hashSet.add(level[y][x].getColor());
                }
            }
        }

        return hashSet;
    }

    public static Rule[] getAllRules(Tile[][] level, boolean withNot) {
        ArrayList<Rule> allRules = new ArrayList<>();

        // backgrounds
        HashSet<Background> backgroundSet = getAllBackgrounds(level);
        ArrayList<Background> backgrounds = new ArrayList<>(backgroundSet);

        int start = 0;
        while (start <= backgrounds.size()) {
            for (int i = start + 1; i < backgrounds.size(); i++) {
                ArrayList<Background> chosenBackgrounds = new ArrayList<>();
                chosenBackgrounds.add(backgrounds.get(start));
                chosenBackgrounds.add(backgrounds.get(i));
                if (withNot) {
                    allRules.add(new Rule(chosenBackgrounds, true));
                }
                allRules.add(new Rule(chosenBackgrounds, false));
            }
            start += 1;
        }

        // habitat
        HashSet<ExtraObjective> animalSet = getAllAnimals(level);
        for (ExtraObjective animal : animalSet) {
            allRules.add(new Rule(animal, TileColor.BLACK, 2, false));
            if (withNot) {
                allRules.add(new Rule(animal, TileColor.BLACK, 2, true));
            }
        }

        // objectives
        HashSet<ExtraObjective> allObjectives = getAllObjectives(level);
        ArrayList<ExtraObjective> objectives = new ArrayList<>(allObjectives);

        for (ExtraObjective objective : objectives) {
            allRules.add(new Rule(objective, TileColor.values()[0], 2, false));
            if (withNot) {
                allRules.add(new Rule(objective, TileColor.values()[0], 2, true));
            }
        }

        //colors
        HashSet<TileColor> colorSet = getAllColors(level);
        ArrayList<TileColor> colors = new ArrayList<>(colorSet);

        for (TileColor color : colors) {
            allRules.add(new Rule(null, color, 3, false));
            if (withNot) {
                allRules.add(new Rule(null, color, 3, true));
            }
        }

        // background with 1 distance
        for (int i = 0; i < 5; i++) {
            Background background = Background.values()[i];
            allRules.add(new Rule(background, false));
            if (withNot) {
                allRules.add(new Rule(background, true));
            }
        }

        Rule[] allRulesArray = new Rule[allRules.size()];
        return allRules.toArray(allRulesArray);
    }
}
