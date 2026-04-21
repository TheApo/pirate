package com.apogames.pirate.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import static com.badlogic.gdx.utils.I18NBundle.createBundle;

/**
 * Holds instances of the resource bundles and manages the locale.
 *
 * @author Maik Becker {@literal <hi@maik.codes>}
 */
public final class Localization {
    private static final Localization INSTANCE = new Localization();

    private Locale locale = Locale.ENGLISH;
    private I18NBundle common;

    private Localization() {
    }

    /**
     * Sets locale. Safe to call before libGDX is initialized — the bundle
     * is loaded lazily on first access.
     *
     * @param locale the locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        this.common = null;
    }

    /**
     * Gets locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Gets common. Loads the bundle on first call (requires Gdx.files to be available).
     *
     * @return the common
     */
    public I18NBundle getCommon() {
        if (common == null) {
            FileHandle internal = Gdx.files.internal("i18n/pirate");
            common = createBundle(internal, locale);
        }
        return common;
    }

    /**
     * Shortcut for getInstance().getCommon().get(key).
     */
    public static String get(String key) {
        return INSTANCE.getCommon().get(key);
    }

    /**
     * Shortcut for getInstance().getCommon().format(key, args).
     */
    public static String format(String key, Object... args) {
        return INSTANCE.getCommon().format(key, args);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Localization getInstance() {
        return INSTANCE;
    }
}
