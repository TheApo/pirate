package com.apogames.pirate.game.treasure.help;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.game.MainPanel;

public class ShowTimeInformation {

    private int time;

    private String[] text;

    public ShowTimeInformation() {

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
            mainPanel.spriteBatch.draw(AssetLoader.scroll, 20, 10, Constants.GAME_WIDTH - 40, 80);
            mainPanel.drawString(this.text[0], Constants.GAME_WIDTH / 2f, 35, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, false, false);
        } else {
            mainPanel.spriteBatch.draw(AssetLoader.scroll, 20, 10, Constants.GAME_WIDTH - 40, 100);
            mainPanel.drawString(this.text[0], Constants.GAME_WIDTH / 2f, 30, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
            mainPanel.drawString(this.text[1], Constants.GAME_WIDTH / 2f, 55, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        }
    }
}
