package com.apogames.pirate.entity;

import com.apogames.pirate.common.Localization;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Image button that shows the current locale's language code (e.g. "DE", "EN")
 * as a label on top of the image, reading the label fresh from {@link Localization}
 * on every render — no manual refresh needed when the locale changes. The label
 * itself is drawn by the parent via {@link #getText()}.
 */
public class ApoButtonLanguageImage extends ApoButtonImageWithThree {

    public ApoButtonLanguageImage(int x, int y, int width, int height, String function, TextureRegion[] images) {
        super(x, y, width, height, function, "", images);
    }

    @Override
    public String getText() {
        return Localization.getInstance().getLocale().getLanguage().toUpperCase();
    }
}
