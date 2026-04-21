package com.apogames.pirate.game.tutorial;

public enum TutorialStep {
    GAME_START(0),
    GAME_OVERVIEW(1),
    GAME_ASKING(2),
    GAME_ASKING_CORRECT(3),
    GAME_ASKING_INCORRECT(3),
    GAME_ASKING_INCORRECT_2(3),
    GAME_ASKING_OTHER(4),
    GAME_RULES_BACKGROUND(5),
    GAME_SHOW_NOTICE(6),
    GAME_SHOW_NOTICE_INFO(7),
    MENU_START(8),
    MENU_PLAYERS(9);

    private final int step;

    TutorialStep(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }

    public TutorialStep getNextStep() {
        for (TutorialStep step : values()) {
            if (step.getStep() == this.step + 1) {
                return step;
            }
        }
        return GAME_OVERVIEW;
    }
}
