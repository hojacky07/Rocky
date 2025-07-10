package com.hojacky07.Rocky;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture[] images;
    private int currentIndex = 4;

    private float alpha = 0;
    private float waitTime = 2;
    private final float fadeSpeed = 1;

    private enum Phase { FADE_IN, WAIT, FADE_OUT }
    private Phase phase = Phase.FADE_IN;

    public LoadingScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        images = new Texture[] {
            new Texture("libgdx.png"),
            new Texture("credit.png"),
            new Texture("chatgpt.png")
        };
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        if (currentIndex >= images.length) {
            game.setScreen(new GameScreen(game));
            dispose();
            return;
        }

        Texture currentTexture = images[currentIndex];

        switch(phase) {
            case FADE_IN:
                alpha += delta * fadeSpeed;
                if (alpha >= 1) {
                    alpha = 1;
                    phase = Phase.WAIT;
                    waitTime = 2;
                }
                break;

            case WAIT:
                waitTime -= delta;
                if (waitTime <= 0) {
                    phase = Phase.FADE_OUT;
                }
                break;

            case FADE_OUT:
                alpha -= delta * fadeSpeed;
                if (alpha <= 0) {
                    alpha = 0;
                    phase = Phase.FADE_IN;
                    currentIndex++;
                }
                break;
        }

        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(currentTexture, 0, 0);
        batch.setColor(1, 1, 1, 1);
        batch.end();
    }

    @Override
    public void resize(int width, int height) { }
    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }
    @Override
    public void show() { }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture tex : images) {
            tex.dispose();
        }
    }
}
