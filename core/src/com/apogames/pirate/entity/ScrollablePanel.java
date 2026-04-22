package com.apogames.pirate.entity;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.game.MainPanel;

/**
 * Base class for floating UI panels with a 9-slice background, a centered
 * title header and a scrollable list of rows. Scrolls via mouse wheel (when
 * the mouse is inside the panel) and via vertical drag started inside the
 * panel. Subclasses provide the row count, title text, and per-row rendering.
 *
 * Inherits position ({@code x,y,width,height}), visibility flag and
 * {@link #intersects(float, float)} hit-testing from {@link ApoEntity}.
 *
 * Mouse-event methods return true when they consume the event so the caller
 * can short-circuit further handling.
 */
public abstract class ScrollablePanel extends ApoEntity {

    protected final int headerHeight;
    protected final int lineHeight;
    protected final int bottomPadding;
    protected final int contentTopPadding;
    protected final int textXOffset;

    private final NineSlice background;

    private int scrollOffset = 0;
    private boolean dragging = false;
    private float dragAccum = 0f;
    private int pressY = -1;

    protected ScrollablePanel(int x, int y, int width, int height,
                              int headerHeight, int lineHeight, int bottomPadding,
                              int contentTopPadding, int textXOffset,
                              NineSlice background) {
        super(x, y, width, height);
        setVisible(false);
        this.headerHeight = headerHeight;
        this.lineHeight = lineHeight;
        this.bottomPadding = bottomPadding;
        this.contentTopPadding = contentTopPadding;
        this.textXOffset = textXOffset;
        this.background = background;
    }

    public boolean isOpen() { return isVisible(); }

    public void setOpen(boolean open) {
        if (isVisible() == open) return;
        setVisible(open);
        if (!open) {
            resetDragState();
            onClosed();
        } else {
            onOpened();
        }
    }

    public void toggle() { setOpen(!isOpen()); }

    public boolean isInside(int mx, int my) { return isOpen() && intersects(mx, my); }

    public int getLineHeight() { return lineHeight; }

    public int maxVisibleLines() {
        return ((int) getHeight() - headerHeight - bottomPadding) / lineHeight;
    }

    public int getScrollOffset() { return scrollOffset; }
    public void scrollToBottom() {
        this.scrollOffset = Math.max(0, rowCount() - maxVisibleLines());
    }
    public void scrollToTop() { this.scrollOffset = 0; }

    public boolean isDragging() { return dragging; }

    /** Starts a drag if the press is inside the panel. Returns true iff consumed. */
    public boolean onMousePressed(int mx, int my) {
        if (!isInside(mx, my)) return false;
        dragging = true;
        dragAccum = 0f;
        pressY = my;
        return true;
    }

    /** Applies vertical delta to the scroll if a drag is active. Returns true iff consumed. */
    public boolean onMouseDragged(int deltaY) {
        if (!dragging) return false;
        dragAccum += deltaY;
        int steps = (int) (dragAccum / lineHeight);
        if (steps != 0) {
            scrollOffset = clamp(scrollOffset - steps, rowCount());
            dragAccum -= steps * lineHeight;
        }
        return true;
    }

    public boolean onMouseWheel(int mx, int my, int changed) {
        if (!isInside(mx, my)) return false;
        scrollOffset = clamp(scrollOffset + changed, rowCount());
        return true;
    }

    /**
     * Ends a drag. If the movement was small enough to count as a tap AND the
     * release happened inside the panel, {@link #onTap(int, int)} is invoked.
     * Returns true iff this panel owned the drag (caller should short-circuit).
     */
    public boolean onMouseReleased(int mx, int my) {
        if (!dragging) return false;
        boolean wasTap = Math.abs(my - pressY) < lineHeight / 2;
        resetDragState();
        if (wasTap && isInside(mx, my)) {
            onTap(mx, my);
        }
        return true;
    }

    public void render(MainPanel panel) {
        if (!isOpen()) return;
        background.draw(panel.spriteBatch, getX(), getY(), getWidth(), getHeight());

        int count = rowCount();
        String t = title();
        int maxLines = maxVisibleLines();
        if (count > maxLines) {
            t += "  (" + (scrollOffset + 1) + "-"
                    + Math.min(count, scrollOffset + maxLines) + ")";
        }
        panel.drawString(t, getX() + getWidth() / 2f, getY() + 8 + contentTopPadding,
                Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, false, false);

        scrollOffset = clamp(scrollOffset, count);
        int end = Math.min(count, scrollOffset + maxLines);
        int listTop = (int) getY() + headerHeight;
        for (int i = scrollOffset; i < end; i++) {
            float lineY = listTop + (i - scrollOffset) * lineHeight;
            renderRow(panel, i, lineY);
        }
    }

    /** Returns the absolute row index under (mx, my), or -1 if outside row area. */
    public int rowAt(int mx, int my) {
        if (!isOpen()) return -1;
        if (mx < getX() || mx > getX() + getWidth()) return -1;
        int listTop = (int) getY() + headerHeight;
        int maxLines = maxVisibleLines();
        int listBottom = listTop + maxLines * lineHeight;
        if (my < listTop || my > listBottom) return -1;
        int visibleRow = (my - listTop) / lineHeight;
        int idx = scrollOffset + visibleRow;
        if (idx < 0 || idx >= rowCount()) return -1;
        return idx;
    }

    private void resetDragState() {
        dragging = false;
        dragAccum = 0f;
        pressY = -1;
    }

    protected static int clamp(int off, int rowCount) {
        int max = Math.max(0, rowCount - 1);
        if (off < 0) return 0;
        if (off > max) return max;
        return off;
    }

    // -- template-method hooks ---------------------------------------------

    protected abstract int rowCount();
    protected abstract String title();
    protected abstract void renderRow(MainPanel panel, int index, float lineY);

    /** Default: no-op. Override to handle a tap inside the row area. */
    protected void onTap(int mx, int my) { }
    protected void onOpened() { }
    protected void onClosed() { }
}
