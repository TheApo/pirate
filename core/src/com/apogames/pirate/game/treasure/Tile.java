package com.apogames.pirate.game.treasure;

import com.apogames.pirate.Constants;
import com.apogames.pirate.asset.AssetLoader;
import com.apogames.pirate.game.MainPanel;
import com.apogames.pirate.game.treasure.enums.Background;
import com.apogames.pirate.game.treasure.enums.ExtraObjective;
import com.apogames.pirate.game.treasure.enums.TileColor;
import com.badlogic.gdx.math.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Tile {

    private final Comparator<TileEntity> tileEntityComparator = new Comparator<TileEntity>() {
        public int compare(TileEntity entity1, TileEntity entity2) {
            return Float.compare(entity1.getY(), entity2.getY());
        }
    };

    private final Background background;

    private TileColor color;

    private ExtraObjective objective;

    private boolean[] correct_guesses;
    private boolean[] incorrect_guesses;

    private final ArrayList<TileEntity> entities = new ArrayList<>();

    public Tile() {
        this.background = Background.getRandomBackground();
        if (Math.random() * 100 > 75) {
            this.objective = ExtraObjective.getRandomObjective();
            TileColor currentColor = TileColor.BLACK;
            if (!isAnimal(this.objective)) {
                currentColor = TileColor.OBJECT_COLORS[(int)(Math.random() * TileColor.OBJECT_COLORS.length)];
            }
            this.color = currentColor;
        } else {
            this.color = null;
            this.objective = null;
        }
    }

    public Tile(Background background) {
        this.background = background;
        this.setObjective(null, null);

        this.resetGuessed();
    }

    public boolean hasIncorrectGuess() {
        for (boolean incorrect_guess : this.incorrect_guesses) {
            if (incorrect_guess)
                return true;
        }
        return false;
    }

    public void resetGuessed() {
        this.correct_guesses = new boolean[Constants.PLAYER_COLORS.length];
        this.incorrect_guesses = new boolean[Constants.PLAYER_COLORS.length];
    }

    public void setObjective(ExtraObjective objective, TileColor tileColor) {
        this.color = tileColor;
        this.objective = objective;
    }

    public boolean hasOnlyCorrectGuess(int maxPlayers) {
        for (int i = 0; i < this.correct_guesses.length && i < maxPlayers; i++) {
            if (!this.correct_guesses[i])
                return false;
        }
        return true;
    }

    public boolean[] getCorrectGuesses() {
        return correct_guesses;
    }

    public boolean[] getIncorrectGuesses() {
        return incorrect_guesses;
    }

    public Background getBackground() {
        return background;
    }

    public TileColor getColor() {
        return color;
    }

    public ExtraObjective getObjective() {
        return objective;
    }

    public void setEntities(int tileSize) {
        if (!isAnimal(this.objective)) {
            return;
        }

        int animal = 0;
        int width = 60;
        int height = 50;
        if (this.objective == ExtraObjective.BEARS) {
            animal = 1;
            width = 50;
        } else if (this.objective == ExtraObjective.WHITE_SHEEP || this.objective == ExtraObjective.BLACK_SHEEP) {
            animal = (this.objective == ExtraObjective.WHITE_SHEEP) ? 2 : 3;
            width = 64;
            height = 64;
        }

        this.entities.clear();
        Polygon poly = this.getPolygonEntity();
        float[] vertices = poly.getVertices();
        for (int i = 0; i < vertices.length; i += 2) {
            TileEntity entity = new TileEntity(vertices[i], vertices[i + 1], width, height, animal);
            entity.setNewGoal(poly, tileSize, true);
            this.entities.add(entity);
        }
    }

    public Polygon getPolygon(int changeX, int changeY, int tileSize) {
        Polygon polygon = new Polygon();
        polygon.setVertices(new float[]{
                changeX + tileSize / 2f, changeY,
                changeX + tileSize, changeY + 0.25f * 295f / 256f * tileSize,
                changeX + tileSize, changeY + 0.75f * 295f / 256f * tileSize,
                changeX + tileSize / 2f, changeY + 295f / 256f * tileSize,
                changeX, changeY + 0.75f * 295f / 256f * tileSize,
                changeX, changeY + 0.25f * 295f / 256f * tileSize});
        return polygon;
    }

    public Polygon getPolygonEntity() {
        Polygon polygon = new Polygon();
        polygon.setVertices(new float[]{
                128f, 29.5f,
                230.4f, 73.75f,
                230.4f, 221.25f,
                128f, 236f,
                0f, 221.25f,
                0f, 73.75f});
        return polygon;
    }

    public void doThink(float delta, int tileSize) {
        if (this.entities.isEmpty()) {
            return;
        }
        for (TileEntity entity : this.entities) {
            entity.doThink(delta, tileSize);
            if (entity.isNeedNewGoal()) {
                entity.setNewGoal(this.getPolygonEntity(), tileSize);
            }
        }
        Collections.sort(this.entities, this.tileEntityComparator);
    }

    public void render(MainPanel mainPanel, int changeX, int changeY, int tileSize) {
        if (this.background == null) {
            return;
        }
        mainPanel.spriteBatch.draw(AssetLoader.tiles[this.background.getAssetNumber()], changeX, changeY, tileSize, 295f / 256f * tileSize);
        if (this.objective == null) {
            return;
        }
        if (!isAnimal(this.objective)) {
            mainPanel.spriteBatch.draw(AssetLoader.objectives[this.color.getAssetNumber()][this.objective.getAssetNumber()], changeX, changeY, tileSize, 295f / 256f * tileSize);
        } else {
            for (TileEntity entity : this.entities) {
                entity.render(mainPanel, changeX, changeY, tileSize);
            }
        }
    }

    // Layout for the guess markers — one slot per player index. Skulls
    // ("cannot be here") sit in a row in the upper half of the tile, X marks
    // ("search here — treasure possible") sit in a row in the lower half.
    // Both rows are placed inside the hex's full-width band (the tile is a
    // pointy-top hexagon that narrows above y≈0.25 and below y≈0.75 of tile
    // height), so markers stay inside the visible hex at every slot.
    //
    // The row is centred on the tile based on the actual player count: with
    // three players the slots sit centrally, with five they span nearly the
    // full tile width. Step is fixed so a given player's slot stays at the
    // same relative position regardless of marker type.
    private static final float MARKER_RADIUS   = 0.095f;   // of tileSize — half of marker size
    private static final float MARKER_STEP     = 0.19f;    // centre-to-centre, fits 5 side by side
    private static final float SKULL_ROW_Y     = 0.35f;    // upper row — rule-out markers
    private static final float X_ROW_Y         = 0.60f;    // lower row — candidate markers

    public void renderFilled(MainPanel mainPanel, int changeX, int changeY, int tileSize, int playerCount) {
        float tileHeight = 295f / 256f * tileSize;
        float skullY = changeY + tileHeight * SKULL_ROW_Y;
        float xY     = changeY + tileHeight * X_ROW_Y;
        float radius = MARKER_RADIUS * tileSize;
        float startFraction = slotStartFraction(playerCount);

        for (int i = 0; i < playerCount && i < this.incorrect_guesses.length; i++) {
            if (!this.incorrect_guesses[i]) continue;
            float cx = changeX + (startFraction + MARKER_STEP * i) * tileSize;
            drawSkull(mainPanel, cx, skullY, radius, Constants.PLAYER_COLORS[i]);
        }
        for (int i = 0; i < playerCount && i < this.correct_guesses.length; i++) {
            if (!this.correct_guesses[i]) continue;
            float cx = changeX + (startFraction + MARKER_STEP * i) * tileSize;
            drawX(mainPanel, cx, xY, radius, Constants.PLAYER_COLORS[i]);
        }
    }

    /** Fraction (of tile width) for the first slot's centre — shifts the
     *  whole row so N slots are centred on the tile.
     *  With playerCount=5: start≈0.12 (nearly full-width). With playerCount=3:
     *  start≈0.31 (centred, leaves margin on both sides). */
    private static float slotStartFraction(int playerCount) {
        if (playerCount <= 1) return 0.5f;
        return (1f - (playerCount - 1) * MARKER_STEP) / 2f;
    }

    /**
     * X mark in the player's colour — "search here, treasure could be on this
     * tile". Three-layer stroke: white halo (outermost) + black backing +
     * player-colour core. The white halo reads against dark tiles (forest,
     * water) where pure-black-on-dark-blue would disappear, the black rim
     * reads against light tiles, and the player-colour core carries the
     * owner identity. Same rim scheme applies to every player so all markers
     * are treated consistently.
     *
     * Public so the tutorial can render sample markers alongside its text.
     */
    public static void drawX(MainPanel panel, float cx, float cy, float r, float[] playerColor) {
        float ext        = r * 0.90f;   // half-length of each diagonal
        float whiteWidth = r * 0.75f;   // outermost halo
        float blackWidth = r * 0.55f;   // black backing
        float colorWidth = r * 0.38f;   // player-colour core

        setColor(panel, Constants.COLOR_WHITE);
        panel.getRenderer().rectLine(cx - ext, cy - ext, cx + ext, cy + ext, whiteWidth);
        panel.getRenderer().rectLine(cx - ext, cy + ext, cx + ext, cy - ext, whiteWidth);

        setColor(panel, Constants.COLOR_BLACK);
        panel.getRenderer().rectLine(cx - ext, cy - ext, cx + ext, cy + ext, blackWidth);
        panel.getRenderer().rectLine(cx - ext, cy + ext, cx + ext, cy - ext, blackWidth);

        setColor(panel, playerColor);
        panel.getRenderer().rectLine(cx - ext, cy - ext, cx + ext, cy + ext, colorWidth);
        panel.getRenderer().rectLine(cx - ext, cy + ext, cx + ext, cy - ext, colorWidth);
    }

    /**
     * Skull silhouette — "treasure cannot be here". Wider cranium on top,
     * narrower jaw below (pear-shaped). Drawn with a white halo + black outline
     * + player-colour fill so the marker stays readable on both very dark and
     * very light tile backgrounds.
     *
     * Public so the tutorial can render sample markers alongside its text.
     */
    public static void drawSkull(MainPanel panel, float cx, float cy, float r, float[] playerColor) {
        float cranR  = r * 0.95f;
        float cranCy = cy - r * 0.15f;
        float jawR   = r * 0.55f;
        float jawCy  = cy + r * 0.55f;

        // White halo — outermost ring so the marker is visible on dark tiles.
        setColor(panel, Constants.COLOR_WHITE);
        panel.getRenderer().circle(cx, cranCy, cranR * 1.10f);
        panel.getRenderer().circle(cx, jawCy,  jawR * 1.15f);

        // Black outline.
        setColor(panel, Constants.COLOR_BLACK);
        panel.getRenderer().circle(cx, cranCy, cranR);
        panel.getRenderer().circle(cx, jawCy,  jawR);

        // Player-colour fill — leaves a thin black rim between fill and white halo.
        setColor(panel, playerColor);
        panel.getRenderer().circle(cx, cranCy, cranR * 0.88f);
        panel.getRenderer().circle(cx, jawCy,  jawR * 0.82f);

        // Eye sockets — intentionally oversized so the skull reads at small scale.
        setColor(panel, Constants.COLOR_BLACK);
        float eyeR  = r * 0.26f;
        float eyeDx = r * 0.32f;
        float eyeCy = cranCy + r * 0.05f;
        panel.getRenderer().circle(cx - eyeDx, eyeCy, eyeR);
        panel.getRenderer().circle(cx + eyeDx, eyeCy, eyeR);

        // Mouth slit between cranium and jaw.
        float mouthW = r * 0.60f;
        float mouthH = r * 0.16f;
        panel.getRenderer().rect(cx - mouthW / 2f, cy + r * 0.22f, mouthW, mouthH);
    }

    private static void setColor(MainPanel panel, float[] c) {
        panel.getRenderer().setColor(c[0], c[1], c[2], c[3]);
    }

    private static boolean isAnimal(ExtraObjective objective) {
        return objective == ExtraObjective.BEARS || objective == ExtraObjective.RED_PANDA
                || objective == ExtraObjective.WHITE_SHEEP || objective == ExtraObjective.BLACK_SHEEP;
    }
}
