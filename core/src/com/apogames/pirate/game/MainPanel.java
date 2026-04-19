package com.apogames.pirate.game;

import com.apogames.pirate.Constants;
import com.apogames.pirate.backend.GameScreen;
import com.apogames.pirate.backend.ScreenModel;
import com.apogames.pirate.backend.io.IOOnlineLibgdx;
import com.apogames.pirate.game.menu.Menu;
import com.apogames.pirate.game.treasure.Treasure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainPanel extends GameScreen {
	
    private Menu menu;
    private Treasure game;

    private float scale = 1;

    private IOOnlineLibgdx ioOnline;
    
    //	private FPSLogger logger = new FPSLogger();

    public MainPanel() {
        super();
        if ((this.getButtons() == null) || (this.getButtons().size() <= 0)) {
            ButtonProvider button = new ButtonProvider(this);
            button.init();
        }

        if (this.menu == null) {
            this.menu = new Menu(this);
        }
        if (this.game == null) {
            this.game = new Treasure(this);
        }
        if (this.ioOnline == null) {
        	this.ioOnline = new IOOnlineLibgdx();
        	loadDate();
        }

        this.changeToMenu();
    }

    public void loadDate() {
    	this.ioOnline.load();
    }
    
    public String saveSolution(final String email, final String solution) {
    	return this.ioOnline.save(email, solution);
    }
    
    public final void changeToMenu() {
        changeModel(menu);
    }

    public final void changeToGame() {
        changeModel(game);
        this.game.setSettings(this.menu.getPlayers(), this.menu.getDifficulty(), this.menu.getSize());
    }

    /**
     * Quit game.
     */
    public final void quitGame() {
        this.saveProperties();
        Gdx.app.exit();
    }

    /**
     * Update level chooser.
     */
    public void saveProperties() {
    }

    private void changeModel(final ScreenModel model) {
        if (this.model != null) {
            this.model.dispose();
        }

        this.model = model;

        this.setButtonsInvisible();
        this.model.setNeededButtonsVisible();
        this.model.init();
    }
    
    public final void setButtonsInvisible() {
    	for (int i = 0; i < this.getButtons().size(); i++) {
            this.getButtons().get(i).setVisible(false);
        }
    }

    public float getScale() {
        return scale;
    }

    public void think(final float delta) {
        super.think(delta);
        if (model != null) model.think(delta);
    }

    public void render(float delta) {
        super.render(delta);

        if (model != null) {
            model.render();
            model.drawOverlay();
        }
    }

    public void renderBackground() {
        this.getRenderer().begin(ShapeType.Filled);
        this.getRenderer().setColor(Constants.COLOR_BACKGROUND[0], Constants.COLOR_BACKGROUND[1], Constants.COLOR_BACKGROUND[2], 1);
        this.getRenderer().rect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        this.getRenderer().end();
    }

}
