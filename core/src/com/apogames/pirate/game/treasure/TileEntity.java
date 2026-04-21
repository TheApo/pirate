package com.apogames.pirate.game.treasure;

import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.entity.ApoEntity;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class TileEntity extends ApoEntity {

    private static final int DIRECTION_RIGHT = 0;
    private static final int DIRECTION_DOWN = 1;
    private static final int DIRECTION_LEFT = 2;
    private static final int DIRECTION_UP = 3;

    private final int animal;
    private int direction;
    private boolean needNewGoal;
    private float goalX = -1.0F;
    private float goalY = -1.0F;
    private Rectangle goalRec;
    private int stayTime;

    public TileEntity(float x, float y, float width, float height, int animal) {
        super(x, y, width, height);
        this.animal = animal;
        this.stayTime = (int) (Math.random() * 5000.0);
    }

    public boolean isNeedNewGoal() {
        return this.needNewGoal;
    }

    public void setNewGoal(Polygon polygon, int tileSize) {
        this.setNewGoal(polygon, tileSize, false);
    }

    public void setNewGoal(Polygon polygon, int tileSize, boolean setStartValues) {
        Rectangle rec = polygon.getBoundingRectangle();

        float newGoalX;
        float newGoalY;
        do {
            newGoalX = (float) (Math.random() * (rec.width - this.getWidth() * tileSize / 256f));
            newGoalY = (float) (Math.random() * (rec.height - this.getHeight() * tileSize / 256f));
        } while (!polygon.contains(newGoalX, newGoalY));

        this.goalX = newGoalX;
        this.goalY = newGoalY;
        this.goalRec = new Rectangle(newGoalX - 1.5F, newGoalY - 1.5F, 3.0F, 3.0F);
        this.setDirection(this.getX(), this.getY(), this.goalX, this.goalY);
        this.needNewGoal = false;
        this.stayTime = (int) (Math.random() * 10000.0) + 1000;
        if (setStartValues) {
            this.setX(this.goalX);
            this.setY(this.goalY);
            this.setNewGoal(polygon, tileSize, false);
        }
    }

    private void setDirection(float x, float y, float goalX, float goalY) {
        if (Math.abs(x - goalX) > Math.abs(y - goalY)) {
            this.direction = DIRECTION_LEFT;
            if (x < goalX) {
                this.direction = DIRECTION_RIGHT;
            }
        } else {
            this.direction = DIRECTION_DOWN;
            if (y < goalY) {
                this.direction = DIRECTION_UP;
            }
        }
        this.setVec();
    }

    private void setVec() {
        this.setVecX(0.0F);
        this.setVecY(0.0F);
        float difX = Math.abs(this.getX() - this.goalX);
        float difY = Math.abs(this.getY() - this.goalY);
        float vec = 0.02F + (float) (Math.random() * 0.03);
        if (difX != 0.0F) {
            this.setVecX(-vec * (this.getX() - this.goalX) / (difX + difY));
        }
        if (difY != 0.0F) {
            this.setVecY(-vec * (this.getY() - this.goalY) / (difX + difY));
        }
    }

    public void doThink(float delta, int tileSize) {
        if (this.stayTime > 0) {
            this.stayTime = (int) (this.stayTime - delta);
            return;
        }
        if (this.getVecX() != 0.0F || this.getVecY() != 0.0F) {
            this.setX(this.getX() + this.getVecX() * delta * tileSize / 256f);
            this.setY(this.getY() + this.getVecY() * delta * tileSize / 256f);
            if (this.goalRec.contains(this.getX(), this.getY())) {
                this.needNewGoal = true;
            }
        }
    }

    public void render(MainPanel mainPanel, int changeX, int changeY, int tileSize) {
        mainPanel.spriteBatch.draw(
                AssetLoader.animals[this.animal][this.direction],
                this.getX() * tileSize / 256f + changeX,
                this.getY() * tileSize / 256f + changeY,
                tileSize * this.getWidth() / 256f,
                295f / 256f * tileSize * this.getHeight() / 256f);
    }
}
