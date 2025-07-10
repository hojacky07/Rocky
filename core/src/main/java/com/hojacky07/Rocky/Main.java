package com.hojacky07.Rocky;

import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }
}
