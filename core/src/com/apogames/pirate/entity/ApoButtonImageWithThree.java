package com.apogames.pirate.entity;

import com.apogames.pirate.Constants;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.backend.GameScreen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Apo button image.
 */
public class ApoButtonImageWithThree extends ApoButton {

	private TextureRegion[] images;

	private TextureRegion mouseOverTextureRegion;
	private String mouseOverText;

	private boolean mouseOverTextBottom = false;

    public ApoButtonImageWithThree(int x, int y, int width, int height, String function, String text, TextureRegion[] images) {
		super(x, y, width, height, function, text);
		
		this.images = images;
		this.mouseOverText = "";
	}

	public void setMouseOverText(TextureRegion mouseOverTextureRegion, String mouseOverText) {
		this.setMouseOverText(mouseOverTextureRegion, mouseOverText, false);
	}

	public void setMouseOverText(TextureRegion mouseOverTextureRegion, String mouseOverText, boolean mouseOverTextBottom) {
		this.mouseOverTextureRegion = mouseOverTextureRegion;
		this.mouseOverText = mouseOverText;
		this.mouseOverTextBottom = mouseOverTextBottom;
	}

	public void render(GameScreen screen, int changeX, int changeY, boolean needNewSpriteBatch) {
		if (this.isVisible()) {
			if (needNewSpriteBatch) {
				screen.spriteBatch.begin();
				screen.spriteBatch.enableBlending();
			}
			renderImage(screen, changeX, changeY);
			if (this.mouseOverText.length() > 0 && this.isBOver()) {
				screen.getGlyphLayout().setText(this.getFont(), this.mouseOverText);
				int width = (int)(screen.getGlyphLayout().width);
				int height = 30;
				int x = (int)(this.getXMiddle() + changeX - width/2 - 10);
				int y = (int)(this.getY() + changeY - 3 - height);
				if (x < 5) {
					x = 5;
				} else if (x + width + 20 > Constants.GAME_WIDTH) {
					x = Constants.GAME_WIDTH - width - 20;
				}
				if (y < 10 || this.mouseOverTextBottom) {
					y = (int)(this.getY() + changeY + 3 + this.getHeight());
				}
				screen.spriteBatch.draw(this.mouseOverTextureRegion, x, y, width + 20, height);
//				screen.drawString(this.mouseOverText, x + 11 + width/2f, y + 1, Constants.COLOR_WHITE, this.getFont(), DrawString.MIDDLE, false, false);
				screen.drawString(this.mouseOverText, x + 10 + width/2f, y, Constants.COLOR_WHITE, this.getFont(), DrawString.MIDDLE, false, false);
			}
			if (needNewSpriteBatch) {
				screen.spriteBatch.end();
			}
		}
	}

	public void renderOutline(GameScreen screen, int changeX, int changeY) {
	}

	public void renderImage(GameScreen screen, int changeX, int changeY) {
		if (this.isBPressed() || this.isSelect()) {
			screen.spriteBatch.draw(this.images[2], this.getX() + changeX, this.getY() + changeY, getWidth(), getHeight());
		} else if (this.isBOver()) {
			screen.spriteBatch.draw(this.images[1], this.getX() + changeX, this.getY() + changeY, getWidth(), getHeight());
		} else {
			screen.spriteBatch.draw(this.images[0], this.getX() + changeX, this.getY() + changeY, getWidth(), getHeight());
		}
	}
}
