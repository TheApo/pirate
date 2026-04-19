package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.game.MainPanel;

public class ShowMoreInformation {

    private int time;

    private String[] text;

    private int x;
    private int y;

    public ShowMoreInformation() {
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setTimer(int time, String... text) {
        this.text = text;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public String[] getText() {
        return text;
    }

    public void doThink(float delta) {
        if (this.time > 0) {
            this.time -= (int)(delta);
        }
    }

    public void render(MainPanel mainPanel) {
        if (this.text.length == 1) {
            mainPanel.spriteBatch.draw(AssetLoader.scroll, x - 300, y, 600, 70);

            mainPanel.getGlyphLayout().setText(AssetLoader.font20, this.text[0]);
            if (mainPanel.getGlyphLayout().width > 500) {
                mainPanel.drawString(this.text[0], x, y + 20, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
            } else {
                mainPanel.drawString(this.text[0], x, y + 20, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
            }
        }
    }
}
