package com.hojacky07.Rocky;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private Texture walkSheet, runSheet, idleSheet, jumpSheet;
    private TextureRegion[] walkFrames, runFrames, idleFrames, jumpFrames;
    private TextureRegion[] currentFrames;

    private int currentFrame = 0;
    private float frameTime = 0;
    private float frameDuration = 0.1f;

    private float x, y;
    private float velocityY = 0;
    private boolean onGround = false;

    private float speed;
    private boolean facingLeft = false;

    private final float walkSpeed = 200;
    private final float runSpeed = 650;
    private final float speedThreshold = 350;

    private final float gravity = -800;
    private final float jumpVelocity = 350;

    private final float groundLevel = 16;

    public Player(String idlePath, String walkPath, String runPath, String jumpPath, int frameCount, float startX, float startY) {
        idleSheet = new Texture(Gdx.files.internal(idlePath));
        walkSheet = new Texture(Gdx.files.internal(walkPath));
        runSheet = new Texture(Gdx.files.internal(runPath));
        jumpSheet = new Texture(Gdx.files.internal(jumpPath));

        idleFrames = new TextureRegion[4];
        walkFrames = new TextureRegion[frameCount];
        runFrames = new TextureRegion[frameCount];
        jumpFrames = new TextureRegion[6];

        for (int i = 0; i < 4; i++)
            idleFrames[i] = new TextureRegion(idleSheet, i * 80, 0, 80, 80);
        for (int i = 0; i < frameCount; i++) {
            walkFrames[i] = new TextureRegion(walkSheet, i * 80, 0, 80, 80);
            runFrames[i] = new TextureRegion(runSheet, i * 80, 0, 80, 80);
        }
        for (int i = 0; i < 6; i++)
            jumpFrames[i] = new TextureRegion(jumpSheet, i * 80, 0, 80, 80);

        currentFrames = idleFrames;
        x = startX;
        y = startY;
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        boolean moving = false;
        boolean isRunning = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        float acceleration = 300f;
        float friction = 600f;

        float maxSpeed = isRunning ? runSpeed : walkSpeed;

        boolean pressingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean pressingLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);

        // Reset speed if switching direction to avoid sliding
        if ((speed > 0 && pressingLeft) || (speed < 0 && pressingRight)) {
            speed = 0;
        }

        if (pressingRight) {
            speed += acceleration * delta;
            speed = Math.min(speed, maxSpeed);
            facingLeft = false;
            moving = true;
        } else if (pressingLeft) {
            speed -= acceleration * delta;
            speed = Math.max(speed, -maxSpeed);
            facingLeft = true;
            moving = true;
        } else {
            // Apply friction when no keys pressed
            if (speed > 0) {
                speed -= friction * delta;
                if (speed < 0.1f) speed = 0;
            } else if (speed < 0) {
                speed += friction * delta;
                if (speed > -0.1f) speed = 0;
            }
            // Clamp speed again after friction
            speed = Math.max(Math.min(speed, maxSpeed), -maxSpeed);
        }

        // Update position
        x += speed * delta;

        // Jump input
        if (onGround && (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            velocityY = jumpVelocity;
            onGround = false;
        }

        // Apply gravity
        velocityY += gravity * delta;
        y += velocityY * delta;

        // Ground collision
        if (y <= groundLevel) {
            y = groundLevel;
            velocityY = 0;
            onGround = true;
        }

        // Animation logic
        TextureRegion[] previousFrames = currentFrames;

        if (!onGround) {
            currentFrames = jumpFrames;
        } else if (Math.abs(speed) > 0.1f) {
            if (Math.abs(speed) > speedThreshold)
                currentFrames = runFrames;
            else
                currentFrames = walkFrames;
        } else {
            currentFrames = idleFrames;
        }

        // Reset animation if changed
        if (currentFrames != previousFrames) {
            currentFrame = 0;
            frameTime = 0;
        }

        // Animate frames
        frameTime += delta;
        if (frameTime >= frameDuration) {
            frameTime = 0;
            currentFrame = (currentFrame + 1) % currentFrames.length;
        }

        float screenWidth = Gdx.graphics.getWidth();
        float playerCollisionWidth = 32;

        // Clamp player's x inside screen bounds for collision box
        if (x < 0) {
            x = 0;
            speed = 0;
        } else if (x > screenWidth - playerCollisionWidth) {
            x = screenWidth - playerCollisionWidth;
            speed = 0;
        }
    }


    public void draw(SpriteBatch batch) {
        TextureRegion region = currentFrames[currentFrame];
        if (facingLeft && !region.isFlipX()) region.flip(true, false);
        if (!facingLeft && region.isFlipX()) region.flip(true, false);
        batch.draw(region, x - 64, y - 48, 160, 160);
    }

    public void dispose() {
        walkSheet.dispose();
        runSheet.dispose();
        idleSheet.dispose();
        jumpSheet.dispose();
    }
}
