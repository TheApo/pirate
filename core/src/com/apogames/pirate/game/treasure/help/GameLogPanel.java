package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.common.Localization;
import com.apogames.pirate.entity.ScrollablePanel;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.GameLogEntry;

import java.util.ArrayList;

/**
 * Scrollable panel showing every logged move in chronological order, grouped
 * by round. Tapping a row selects/deselects an entry; the owner reads
 * {@link #getSelectedIndex()} to highlight the referenced tile on the map.
 */
public class GameLogPanel extends ScrollablePanel {

    private static final int LINE_HEIGHT = 30;
    private static final int HEADER_HEIGHT = 55;
    private static final int BOTTOM_PADDING = 40;
    private static final int CONTENT_TOP_PADDING = 15;
    private static final int TEXT_X_OFFSET = 55;

    private final ArrayList<GameLogEntry> entries = new ArrayList<>();
    private int hoveredIndex = -1;
    private int selectedIndex = -1;

    public GameLogPanel(int x, int y, int width, int height) {
        super(x, y, width, height,
                HEADER_HEIGHT, LINE_HEIGHT, BOTTOM_PADDING, CONTENT_TOP_PADDING, TEXT_X_OFFSET,
                AssetLoader.hudInfoSlice);
    }

    public ArrayList<GameLogEntry> entries() { return entries; }

    public void clear() {
        entries.clear();
        hoveredIndex = -1;
        selectedIndex = -1;
        scrollToTop();
    }

    /** Appends a new entry. If the view was already at the bottom, follow it. */
    public void addEntry(GameLogEntry entry) {
        boolean wasAtEnd = getScrollOffset() + maxVisibleLines() >= buildRows().size();
        entries.add(entry);
        if (wasAtEnd) {
            scrollToBottom();
        }
    }

    /** Index of the selected entry (for tile highlight), or -1. */
    public int getSelectedIndex() { return selectedIndex; }

    /** Index of the hovered entry, or -1. */
    public int getHoveredIndex() { return hoveredIndex; }

    /** Preferred index for tile highlight: hovered wins over selected. */
    public int getHighlightEntryIndex() {
        return hoveredIndex >= 0 ? hoveredIndex : selectedIndex;
    }

    public void clearSelection() { selectedIndex = -1; }

    /** Updates the hover index for the given mouse position. */
    public void updateHover(int mx, int my) {
        if (!isOpen() || entries.isEmpty()) {
            hoveredIndex = -1;
            return;
        }
        hoveredIndex = entryIndexAt(mx, my);
    }

    @Override
    protected void onClosed() {
        hoveredIndex = -1;
        selectedIndex = -1;
    }

    @Override
    protected void onOpened() {
        scrollToBottom();
    }

    @Override
    protected void onTap(int mx, int my) {
        int tapped = entryIndexAt(mx, my);
        if (tapped >= 0) {
            selectedIndex = (selectedIndex == tapped) ? -1 : tapped;
        }
    }

    // -- rendering ----------------------------------------------------------

    @Override
    protected int rowCount() { return buildRows().size(); }

    @Override
    protected String title() { return Localization.get("log.title"); }

    @Override
    protected void renderRow(MainPanel panel, int index, float lineY) {
        ArrayList<LogRow> rows = buildRows();
        if (index >= rows.size()) return;
        LogRow row = rows.get(index);
        if (row.header) {
            panel.drawString(Localization.format("log.round", row.round),
                    getX() + getWidth() / 2f, lineY,
                    Constants.COLOR_YELLOW, AssetLoader.font20, DrawString.MIDDLE, false, false);
            return;
        }
        GameLogEntry entry = entries.get(row.entryIndex);
        int highlight = getHighlightEntryIndex();
        boolean marked = (row.entryIndex == highlight);
        float xStart = getX() + textXOffset + (marked ? 4f : 0f);
        if (marked) {
            panel.drawString(">", getX() + textXOffset - 18, lineY,
                    Constants.COLOR_WHITE, AssetLoader.font20, DrawString.BEGIN, false, false);
        }
        drawEntryLine(panel, entry, xStart, lineY);
    }

    // -- entry → line rendering --------------------------------------------

    private void drawEntryLine(MainPanel panel, GameLogEntry entry, float x, float y) {
        float cx = x;
        cx += drawSegment(panel, "P" + (entry.getActorPlayer() + 1) + " ",
                cx, y, playerColor(entry.getActorPlayer()));
        int tx = entry.getTileX();
        int ty = entry.getTileY();
        switch (entry.getType()) {
            case ASK_YES:
            case ASK_NO: {
                cx += drawSegment(panel, Localization.get("log.ask.verb") + " ", cx, y, Constants.COLOR_WHITE);
                cx += drawSegment(panel, "P" + (entry.getTargetPlayer() + 1) + " ",
                        cx, y, playerColor(entry.getTargetPlayer()));
                String tailKey = (entry.getType() == GameLogEntry.Type.ASK_YES)
                        ? "log.ask_yes.tail" : "log.ask_no.tail";
                drawSegment(panel, Localization.format(tailKey, tx, ty), cx, y, Constants.COLOR_WHITE);
                break;
            }
            case PLACE_NOT:
                drawSegment(panel, Localization.format("log.place_not", tx, ty), cx, y, Constants.COLOR_WHITE);
                break;
            case TREASURE_FOUND:
                drawSegment(panel, Localization.format("log.treasure_found", tx, ty), cx, y, Constants.COLOR_WHITE);
                break;
            case TREASURE_WRONG:
                drawSegment(panel, Localization.format("log.treasure_wrong", tx, ty), cx, y, Constants.COLOR_WHITE);
                break;
            default:
                break;
        }
    }

    private float drawSegment(MainPanel panel, String text, float x, float y, float[] color) {
        Constants.glyphLayout.setText(AssetLoader.font20, text);
        panel.drawString(text, x, y, color, AssetLoader.font20, DrawString.BEGIN, false, false);
        return Constants.glyphLayout.width;
    }

    private static float[] playerColor(int player) {
        if (player < 0 || player >= Constants.PLAYER_COLORS.length) {
            return Constants.COLOR_WHITE;
        }
        return Constants.PLAYER_COLORS[player];
    }

    // -- row model ----------------------------------------------------------

    private static final class LogRow {
        final boolean header;
        final int round;
        final int entryIndex;
        LogRow(boolean header, int round, int entryIndex) {
            this.header = header;
            this.round = round;
            this.entryIndex = entryIndex;
        }
    }

    private ArrayList<LogRow> buildRows() {
        ArrayList<LogRow> rows = new ArrayList<>();
        int lastRound = -1;
        for (int i = 0; i < entries.size(); i++) {
            GameLogEntry e = entries.get(i);
            if (e.getRound() != lastRound) {
                rows.add(new LogRow(true, e.getRound(), -1));
                lastRound = e.getRound();
            }
            rows.add(new LogRow(false, e.getRound(), i));
        }
        return rows;
    }

    /** Returns the entry index for the row currently under (mx, my), or -1. */
    private int entryIndexAt(int mx, int my) {
        int rowIdx = rowAt(mx, my);
        if (rowIdx < 0) return -1;
        ArrayList<LogRow> rows = buildRows();
        LogRow r = rows.get(rowIdx);
        return r.header ? -1 : r.entryIndex;
    }
}
