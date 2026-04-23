package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.game.treasure.Tile;
import com.badlogic.gdx.math.Vector2;

/**
 * Camera / iso-coordinate math for the hex-ish treasure grid. Holds the
 * scroll offset ({@code changeX}, {@code changeY}) and the current tile
 * size index, and converts between tile and screen coordinates.
 *
 * All geometry that depends on tile size lives here; callers should never
 * need to multiply by {@code 295f/256f} or {@code 0.75f} themselves.
 */
public class LevelView {

    /** Height-to-width ratio of a single hex tile image. */
    public static final float TILE_ASPECT = 295f / 256f;
    /** Row pitch as a fraction of tile height (vertical overlap between rows). */
    public static final float ROW_PITCH = 0.75f;

    private static final int MIN_VISIBLE = 200;

    private int changeX = -100;
    private int changeY = 0;
    private int curTileSize = 1;

    public int getChangeX() { return changeX; }
    public int getChangeY() { return changeY; }
    public void setChangeX(int v) { changeX = v; }
    public void setChangeY(int v) { changeY = v; }

    public int getCurTileSize() { return curTileSize; }

    /** Pixel size of a tile at the current zoom level. */
    public int tileSize() { return Constants.TILE_SIZE[curTileSize]; }

    /** Pixel height of a tile (image is taller than wide). */
    public float tileHeight() { return tileSize() * TILE_ASPECT; }

    /** Screen-space X of the top-left corner of tile (tileX, tileY). */
    public int tileScreenX(int tileX, int tileY) {
        int ts = tileSize();
        return changeX + tileX * ts + tileY * ts / 2;
    }

    /** Screen-space Y of the top-left corner of tile (_, tileY). */
    public int tileScreenY(int tileY) {
        int ts = tileSize();
        return changeY + (int) (tileY * ts * TILE_ASPECT * ROW_PITCH);
    }

    /** True if the tile's on-screen rectangle overlaps the visible area. */
    public boolean isTileOnScreen(int tileX, int tileY) {
        int sx = tileScreenX(tileX, tileY);
        int sy = tileScreenY(tileY);
        int ts = tileSize();
        return sx + ts >= 0 && sy + ts * TILE_ASPECT >= 0
                && sx < Constants.GAME_WIDTH && sy < Constants.GAME_HEIGHT;
    }

    /** Returns (tileX, tileY) under (mouseX, mouseY), or {@code null} if none. */
    public Vector2 screenToTile(Tile[][] level, int mouseX, int mouseY) {
        if (level == null) return null;
        int ts = tileSize();
        float th = tileHeight();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                Tile t = level[y][x];
                if (t == null) continue;
                int sx = tileScreenX(x, y);
                int sy = tileScreenY(y);
                if (mouseX > sx && mouseX < sx + ts && mouseY > sy && mouseY < sy + th
                        && t.getPolygon(sx, sy, ts).contains(mouseX, mouseY)) {
                    return new Vector2(x, y);
                }
            }
        }
        return null;
    }

    /** Pans the camera, clamped so the level never leaves the screen entirely. */
    public void pan(Tile[][] level, int dx, int dy) {
        changeX = clamp(changeX + dx, levelMinX(level), levelMaxX());
        changeY = clamp(changeY + dy, levelMinY(level), levelMaxY());
    }

    /**
     * Applies a zoom step (positive = zoom in, negative = zoom out), keeping
     * the world point currently under {@code (anchorX, anchorY)} at the same
     * screen position afterwards — so the player is "zooming into" the point
     * they're pointing at (mouse cursor on desktop, first-finger press
     * position on touch).
     */
    public void zoom(int steps, int anchorX, int anchorY) {
        float oldSize = Constants.TILE_SIZE[curTileSize];
        curTileSize -= steps;
        if (curTileSize < 0) curTileSize = 0;
        else if (curTileSize >= Constants.TILE_SIZE.length) curTileSize = Constants.TILE_SIZE.length - 1;
        float newSize = Constants.TILE_SIZE[curTileSize];
        if (newSize == oldSize) return; // clamped — no change to pan either
        float k = newSize / oldSize;
        // Solve: anchor_new_screen_pos = anchor_old_screen_pos  =>
        //   changeX_new + (anchor - changeX_old) * k = anchor
        changeX = Math.round(anchorX - (anchorX - changeX) * k);
        changeY = Math.round(anchorY - (anchorY - changeY) * k);
    }

    /**
     * Centers the bounding box of non-null tiles on the screen; if the level
     * is larger than the screen the leftmost/topmost tile is pinned to 0
     * instead so at least one edge of the level stays visible.
     */
    public void center(Tile[][] level) {
        if (level == null || level.length == 0 || level[0].length == 0) {
            changeX = 0;
            changeY = 0;
            return;
        }
        int ts = tileSize();
        float th = ts * TILE_ASPECT;

        int minLeft = Integer.MAX_VALUE;
        int maxRight = Integer.MIN_VALUE;
        int minTop = Integer.MAX_VALUE;
        int maxBottom = Integer.MIN_VALUE;

        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] == null) continue;
                int tx = x * ts + y * ts / 2;
                int ty = (int) (y * ts * TILE_ASPECT * ROW_PITCH);
                if (tx < minLeft) minLeft = tx;
                if (tx + ts > maxRight) maxRight = tx + ts;
                if (ty < minTop) minTop = ty;
                if (ty + th > maxBottom) maxBottom = (int) (ty + th);
            }
        }
        if (minLeft == Integer.MAX_VALUE) {
            changeX = 0;
            changeY = 0;
            return;
        }
        changeX = (Constants.GAME_WIDTH - (maxRight - minLeft)) / 2 - minLeft;
        changeY = (Constants.GAME_HEIGHT - (maxBottom - minTop)) / 2 - minTop;
        if (changeX + minLeft < 0) changeX = -minLeft;
        if (changeY + minTop < 0) changeY = -minTop;
    }

    // -- pan clamping helpers ----------------------------------------------

    private int levelPixelWidth(Tile[][] level) {
        int ts = tileSize();
        return level[0].length * ts + level.length / 2 * ts;
    }

    private int levelPixelHeight(Tile[][] level) {
        int ts = tileSize();
        return (int) (level.length * ts * TILE_ASPECT * ROW_PITCH + ts * TILE_ASPECT * 0.25f);
    }

    private int levelMinX(Tile[][] level) { return MIN_VISIBLE - levelPixelWidth(level); }
    private int levelMaxX()                { return Constants.GAME_WIDTH - MIN_VISIBLE; }
    private int levelMinY(Tile[][] level) { return MIN_VISIBLE - levelPixelHeight(level); }
    private int levelMaxY()                { return Constants.GAME_HEIGHT - MIN_VISIBLE; }

    private static int clamp(int value, int min, int max) {
        if (min > max) return (min + max) / 2;
        return Math.max(min, Math.min(max, value));
    }
}
