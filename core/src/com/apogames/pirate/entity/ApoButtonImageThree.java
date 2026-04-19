package com.apogames.pirate.entity;

import com.apogames.pirate.backend.GameScreen;

/**
 * The type Apo button image three.
 */
public class ApoButtonImageThree extends ApoButton {

	private int assetX = 0;
	private int assetY = 0;

	private int picWidth = 0;
	private int picHeight = 0;

    /**
     * Instantiates a new Apo button image three.
     *
     * @param x        the x
     * @param y        the y
     * @param width    the width
     * @param height   the height
     * @param function the function
     * @param text     the text
     * @param assetX   the asset x
     * @param assetY   the asset y
     */
    public ApoButtonImageThree(int x, int y, int width, int height, String function, String text, int assetX, int assetY) {
		this(x, y, width, height, function, text, assetX, assetY, width, height);
	}

    /**
     * Instantiates a new Apo button image three.
     *
     * @param x         the x
     * @param y         the y
     * @param width     the width
     * @param height    the height
     * @param function  the function
     * @param text      the text
     * @param assetX    the asset x
     * @param assetY    the asset y
     * @param picWidth  the pic width
     * @param picHeight the pic height
     */
    public ApoButtonImageThree(int x, int y, int width, int height, String function, String text, int assetX, int assetY, int picWidth, int picHeight) {
		super(x, y, width, height, function, text);
		
		this.assetX = assetX;
		this.assetY = assetY;

		this.picWidth = picWidth;
		this.picHeight = picHeight;
	}

    /**
     * Gets asset x.
     *
     * @return the asset x
     */
    public int getAssetX() {
		return assetX;
	}

    /**
     * Sets asset x.
     *
     * @param assetX the asset x
     */
    public void setAssetX(int assetX) {
		this.assetX = assetX;
	}

    /**
     * Gets asset y.
     *
     * @return the asset y
     */
    public int getAssetY() {
		return assetY;
	}

    /**
     * Sets asset y.
     *
     * @param assetY the asset y
     */
    public void setAssetY(int assetY) {
		this.assetY = assetY;
	}

	public void render(GameScreen screen, int changeX, int changeY, boolean bShowTextOnly ) {
		if ( this.isVisible() ) {
			screen.spriteBatch.begin();
			screen.spriteBatch.enableBlending();
			renderImage(screen, changeX, changeY);
			screen.spriteBatch.end();
		}
	}

	public void renderImage(GameScreen screen, int changeX, int changeY) {
		int currentX = assetX;
		if (this.isBOver()) {
			currentX = assetX + 1;
		}
		if (this.isBPressed()) {
			currentX = assetX + 2;
		}
		float x = this.getX() + changeX;
		if (picWidth != getWidth()) {
			x = getXMiddle() - picWidth/2 + changeX;
		}
		float y = this.getY() + changeY;
		if (picHeight != getHeight()) {
			y = getY() + getHeight()/2 - picHeight/2 + changeY;
		}
//		screen.spriteBatch.draw(AssetLoader.buttonTextureRegion[assetY][currentX], x, y, picWidth, picHeight);
	}
}
