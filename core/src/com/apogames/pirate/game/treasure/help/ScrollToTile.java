package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.game.treasure.Treasure;

/**
 * Animates scrolling the level view (and optionally an on-screen mouse cursor)
 * to a target tile. Speed is delta-time-driven and scales with the current
 * tile size, so the perceived speed in tiles per second stays roughly
 * constant across zoom levels.
 *
 * <p>Three entry points:
 * <ul>
 *   <li>{@link #scrollToPosition(int, int)} – scroll the map AND animate the
 *       mouse cursor onto the tile (used when the AI is making a guess).</li>
 *   <li>{@link #moveMouseToPosition(int, int)} – just move the cursor to a
 *       point in screen coordinates (no map scroll).</li>
 *   <li>{@link #scrollOnly(int, int)} – scroll the map only, without showing
 *       any cursor (used by GameLog hover/click).</li>
 * </ul>
 */
public class ScrollToTile {

    /**
     * Pixels per second of scroll/cursor movement, per tile-size pixel.
     * Old behaviour was {@code value = 2} per 10-ms tick = 200 px/s, fixed.
     * 1.5× at tileSize 100 ≈ 300 px/s ⇒ {@code 3 / 100 = 0.03 → 3.0} as
     * "px per second per tile-pixel". Speed scales linearly with the
     * current tile size so the perceived tiles/sec stays roughly constant
     * across zoom levels.
     *
     * <p>Note: {@link #doThink(float)} is called from
     * {@link com.apogames.pirate.backend.SequentiallyThinkingScreenModel}
     * at a fixed 100 Hz with {@code deltaMs = 10}.
     */
    private static final float SPEED_PER_TILE_PX_PER_SEC = 3.0f;

    /** Scroll keeps panning until the tile sits inside this margin. */
    private static final int MARGIN_LEFT = 10;
    private static final int MARGIN_RIGHT = 300;
    private static final int MARGIN_TOP = 120;
    private static final int MARGIN_BOTTOM = 120;
    /** Cursor / mouse-goal "snap" tolerance. */
    private static final int CURSOR_SNAP_PX = 10;

    private Treasure treasure;

    private int goalX = -1;
    private int goalY = -1;

    /** True once the map is in place and we're animating the mouse cursor. */
    private boolean scrollMouse = false;
    /** When false, no cursor is drawn or animated – we just scroll the map. */
    private boolean cursorEnabled = true;
    /** Override for the left visible boundary when a UI panel obscures the map. */
    private int extraLeftMargin = 0;

    private int mouseX = Constants.GAME_WIDTH/2;
    private int mouseY = Constants.GAME_HEIGHT/2;

    private int mouseGoalX = Constants.GAME_WIDTH/2;
    private int mouseGoalY = Constants.GAME_HEIGHT/2;

    /** Sub-pixel accumulator so delta-time movement stays smooth. */
    private float remainder = 0f;

    public ScrollToTile(Treasure treasure) {
        this.treasure = treasure;
    }

    public void moveMouseToPosition(int x, int y) {
        this.scrollMouse = true;
        this.cursorEnabled = true;
        this.mouseGoalX = x;
        this.mouseGoalY = y;
        this.goalX = 0;
        this.extraLeftMargin = 0;
        this.remainder = 0f;
    }

    public void scrollToPosition(int x, int y) {
        startScroll(x, y, true, 0);
        this.mouseX = Constants.GAME_WIDTH / 2 - 150;
        this.mouseY = Constants.GAME_HEIGHT / 2;
        this.mouseGoalX = -1;
        this.mouseGoalY = -1;
    }

    /** Scrolls the map until the tile is fully visible, without animating any cursor. */
    public void scrollOnly(int tileX, int tileY) {
        startScroll(tileX, tileY, false, 0);
    }

    /**
     * Like {@link #scrollOnly(int, int)} but also reserves {@code leftPanelWidth}
     * pixels on the left as obstructed (e.g. open game-log panel).
     */
    public void scrollOnly(int tileX, int tileY, int leftPanelWidth) {
        startScroll(tileX, tileY, false, leftPanelWidth);
    }

    private void startScroll(int tileX, int tileY, boolean withCursor, int leftPanelWidth) {
        this.goalX = tileX;
        this.goalY = tileY;
        this.scrollMouse = false;
        this.cursorEnabled = withCursor;
        this.extraLeftMargin = leftPanelWidth;
        this.remainder = 0f;
    }

    public int getGoalX() {
        return goalX;
    }

    public boolean isScrollMouse() {
        return scrollMouse && cursorEnabled;
    }

    public boolean isCursorEnabled() {
        return cursorEnabled;
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
        if (this.goalX < 0) {
            return;
        }
        LevelView view = this.treasure.getView();
        int tileSize = view.tileSize();
        int changeX = view.tileScreenX(this.goalX, this.goalY);
        int changeY = view.tileScreenY(this.goalY);

        int step = stepFor(tileSize, delta);
        if (step <= 0) {
            return;
        }

        if (!this.scrollMouse) {
            boolean changed = panMapTowardsGoal(view, changeX, changeY, tileSize, step);
            if (!changed) {
                if (this.cursorEnabled) {
                    this.scrollMouse = true;
                } else {
                    finish();
                }
            }
        } else {
            advanceCursor(changeX, changeY, tileSize, step);
        }
    }

    /**
     * Computes integer pixel steps for this tick, accumulating the fractional
     * remainder so small per-tick fractions (e.g. 1.5 px at small zoom) still
     * average out correctly. {@code deltaMs} comes in milliseconds from the
     * fixed-step think loop.
     */
    private int stepFor(int tileSize, float deltaMs) {
        this.remainder += SPEED_PER_TILE_PX_PER_SEC * tileSize * (deltaMs / 1000f);
        int step = (int) this.remainder;
        if (step > 0) {
            this.remainder -= step;
        }
        return step;
    }

    private boolean panMapTowardsGoal(LevelView view, int changeX, int changeY, int tileSize, int step) {
        boolean changed = false;
        int leftBound = MARGIN_LEFT + this.extraLeftMargin;
        int rightBound = Constants.GAME_WIDTH - MARGIN_RIGHT - tileSize;
        if (changeX < leftBound) {
            view.setChangeX(view.getChangeX() + Math.min(step, leftBound - changeX));
            changed = true;
        } else if (changeX > rightBound) {
            view.setChangeX(view.getChangeX() - Math.min(step, changeX - rightBound));
            changed = true;
        }
        int topBound = MARGIN_TOP;
        int bottomBound = Constants.GAME_HEIGHT - MARGIN_BOTTOM - tileSize;
        if (changeY < topBound) {
            view.setChangeY(view.getChangeY() + Math.min(step, topBound - changeY));
            changed = true;
        } else if (changeY > bottomBound) {
            view.setChangeY(view.getChangeY() - Math.min(step, changeY - bottomBound));
            changed = true;
        }
        return changed;
    }

    private void advanceCursor(int changeX, int changeY, int tileSize, int step) {
        boolean changed;
        if (this.mouseGoalX >= 0) {
            changed = approachCursor(this.mouseGoalX, this.mouseGoalY, step);
        } else {
            int targetX = changeX + tileSize / 2;
            int targetY = changeY + tileSize / 2;
            changed = approachCursor(targetX, targetY, step);
        }
        if (!changed) {
            finish();
        }
    }

    private boolean approachCursor(int targetX, int targetY, int step) {
        boolean changed = false;
        if (targetX - CURSOR_SNAP_PX > this.mouseX) {
            this.mouseX += Math.min(step, targetX - CURSOR_SNAP_PX - this.mouseX);
            changed = true;
        } else if (targetX + CURSOR_SNAP_PX < this.mouseX) {
            this.mouseX -= Math.min(step, this.mouseX - (targetX + CURSOR_SNAP_PX));
            changed = true;
        }
        if (targetY - CURSOR_SNAP_PX > this.mouseY) {
            this.mouseY += Math.min(step, targetY - CURSOR_SNAP_PX - this.mouseY);
            changed = true;
        } else if (targetY + CURSOR_SNAP_PX < this.mouseY) {
            this.mouseY -= Math.min(step, this.mouseY - (targetY + CURSOR_SNAP_PX));
            changed = true;
        }
        return changed;
    }

    private void finish() {
        this.goalX = -1;
        this.goalY = -1;
        this.mouseGoalX = -1;
        this.mouseGoalY = -1;
        this.scrollMouse = false;
        this.extraLeftMargin = 0;
        this.remainder = 0f;
    }
}
