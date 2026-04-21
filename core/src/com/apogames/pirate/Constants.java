/*
 * Copyright (c) 2005-2017 Dirk Aporius <dirk.aporius@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.apogames.pirate;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Constants {
    public static final String PROGRAM_NAME = "Treasure Island";

    public final static GlyphLayout glyphLayout = new GlyphLayout();
    public static String WAIT_FOR_ANSWER = "";

    public final static boolean CAN_PLAY_ALL_GAMES = false;

    public static int YEAR = 2017;
    public static int MONTH = 12;
    public static int DAY = 11;

    public final static String USERLEVELS_GETPHP = "http://apo-games.de/advent/getDate.php";
    public final static String USERLEVELS_SAVEPHP = "http://apo-games.de/advent/save.php";
    public static final double VERSION = 0.01;
    public static boolean FPS = false;
    public static final int MAX_SCALE = 1;
    public static final int GAME_WIDTH = 1024 * MAX_SCALE;
    public static final int GAME_HEIGHT = 900 * MAX_SCALE;
    public static final int WAIT_TIME = 5000;
    public static final int WAIT_TIME_MORE = 3000;
    public static final int WAIT_TIME_LONGER = 7500;
    public static float[] COLOR_CLEAR = new float[]{81f / 255f, 46f / 255f, 18f / 255f, 1f};
    public static final float[] COLOR_WHITE = new float[]{255f / 255f, 255f / 255f, 255f / 255f, 1f};
    public static final float[] COLOR_BLUE_BRIGHT = new float[]{128f / 255f, 128f / 255f, 255f / 255f, 1f};
    public static final float[] COLOR_BLUE = new float[]{0f / 255f, 0f / 255f, 255f / 255f, 1f};
    public static final float[] COLOR_GREEN_BRIGHT = new float[]{128f / 255f, 255f / 255f, 128f / 255f, 1f};
    public static final float[] COLOR_GREEN = new float[]{0f / 255f, 255f / 255f, 0f / 255f, 1f};
    public static final float[] COLOR_RED_LIGHT = new float[]{255f / 255f, 128f / 255f, 128f / 255f, 1f};
    public static final float[] COLOR_RED = new float[]{255f / 255f, 0f / 255f, 0f / 255f, 1f};
    public static final float[] COLOR_YELLOW = new float[]{255f / 255f, 255f / 255f, 0f / 255f, 1f};
    public static final float[] COLOR_BLACK = new float[]{0f / 255f, 0f / 255f, 0f / 255f, 1f};
    public static final float[] COLOR_GREY = new float[]{99f / 255f, 99f / 255f, 99f / 255f, 1f};
    public static final float[] COLOR_BACKGROUND_BRIGHT = new float[]{126f / 255f, 126f / 255f, 146f / 255f, 1f};
    public static final float[] COLOR_BACKGROUND = new float[]{26f / 255f, 26f / 255f, 46f / 255f, 1f};
    public static final float[] COLOR_BUTTONS = new float[]{55f / 255f, 44f / 255f, 72f / 255f, 1f};
    public static boolean HELP_TIMER = false;

    public static boolean IS_ANDROID = false;
    public static boolean IS_HTML = false;

    public static boolean IS_DATE_SET = false;

    public static final float[][] COLORS = new float[][]{
            {1f / 255f, 74f / 255f, 65f / 255f, 1f},
            {196f / 255f, 0f / 255f, 20f / 255f, 1f},
            {197f / 255f, 27f / 255f, 123f / 255f, 1f},
            {2f / 255f, 150f / 255f, 220f / 255f, 1f},
            {87f / 255f, 173f / 255f, 24f / 255f, 1f},
            {192f / 255f, 76f / 255f, 27f / 255f, 1f},
            {255 / 255.0f, 210 / 255.0f, 80 / 255.0f, 1f},
            {255 / 255.0f, 136 / 255.0f, 110 / 255.0f, 1f},
            {163 / 255.0f, 194 / 255.0f, 255 / 255.0f, 1f},
            {163 / 255.0f, 210 / 255.0f, 136 / 255.0f, 1f},
    };
    public static final float[][] PLAYER_COLORS = new float[][]{
            {192f / 255f, 0f / 255f, 0f / 255f, 1f},
            {89f / 255f, 89f / 255f, 215f / 255f, 1f},
            {255f / 255f, 255f / 255f, 0f / 255f, 1f},
            {100f / 255f, 200f / 255f, 100f / 255f, 1f},
            {221f / 255f, 115f / 255f, 221f / 255f, 1f}
    };

    public static String round(double zahl, int stellen) {
        double d = (double) ((int) zahl + (Math.round(Math.pow(10, stellen) * (zahl - (int) zahl))) / (Math.pow(10, stellen)));
        String result = String.valueOf(d);
        if (result.indexOf(".") < result.length() - stellen) {
            result = result.substring(0, result.indexOf(".") + stellen + 1);
        } else if (result.indexOf(".") >= result.length() - stellen) {
            result = result + "0";
        }
        return result;
    }

    public static final int[] TILE_SIZE = {50, 75, 100, 150, 200, 256};
}
