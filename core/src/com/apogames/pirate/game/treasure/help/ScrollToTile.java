package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.game.treasure.Treasure;

public class ScrollToTile {

    private Treasure treasure;

    private int goalX = -1;
    private int goalY = -1;

    private boolean scrollMouse = false;

    private int mouseX = Constants.GAME_WIDTH/2;
    private int mouseY = Constants.GAME_HEIGHT/2;

    private int mouseGoalX = Constants.GAME_WIDTH/2;
    private int mouseGoalY = Constants.GAME_HEIGHT/2;

    public ScrollToTile(Treasure treasure) {
        this.treasure = treasure;
    }

    public void moveMouseToPosition(int x, int y) {
        this.scrollMouse = true;

        this.mouseGoalX = x;
        this.mouseGoalY = y;

        this.goalX = 0;
    }

    public void scrollToPosition(int x, int y) {
        this.goalX = x;
        this.goalY = y;

        this.scrollMouse = false;

        this.mouseX = Constants.GAME_WIDTH / 2 - 150;
        this.mouseY = Constants.GAME_HEIGHT / 2;

        this.mouseGoalX = -1;
        this.mouseGoalY = -1;
    }

    public int getGoalX() {
        return goalX;
    }

    public boolean isScrollMouse() {
        return scrollMouse;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void doThink(float delta) {
        if (this.treasure.noHumanLeft()) {
            this.goalX = -1;
            this.goalY = -1;
            this.scrollMouse = false;
        }
        if (this.goalX >= 0) {
            int tileSize = Constants.TILE_SIZE[this.treasure.getCurTileSize()];
            int changeX = this.treasure.getChangeX() + this.goalX * tileSize + this.goalY * tileSize / 2;
            int changeY = this.treasure.getChangeY() + (int) (this.goalY * tileSize * 295f / 256f * 0.75f);
            if (!this.scrollMouse) {
                final int value = 2;
                boolean changed = false;
                if (changeX < 10) {
                    this.treasure.setChangeX(this.treasure.getChangeX() + value);
                    changed = true;
                } else if (changeX > Constants.GAME_WIDTH - 300 - tileSize) {
                    this.treasure.setChangeX(this.treasure.getChangeX() - value);
                    changed = true;
                }
                if (changeY < 120) {
                    this.treasure.setChangeY(this.treasure.getChangeY() + value);
                    changed = true;
                } else if (changeY > Constants.GAME_HEIGHT - 120 - tileSize) {
                    this.treasure.setChangeY(this.treasure.getChangeY() - value);
                    changed = true;
                }
                if (!changed) {
                    this.scrollMouse = true;
                }
            } else {
                if (this.mouseGoalX >= 0) {
                    boolean changed = false;
                    if (this.mouseGoalX - 10 > this.mouseX) {
                        this.mouseX += 2;
                        changed = true;
                    } else if (this.mouseGoalX + 10 < this.mouseX) {
                        this.mouseX -= 2;
                        changed = true;
                    }
                    if (this.mouseGoalY - 10 > this.mouseY) {
                        this.mouseY += 2;
                        changed = true;
                    } else if (this.mouseGoalY + 10 < this.mouseY) {
                        this.mouseY -= 2;
                        changed = true;
                    }
                    if (!changed) {
                        this.goalX = -1;
                        this.goalY = -1;
                        this.mouseGoalX = -1;
                        this.mouseGoalY = -1;
                        this.scrollMouse = false;
                    }
                } else {
                    boolean changed = false;
                    if (changeX + tileSize / 2 - 10 > this.mouseX) {
                        this.mouseX += 2;
                        changed = true;
                    } else if (changeX + tileSize / 2 + 10 < this.mouseX) {
                        this.mouseX -= 2;
                        changed = true;
                    }
                    if (changeY + tileSize / 2 - 10 > this.mouseY) {
                        this.mouseY += 2;
                        changed = true;
                    } else if (changeY + tileSize / 2 + 10 < this.mouseY) {
                        this.mouseY -= 2;
                        changed = true;
                    }
                    if (!changed) {
                        this.goalX = -1;
                        this.goalY = -1;
                        this.mouseGoalX = -1;
                        this.mouseGoalY = -1;
                        this.scrollMouse = false;
                    }
                }
            }
        }
    }
}