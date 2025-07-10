package com.hojacky07.Rocky;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Player player;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        player = new Player("idle.png", "walk.png", "run.png", "jump.png", 8, 160, 160);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.39f,0.71f,0.85f,1);
        player.update();
        batch.begin();
        player.draw(batch);
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
