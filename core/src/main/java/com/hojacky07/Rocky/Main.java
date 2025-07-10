package com.hojacky07.Rocky;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture libgdx;
    private Texture credit;

    private int currentStep = 0;
    private float alpha = 0;
    private float waitTime = 1;
    private float fadeSpeed = 1;
    private boolean fadingIn = true;
    private boolean waiting = false;
    private boolean finished = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        libgdx = new Texture("libgdx.png");
        credit = new Texture("credit.png");
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        if (finished) return;

        batch.begin();

        if (currentStep == 0) {
            if (fadeImage(libgdx)) {
                currentStep = 1;
                resetFade();
            }
        } else if (currentStep == 1) {
            if (fadeImage(credit)) {
                finished = true;
            }
        }

        batch.end();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        
    }

    private boolean fadeImage(Texture img) {
        float delta = Gdx.graphics.getDeltaTime();

        if (fadingIn) {
            alpha += delta * fadeSpeed;
            if (alpha >= 1) {
                alpha = 1;
                fadingIn = false;
                waiting = true;
                waitTime = 2;
            }
        } else if (waiting) {
            waitTime -= delta;
            if (waitTime <= 0) {
                waiting = false;
            }
        } else {
            alpha -= delta * fadeSpeed;
            if (alpha <= 0) {
                alpha = 0;
                return true;
            }
        }

        batch.setColor(1, 1, 1, alpha);
        batch.draw(img, 0, 0);
        batch.setColor(1, 1, 1, 1);

        return false;
    }

    private void resetFade() {
        alpha = 0;
        fadingIn = true;
        waiting = false;
    }

    @Override
    public void dispose() {
        batch.dispose();
        libgdx.dispose();
        credit.dispose();
    }
}
