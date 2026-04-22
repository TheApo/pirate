package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;
import com.apogames.pirate.game.treasure.enums.Status;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Draws the iso-tile grid in two passes (sprite batch + shape renderer) and
 * overlays the current-player's rule help / solution numbering / optional
 * coordinate debug label on each tile.
 *
 * Self-contained: opens and closes its own SpriteBatch / ShapeRenderer cycles.
 */
public class TileGridRenderer {

    private final LevelView view;

    private boolean showHelp = false;
    private boolean showSolution = false;
    private boolean showCoords = false;

    public TileGridRenderer(LevelView view) {
        this.view = view;
    }

    public void setShowHelp(boolean v) { this.showHelp = v; }
    public void setShowSolution(boolean v) { this.showSolution = v; }
    public void setShowCoords(boolean v) { this.showCoords = v; }

    public boolean isShowHelp() { return showHelp; }
    public boolean isShowSolution() { return showSolution; }
    public boolean isShowCoords() { return showCoords; }

    /** Draws tile sprites (treasure + rule overlays + coord labels) then the tile shape fills. */
    public void render(MainPanel panel, Tile[][] level, Rule[] rules,
                       Status status, int currentPlayer, int playerCount) {
        if (level == null) return;

        int tileSize = view.tileSize();
        float tileH = view.tileHeight();

        panel.spriteBatch.begin();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                Tile tile = level[y][x];
                if (tile == null || !view.isTileOnScreen(x, y)) continue;
                int sx = view.tileScreenX(x, y);
                int sy = view.tileScreenY(y);

                tile.render(panel, sx, sy, tileSize);

                if (tile.hasOnlyCorrectGuess(playerCount)) {
                    panel.spriteBatch.draw(AssetLoader.treasure,
                            sx + 0.1f * tileSize, sy + 0.3f * tileSize,
                            tileSize * 0.8f, tileSize * 0.8f * 2f / 3f);
                }
                if (showCoords) {
                    panel.drawString(x + "," + y,
                            sx + tileSize / 2f, sy + tileH / 2f - 8,
                            Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
                }
                renderRuleOverlays(panel, level, rules, status, currentPlayer, x, y, sx, sy, tileSize, tileH);
            }
        }
        panel.spriteBatch.end();

        panel.getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                Tile tile = level[y][x];
                if (tile == null || !view.isTileOnScreen(x, y)) continue;
                tile.renderFilled(panel, view.tileScreenX(x, y), view.tileScreenY(y), tileSize);
            }
        }
        panel.getRenderer().end();
    }

    private void renderRuleOverlays(MainPanel panel, Tile[][] level, Rule[] rules,
                                    Status status, int currentPlayer,
                                    int x, int y, int sx, int sy,
                                    int tileSize, float tileH) {
        if (rules == null) return;
        for (int i = 0; i < rules.length; i++) {
            if (rules[i] == null) continue;
            boolean[][] solution = rules[i].getSolution(level);
            if (!solution[y][x]) continue;

            if (showHelp
                    && ((i == 0 && status != Status.WON)
                     || (status == Status.WON && i == currentPlayer))) {
                panel.spriteBatch.draw(AssetLoader.tilesOverlay[i], sx, sy, tileSize, tileH);
            }
            if (showSolution) {
                boolean small = rules.length > 3;
                panel.drawString(String.valueOf(i + 1),
                        sx + 10 + i * 20, sy + 40, Constants.COLOR_BLACK,
                        small ? AssetLoader.font15 : AssetLoader.font20,
                        DrawString.MIDDLE, false, false);
            }
        }
    }

    /** Overlays a hex tile image (e.g. a highlight) at the given tile coords. */
    public void drawTileOverlay(MainPanel panel, int tileX, int tileY, int overlayIndex) {
        int sx = view.tileScreenX(tileX, tileY);
        int sy = view.tileScreenY(tileY);
        int tileSize = view.tileSize();
        panel.spriteBatch.draw(AssetLoader.tilesOverlay[overlayIndex],
                sx, sy, tileSize, view.tileHeight());
    }
}
