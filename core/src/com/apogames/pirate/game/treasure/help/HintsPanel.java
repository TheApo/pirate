package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.common.Localization;
import com.apogames.pirate.entity.ScrollablePanel;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;
import com.apogames.pirate.game.treasure.ai.PiratePlayer;

import java.util.ArrayList;

/**
 * Scrollable panel that, for each opponent of the active human player,
 * lists every possible hint rule (from {@code allPossibleRules}). Rules
 * that are already ruled out by earlier yes/no answers are shown in grey.
 *
 * Data is refreshed from the outside via {@link #rebuild(Tile[][], Rule[], PiratePlayer[], Rule[], int, int)}.
 */
public class HintsPanel extends ScrollablePanel {

    private static final int LINE_HEIGHT = 28;
    private static final int HEADER_HEIGHT = 55;
    private static final int BOTTOM_PADDING = 40;
    private static final int CONTENT_TOP_PADDING = 15;
    private static final int TEXT_X_OFFSET = 40;

    private ArrayList<HintRow> rows = new ArrayList<>();

    public HintsPanel(int x, int y, int width, int height) {
        super(x, y, width, height,
                HEADER_HEIGHT, LINE_HEIGHT, BOTTOM_PADDING, CONTENT_TOP_PADDING, TEXT_X_OFFSET,
                AssetLoader.hudInfoSlice);
    }

    /**
     * Rebuilds the row list for the current game state. Does nothing if the
     * active player is not human (rows become empty — caller typically hides
     * the button in that case).
     */
    public void rebuild(Tile[][] level, Rule[] rules, PiratePlayer[] players,
                        Rule[] allPossibleRules, int currentPlayer, int playerCount) {
        rows = new ArrayList<>();
        if (level == null || rules == null || players == null) return;
        if (currentPlayer < 0 || currentPlayer >= players.length) return;
        if (players[currentPlayer] == null || !players[currentPlayer].isHuman()) return;

        for (int i = 0; i < playerCount; i++) {
            if (i == currentPlayer) continue;
            if (i >= rules.length || rules[i] == null || rules[i].isOut()) continue;

            rows.add(HintRow.header(i, Localization.format("hint.player_header", i + 1)));
            int n = 1;
            for (Rule r : allPossibleRules) {
                boolean excluded = !isPossibleForPlayer(r, level, i);
                rows.add(HintRow.rule(n + ".) " + r.getText(), excluded));
                n++;
            }
            rows.add(HintRow.spacer());
        }
    }

    @Override
    protected int rowCount() { return rows.size(); }

    @Override
    protected String title() { return Localization.get("hint.title"); }

    @Override
    protected void renderRow(MainPanel panel, int index, float lineY) {
        HintRow row = rows.get(index);
        if (row.spacer) return;
        if (row.header) {
            panel.drawString(row.text, getX() + getWidth() / 2f, lineY,
                    playerColor(row.playerIndex), AssetLoader.font20, DrawString.MIDDLE, false, false);
            return;
        }
        float[] color = row.excluded ? Constants.COLOR_GREY : Constants.COLOR_WHITE;
        panel.drawString(row.text, getX() + textXOffset, lineY,
                color, AssetLoader.font20, DrawString.BEGIN, false, false);
    }

    @Override
    protected void onOpened() { scrollToTop(); }

    // -- rule-still-possible filter ----------------------------------------

    /**
     * A rule is still possible for {@code playerIdx} iff every tile the player
     * previously confirmed lies within the rule's solution, and every tile the
     * player previously ruled out lies outside it.
     */
    private static boolean isPossibleForPlayer(Rule rule, Tile[][] level, int playerIdx) {
        boolean[][] sol = rule.getSolution(level);
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                Tile t = level[y][x];
                if (t == null) continue;
                if (sol[y][x]  && t.getIncorrectGuesses()[playerIdx]) return false;
                if (!sol[y][x] && t.getCorrectGuesses()[playerIdx])   return false;
            }
        }
        return true;
    }

    private static float[] playerColor(int player) {
        if (player < 0 || player >= Constants.PLAYER_COLORS.length) {
            return Constants.COLOR_WHITE;
        }
        return Constants.PLAYER_COLORS[player];
    }

    // -- row model ---------------------------------------------------------

    private static final class HintRow {
        final boolean header;
        final boolean spacer;
        final int playerIndex;
        final String text;
        final boolean excluded;

        private HintRow(boolean header, boolean spacer, int playerIndex, String text, boolean excluded) {
            this.header = header;
            this.spacer = spacer;
            this.playerIndex = playerIndex;
            this.text = text;
            this.excluded = excluded;
        }

        static HintRow header(int playerIndex, String text) {
            return new HintRow(true, false, playerIndex, text, false);
        }
        static HintRow rule(String text, boolean excluded) {
            return new HintRow(false, false, -1, text, excluded);
        }
        static HintRow spacer() {
            return new HintRow(false, true, -1, "", false);
        }
    }
}
