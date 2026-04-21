package com.apogames.pirate.teavm;

import com.apogames.pirate.Constants;
import com.apogames.pirate.Pirate;
import com.apogames.pirate.common.Localization;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import org.teavm.jso.browser.Navigator;

import java.util.Locale;

public class TeaVMLauncher {
    public static void main(String[] args) {
        Constants.IS_HTML = true;
        Localization.getInstance().setLocale(detectBrowserLocale());
        WebApplicationConfiguration config = new WebApplicationConfiguration();
        config.width = 0;
        config.height = 0;
        config.antialiasing = true;
        config.showDownloadLogs = true;
        config.preloadListener = assetLoader -> {
            assetLoader.loadScript("freetype.js");
        };
        new WebApplication(new Pirate(), config);
    }

    private static Locale detectBrowserLocale() {
        String language = Navigator.getLanguage();
        if (language == null || language.isEmpty()) {
            return Locale.ENGLISH;
        }
        int dash = language.indexOf('-');
        String primary = (dash > 0) ? language.substring(0, dash) : language;
        return new Locale(primary);
    }
}
