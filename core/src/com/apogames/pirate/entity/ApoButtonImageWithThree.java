package com.apogames.pirate.entity;

import com.apogames.pirate.Constants;
import com.apogames.pirate.backend.DrawString;
import com.apogames.pirate.backend.GameScreen;
import com.apogames.pirate.common.Localization;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Apo button image.
 */
public class ApoButtonImageWithThree extends ApoButton {

	private TextureRegion[] images;

	private TextureRegion mouseOverTextureRegion;
	private String mouseOverText;
	private String mouseOverTextKey;
	private Object[] mouseOverTextArgs;

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
		this.mouseOverTextKey = null;
		this.mouseOverTextArgs = null;
		this.mouseOverTextBottom = mouseOverTextBottom;
	}

	public void setMouseOverTextKey(TextureRegion mouseOverTextureRegion, String key, Object... args) {
		this.mouseOverTextureRegion = mouseOverTextureRegion;
		this.mouseOverTextKey = key;
		this.mouseOverTextArgs = args;
		this.mouseOverText = "";
		this.mouseOverTextBottom = false;
	}

	public void setMouseOverTextKeyBottom(TextureRegion mouseOverTextureRegion, String key, Object... args) {
		this.mouseOverTextureRegion = mouseOverTextureRegion;
		this.mouseOverTextKey = key;
		this.mouseOverTextArgs = args;
		this.mouseOverText = "";
		this.mouseOverTextBottom = true;
	}

	private String resolvedMouseOverText() {
		if (this.mouseOverTextKey != null) {
			if (this.mouseOverTextArgs != null && this.mouseOverTextArgs.length > 0) {
				return Localization.format(this.mouseOverTextKey, this.mouseOverTextArgs);
			}
			return Localization.get(this.mouseOverTextKey);
		}
		return this.mouseOverText;
	}

	public void render(GameScreen screen, int changeX, int changeY, boolean needNewSpriteBatch) {
		if (this.isVisible()) {
			if (needNewSpriteBatch) {
				screen.spriteBatch.begin();
				screen.spriteBatch.enableBlending();
			}
			renderImage(screen, changeX, changeY);
			String hover = resolvedMouseOverText();
			if (hover != null && hover.length() > 0 && this.isBOver()) {
				screen.getGlyphLayout().setText(this.getFont(), hover);
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
				screen.drawString(hover, x + 10 + width/2f, y, Constants.COLOR_WHITE, this.getFont(), DrawString.MIDDLE, false, false);
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
