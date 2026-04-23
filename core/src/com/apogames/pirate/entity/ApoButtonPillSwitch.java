package com.apogames.pirate.entity;

import com.apogames.pirate.Constants;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.backend.GameScreen;
import com.apogames.pirate.common.Localization;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Pill-shaped two-label switch. The caller specifies the PILL's position and
 * size; the widget auto-measures the label texts (from i18n) with the given
 * font and places them:
 * <ul>
 *   <li>left label — right-aligned at {@code pillX - LABEL_GAP}</li>
 *   <li>right label — left-aligned at {@code pillX + pillWidth + LABEL_GAP}</li>
 * </ul>
 *
 * Active label is yellow, inactive white; the black handle inside the pill
 * sits on the active side.
 *
 * Clicks are handled by {@link #handleClick(float, float)}: tapping the left
 * label selects "left", tapping the right label selects "right", tapping the
 * pill body toggles. The caller only needs the {@code boolean} return value
 * to know whether the state changed.
 */
public class ApoButtonPillSwitch extends ApoButton {

    /** Horizontal gap between a label and the adjacent pill edge, in pixels.
     *  Exposed so right-anchoring callers can compute the needed pill x. */
    public static final int LABEL_GAP = 5;

    /** Padding added around each hit region, in pixels. */
    private static final int HIT_PAD = 6;

    private final int pillWidth;
    private final int pillHeight;
    private final int handleInset;

    private final String leftKey;
    private final String rightKey;
    private final BitmapFont font;

    // Hit regions, refreshed during each render so hit-testing matches draw.
    private final ApoEntity leftRegion  = new ApoEntity(0, 0, 0, 0);
    private final ApoEntity pillRegion  = new ApoEntity(0, 0, 0, 0);
    private final ApoEntity rightRegion = new ApoEntity(0, 0, 0, 0);

    /**
     * @param pillX         top-left x of the PILL (labels are placed around it)
     * @param pillY         top-left y of the PILL
     * @param pillWidth     pill width in pixels
     * @param pillHeight    pill height in pixels
     * @param function      button function id (required by {@link ApoButton})
     * @param leftLabelKey  i18n key for the left label
     * @param rightLabelKey i18n key for the right label
     * @param font          font used for both labels
     */
    public ApoButtonPillSwitch(int pillX, int pillY,
                               int pillWidth, int pillHeight,
                               String function,
                               String leftLabelKey, String rightLabelKey,
                               BitmapFont font) {
        super(pillX, pillY, pillWidth, pillHeight, function, "");
        this.pillWidth   = pillWidth;
        this.pillHeight  = pillHeight;
        this.leftKey     = leftLabelKey;
        this.rightKey    = rightLabelKey;
        this.font        = font;
        this.handleInset = Math.max(2, pillHeight / 9);
    }

    /**
     * Builds a switch whose widget's right edge (the right label's right
     * edge) sits exactly at {@code rightEdgeX}. The pill x is derived
     * internally from the right label's measured width at {@code font},
     * so the caller does not need to know about label gaps or duplicate
     * the font-measurement logic.
     */
    public static ApoButtonPillSwitch rightAnchored(
            float rightEdgeX, int topY,
            int pillWidth, int pillHeight,
            String function,
            String leftLabelKey, String rightLabelKey,
            BitmapFont font) {
        Constants.glyphLayout.setText(font, Localization.get(rightLabelKey));
        float rightW = Constants.glyphLayout.width;
        int pillX = (int) (rightEdgeX - rightW - LABEL_GAP - pillWidth);
        return new ApoButtonPillSwitch(pillX, topY, pillWidth, pillHeight,
                function, leftLabelKey, rightLabelKey, font);
    }

    /** Whether the right label is the currently-active side. */
    public boolean isRightSelected()             { return isSelect(); }
    public void    setRightSelected(boolean v)   { setSelect(v); }
    public void    toggle()                      { setSelect(!isSelect()); }

    /** Region of the left-label clickable area (updated per render). */
    public ApoEntity getLeftRegion()  { return leftRegion; }
    /** Region of the pill clickable area (updated per render). */
    public ApoEntity getPillRegion()  { return pillRegion; }
    /** Region of the right-label clickable area (updated per render). */
    public ApoEntity getRightRegion() { return rightRegion; }

    /**
     * Handles a click at {@code (mx, my)}. Clicks on either label set that
     * side explicitly; a click on the pill body toggles. Returns {@code true}
     * if the click changed the active side.
     */
    public boolean handleClick(float mx, float my) {
        if (!isVisible()) return false;
        boolean was = isRightSelected();
        if (leftRegion.intersects(mx, my)) {
            setRightSelected(false);
        } else if (rightRegion.intersects(mx, my)) {
            setRightSelected(true);
        } else if (pillRegion.intersects(mx, my)) {
            setRightSelected(!was);
        } else {
            return false;
        }
        return isRightSelected() != was;
    }

    /**
     * Renders the switch. Must be called inside an already-open
     * {@code SpriteBatch}; the method ends and restarts it around the
     * ShapeRenderer pass for the pill so the caller doesn't have to care
     * about the internal pipeline switch.
     */
    public void render(GameScreen screen) {
        if (!isVisible()) return;

        String leftText  = Localization.get(leftKey);
        String rightText = Localization.get(rightKey);

        Constants.glyphLayout.setText(font, leftText);
        float leftW  = Constants.glyphLayout.width;
        float textH  = Constants.glyphLayout.height;
        Constants.glyphLayout.setText(font, rightText);
        float rightW = Constants.glyphLayout.width;

        float pillX   = getX();
        float pillY   = getY();
        float labelY  = pillY + pillHeight / 2f - textH / 2f - 5;
        float leftRight   = pillX - LABEL_GAP;                 // right edge of left label
        float leftLeft    = leftRight - leftW;                 // left edge of left label
        float rightLeft   = pillX + pillWidth + LABEL_GAP;     // left edge of right label
        float rightRight  = rightLeft + rightW;                // right edge of right label

        float[] leftColor  = isRightSelected() ? Constants.COLOR_WHITE  : Constants.COLOR_YELLOW;
        float[] rightColor = isRightSelected() ? Constants.COLOR_YELLOW : Constants.COLOR_WHITE;

        // Left label drawn with END alignment at the pill's left neighbour;
        // right label with BEGIN at the pill's right neighbour.
        screen.drawString(leftText,  leftRight,  labelY, leftColor,  font, DrawString.END,   false, false);
        screen.drawString(rightText, rightLeft,  labelY, rightColor, font, DrawString.BEGIN, false, false);

        screen.spriteBatch.end();
        drawPill(screen, pillX, pillY);
        screen.spriteBatch.begin();

        updateHitRegions(leftLeft, leftW, pillX, rightLeft, rightW, pillY);
    }

    // -- private helpers --------------------------------------------------

    private void drawPill(GameScreen screen, float pillX, float pillY) {
        float pillRadius = pillHeight / 2f;

        screen.getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        screen.getRenderer().setColor(1f, 1f, 1f, 1f);
        screen.getRenderer().roundedRect(pillX, pillY, pillWidth, pillHeight, pillRadius);

        float handleR = pillRadius - handleInset;
        float handleY = pillY + pillRadius;
        float handleX = isRightSelected()
                ? pillX + pillWidth - pillRadius
                : pillX + pillRadius;
        screen.getRenderer().setColor(0f, 0f, 0f, 1f);
        screen.getRenderer().circle(handleX, handleY, handleR);
        screen.getRenderer().end();

        screen.getRenderer().begin(ShapeRenderer.ShapeType.Line);
        screen.getRenderer().setColor(0f, 0f, 0f, 1f);
        screen.getRenderer().roundedRectLine(pillX, pillY, pillWidth, pillHeight, pillRadius);
        screen.getRenderer().end();
    }

    private void updateHitRegions(float leftX, float leftW,
                                  float pillX, float rightX, float rightW,
                                  float pillY) {
        float hitTop    = pillY - HIT_PAD;
        float hitHeight = pillHeight + HIT_PAD * 2;

        leftRegion.setX(leftX - HIT_PAD);
        leftRegion.setY(hitTop);
        leftRegion.setWidth(leftW + HIT_PAD * 2);
        leftRegion.setHeight(hitHeight);

        pillRegion.setX(pillX - HIT_PAD);
        pillRegion.setY(hitTop);
        pillRegion.setWidth(pillWidth + HIT_PAD * 2);
        pillRegion.setHeight(hitHeight);

        rightRegion.setX(rightX - HIT_PAD);
        rightRegion.setY(hitTop);
        rightRegion.setWidth(rightW + HIT_PAD * 2);
        rightRegion.setHeight(hitHeight);
    }
}
