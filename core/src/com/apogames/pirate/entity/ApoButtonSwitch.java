package com.apogames.pirate.entity;

import com.apogames.pirate.backend.GameScreen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Apo button switch.
 */
public class ApoButtonSwitch extends ApoButton {

	private TextureRegion switchImage;

    /**
     * Instantiates a new Apo button switch.
     *
     * @param x           the x
     * @param y           the y
     * @param width       the width
     * @param height      the height
     * @param function    the function
     * @param text        the text
     * @param image       the image
     * @param switchImage the switch image
     */
    public ApoButtonSwitch(int x, int y, int width, int height, String function, String text, TextureRegion image, TextureRegion switchImage) {
		super(x, y, width, height, function, text);
		this.setImage(image);
		this.switchImage = switchImage;
	}

	public void renderImage(GameScreen screen, int changeX, int changeY) {
		if (this.isSelect()) {
			screen.spriteBatch.draw(this.switchImage, this.getX() + changeX + this.getWidth()/2 - this.switchImage.getRegionWidth()/2f, this.getY() + changeY + this.getHeight()/2 - this.switchImage.getRegionHeight()/2f);
		} else {
			screen.spriteBatch.draw(this.getImage(), this.getX() + changeX + this.getWidth()/2 - this.getImage().getRegionWidth()/2f, this.getY() + changeY + this.getHeight()/2 - this.getImage().getRegionHeight()/2f);
		}
	}
	
}
