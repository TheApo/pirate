package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.ai.PiratePlayer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Draws the right-hand player panel: list of player names on a parchment
 * background, with a star and arrow marking the current player, and a
 * horizontal strikethrough for players already eliminated.
 *
 * Uses two rendering passes — sprite batch for images/text, shape renderer
 * for the strikethrough — each self-contained inside the call.
 */
public class RightHudRenderer {

    private static final int SLOT_HEIGHT = 65;
    private static final int AVATAR_SIZE = 50;
    private static final int ARROW_SIZE = 42;
    private static final int STAR_SIZE = 50;

    /** Renders the background parchment, slots, names, and current-player markers. */
    public void renderSprites(MainPanel panel, PiratePlayer[] players, int playerCount,
                              int currentPlayer, boolean blinkHidesStar) {
        if (players == null) return;
        int w = AssetLoader.gameInfo.getRegionWidth();
        int h = AssetLoader.gameInfo.getRegionHeight();
        int leftX = Constants.GAME_WIDTH - w - 10;
        int bgY = Constants.GAME_HEIGHT - h - 135;
        int bgHeight = 410 - (Constants.PLAYER_COLORS.length - playerCount) * 55;

        panel.spriteBatch.draw(AssetLoader.gameInfo, leftX, bgY, 283, bgHeight);
        panel.spriteBatch.draw(AssetLoader.gameInfo, leftX, bgY - 60, 283, 50);

        for (int i = 0; i < playerCount; i++) {
            int slotY = Constants.GAME_HEIGHT - h - 80 + i * SLOT_HEIGHT;
            int avatarVariant = currentPlayer == i ? 2 : 0;

            panel.spriteBatch.draw(AssetLoader.playerButton[i][avatarVariant],
                    Constants.GAME_WIDTH - w + 45, slotY, AVATAR_SIZE, AVATAR_SIZE);

            String name = players[i] != null ? players[i].getName() : "";
            if (currentPlayer == i) {
                if (!blinkHidesStar) {
                    panel.spriteBatch.draw(AssetLoader.star,
                            Constants.GAME_WIDTH - w / 2f - 5, slotY, STAR_SIZE, STAR_SIZE);
                }
                panel.spriteBatch.draw(AssetLoader.arrow,
                        Constants.GAME_WIDTH - w - 10 - 43, slotY, ARROW_SIZE, ARROW_SIZE);
                panel.drawString(name, Constants.GAME_WIDTH - w + 104, slotY + 5 - 1,
                        Constants.PLAYER_COLORS[i], AssetLoader.font25, DrawString.BEGIN, false, false);
            }
            panel.drawString(name, Constants.GAME_WIDTH - w + 103, slotY + 5,
                    Constants.COLOR_WHITE, AssetLoader.font25, DrawString.BEGIN, false, false);
        }
    }

    /** Draws a horizontal strikethrough on top of any player slot whose rule is out. */
    public void renderOutStrikes(MainPanel panel, Rule[] rules, int playerCount) {
        if (rules == null) return;
        boolean anyOut = false;
        for (Rule r : rules) if (r != null && r.isOut()) { anyOut = true; break; }
        if (!anyOut) return;

        int w = AssetLoader.gameInfo.getRegionWidth();
        int h = AssetLoader.gameInfo.getRegionHeight();

        panel.getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < playerCount; i++) {
            if (i >= rules.length || rules[i] == null || !rules[i].isOut()) continue;
            panel.getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
            panel.getRenderer().rect(Constants.GAME_WIDTH - w + 98,
                    Constants.GAME_HEIGHT - h - 80 + i * SLOT_HEIGHT + 25, 120, 5);
        }
        panel.getRenderer().end();
    }
}
