package com.apogames.pirate.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Draws a texture 9-sliced: four fixed-size corners, four tiled edges,
 * and a tiled center. When the destination size is not an integer multiple
 * of the source tile, the last edge/center tile is rendered as a partial
 * source sub-rectangle so no stretching or seams occur.
 *
 * Reusable for any texture; configure corners and source rectangle once at
 * construction time.
 */
public class NineSlice {

    private final Texture texture;
    private final int srcX, srcY, srcW, srcH;
    private final int cornerL, cornerR, cornerT, cornerB;
    private final int midW, midH;
    private final boolean flipY;

    public NineSlice(Texture texture,
                     int srcX, int srcY, int srcW, int srcH,
                     int cornerL, int cornerR, int cornerT, int cornerB,
                     boolean flipY) {
        this.texture = texture;
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcW = srcW;
        this.srcH = srcH;
        this.cornerL = cornerL;
        this.cornerR = cornerR;
        this.cornerT = cornerT;
        this.cornerB = cornerB;
        this.midW = srcW - cornerL - cornerR;
        this.midH = srcH - cornerT - cornerB;
        this.flipY = flipY;
    }

    public void draw(SpriteBatch sb, float x, float y, float w, float h) {
        float innerX = x + cornerL;
        float innerY = y + cornerT;
        float innerW = w - cornerL - cornerR;
        float innerH = h - cornerT - cornerB;
        int srcRightX  = srcX + srcW - cornerR;
        int srcBottomY = srcY + srcH - cornerB;

        drawSlice(sb, x,               y,               cornerL, cornerT, srcX,      srcY,       cornerL, cornerT);
        drawSlice(sb, x + w - cornerR, y,               cornerR, cornerT, srcRightX, srcY,       cornerR, cornerT);
        drawSlice(sb, x,               y + h - cornerB, cornerL, cornerB, srcX,      srcBottomY, cornerL, cornerB);
        drawSlice(sb, x + w - cornerR, y + h - cornerB, cornerR, cornerB, srcRightX, srcBottomY, cornerR, cornerB);

        tileHorizontal(sb, innerX, y,               innerW, cornerT, srcX + cornerL, srcY);
        tileHorizontal(sb, innerX, y + h - cornerB, innerW, cornerB, srcX + cornerL, srcBottomY);

        tileVertical(sb, x,               innerY, cornerL, innerH, srcX,      srcY + cornerT);
        tileVertical(sb, x + w - cornerR, innerY, cornerR, innerH, srcRightX, srcY + cornerT);

        tileBoth(sb, innerX, innerY, innerW, innerH, srcX + cornerL, srcY + cornerT);
    }

    private void drawSlice(SpriteBatch sb, float dstX, float dstY, float dstW, float dstH,
                           int sx, int sy, int sw, int sh) {
        sb.draw(texture, dstX, dstY, dstW, dstH, sx, sy, sw, sh, false, flipY);
    }

    private void tileHorizontal(SpriteBatch sb, float dstX, float dstY,
                                float dstW, float dstH, int sx, int sy) {
        float cursor = dstX;
        float remaining = dstW;
        while (remaining >= midW) {
            drawSlice(sb, cursor, dstY, midW, dstH, sx, sy, midW, (int) dstH);
            cursor += midW;
            remaining -= midW;
        }
        if (remaining > 0) {
            int partial = Math.min(midW, (int) Math.ceil(remaining));
            drawSlice(sb, cursor, dstY, remaining, dstH, sx, sy, partial, (int) dstH);
        }
    }

    private void tileVertical(SpriteBatch sb, float dstX, float dstY,
                              float dstW, float dstH, int sx, int sy) {
        float cursor = dstY;
        float remaining = dstH;
        while (remaining >= midH) {
            drawSlice(sb, dstX, cursor, dstW, midH, sx, sy, (int) dstW, midH);
            cursor += midH;
            remaining -= midH;
        }
        if (remaining > 0) {
            int partial = Math.min(midH, (int) Math.ceil(remaining));
            drawSlice(sb, dstX, cursor, dstW, remaining, sx, sy, (int) dstW, partial);
        }
    }

    private void tileBoth(SpriteBatch sb, float dstX, float dstY, float dstW, float dstH,
                          int sx, int sy) {
        float cy = dstY;
        float remY = dstH;
        while (remY > 0) {
            float tileDstH = Math.min(remY, midH);
            int   tileSrcH = Math.min(midH, (int) Math.ceil(tileDstH));
            float cx = dstX;
            float remX = dstW;
            while (remX > 0) {
                float tileDstW = Math.min(remX, midW);
                int   tileSrcW = Math.min(midW, (int) Math.ceil(tileDstW));
                drawSlice(sb, cx, cy, tileDstW, tileDstH, sx, sy, tileSrcW, tileSrcH);
                cx += tileDstW;
                remX -= tileDstW;
            }
            cy += tileDstH;
            remY -= tileDstH;
        }
    }
}
