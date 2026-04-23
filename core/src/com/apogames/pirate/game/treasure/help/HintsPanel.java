package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.common.Localization;
import com.apogames.pirate.entity.ApoButtonPillSwitch;
import com.apogames.pirate.entity.ScrollablePanel;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.Rule;
import com.apogames.pirate.game.treasure.Tile;
import com.apogames.pirate.game.treasure.ai.PiratePlayer;

import java.util.ArrayList;

/**
 * Scrollable panel that, for each opponent of the active human player, lists
 * every possible hint rule (from {@code allPossibleRules}). Rules that are
 * already ruled out by earlier yes/no answers are shown in grey.
 *
 * The header carries an {@link ApoButtonPillSwitch} — tapping the "Alle"
 * label shows every candidate rule (greyed when excluded), tapping "moegliche"
 * filters the list down to rules still consistent with the opponent's
 * answers, tapping the pill toggles between them.
 *
 * Data is refreshed from the outside via {@link #rebuild(Tile[][], Rule[], PiratePlayer[], Rule[], int, int)}.
 */
public class HintsPanel extends ScrollablePanel {

    private static final int LINE_HEIGHT = 28;
    private static final int HEADER_HEIGHT = 55;
    private static final int BOTTOM_PADDING = 40;
    private static final int CONTENT_TOP_PADDING = 15;
    private static final int TEXT_X_OFFSET = 40;
    private static final int SWITCH_Y_OFFSET = 35;       // pill top, relative to panel top
    private static final int SWITCH_RIGHT_PADDING = 30;  // from panel right edge
    private static final int SWITCH_PILL_WIDTH  = 60;
    private static final int SWITCH_PILL_HEIGHT = 30;

    private ArrayList<HintRow> rows = new ArrayList<>();

    private final ApoButtonPillSwitch modeSwitch;

    // Cached state so tapping the switch can rebuild rows without re-fetching
    // game state from the outside.
    private Tile[][] cachedLevel;
    private Rule[]   cachedRules;
    private PiratePlayer[] cachedPlayers;
    private Rule[]   cachedAllPossibleRules;
    private int      cachedCurrentPlayer;
    private int      cachedPlayerCount;

    public HintsPanel(int x, int y, int width, int height) {
        super(x, y, width, height,
                HEADER_HEIGHT, LINE_HEIGHT, BOTTOM_PADDING, CONTENT_TOP_PADDING, TEXT_X_OFFSET,
                AssetLoader.hudInfoSlice);
        // Widget handles its own positioning — the panel only says "anchor
        // your right edge here, use this font and this size".
        this.modeSwitch = ApoButtonPillSwitch.rightAnchored(
                x + width - SWITCH_RIGHT_PADDING,
                y + SWITCH_Y_OFFSET,
                SWITCH_PILL_WIDTH, SWITCH_PILL_HEIGHT,
                "hints_mode", "hint.mode.all", "hint.mode.possible",
                AssetLoader.font25);
    }

    /**
     * Rebuilds the row list for the current game state. Shows every opponent
     * except the active player. The caller decides when offering this panel
     * is appropriate (e.g. only on a human's turn, or after the game is won
     * for a post-mortem review).
     */
    public void rebuild(Tile[][] level, Rule[] rules, PiratePlayer[] players,
                        Rule[] allPossibleRules, int currentPlayer, int playerCount) {
        this.cachedLevel            = level;
        this.cachedRules            = rules;
        this.cachedPlayers          = players;
        this.cachedAllPossibleRules = allPossibleRules;
        this.cachedCurrentPlayer    = currentPlayer;
        this.cachedPlayerCount      = playerCount;
        rebuildInternal();
    }

    /** Re-runs the row builder against the last-cached game state. Called
     *  when the mode switch is toggled. */
    private void rebuildInternal() {
        rows = new ArrayList<>();
        if (cachedLevel == null || cachedRules == null || cachedPlayers == null) return;

        boolean showOnlyPossible = modeSwitch.isRightSelected();
        for (int i = 0; i < cachedPlayerCount; i++) {
            if (i == cachedCurrentPlayer) continue;
            if (i >= cachedRules.length || cachedRules[i] == null) continue;

            rows.add(HintRow.header(i, Localization.format("hint.player_header", i + 1)));
            int n = 1;
            for (Rule r : cachedAllPossibleRules) {
                boolean excluded = !isPossibleForPlayer(r, cachedLevel, i);
                if (showOnlyPossible && excluded) continue;
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
    public void render(MainPanel panel) {
        super.render(panel);
        if (!isOpen()) return;
        modeSwitch.render(panel);
    }

    @Override
    protected void onTap(int mx, int my) {
        if (modeSwitch.handleClick(mx, my)) {
            rebuildInternal();
            scrollToTop();
        }
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
