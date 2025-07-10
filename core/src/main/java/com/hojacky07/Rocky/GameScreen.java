package com.hojacky07.Rocky;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Player player;
    Texture tileset;
    TextureRegion floorTile;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        player = new Player("idle.png", "walk.png", "run.png", "jump.png", 8, 160, 160);
        // Load from the assets folder
        tileset = new Texture(Gdx.files.internal("tileset.png"));

        // Split the tileset into 32x32 tiles (adjust size if different)
        TextureRegion[][] regions = TextureRegion.split(tileset, 32, 32);
        
        // Use the top-left tile (0,0) as the ground tile
        floorTile = regions[0][0];
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.39f,0.71f,0.85f,1);
        player.update();
        batch.begin();
        player.draw(batch);
        int tileSize = 32;
        int tilesAcross = Gdx.graphics.getWidth() / tileSize;

        for (int i = 0; i < tilesAcross; i++) {
            batch.draw(floorTile, i * tileSize, 0);
        }
        batch.end();
    }

    // Implement other required Screen methods:

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void show() { }
    @Override public void dispose() {
        batch.dispose();
        player.dispose();
    }
}
