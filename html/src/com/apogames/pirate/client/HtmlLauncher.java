package com.apogames.pirate.client;

import com.apogames.pirate.Constants;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.apogames.pirate.Pirate;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                Constants.IS_HTML = true;
                // Resizable application, uses available space in browser
                GwtApplicationConfiguration gwtApplicationConfiguration = new GwtApplicationConfiguration(true);
                gwtApplicationConfiguration.premultipliedAlpha = false;
                gwtApplicationConfiguration.antialiasing = false;
                gwtApplicationConfiguration.alpha = false;
                gwtApplicationConfiguration.preserveDrawingBuffer = false;
                return gwtApplicationConfiguration;
                // Fixed size application:
                //return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Pirate();
        }
}