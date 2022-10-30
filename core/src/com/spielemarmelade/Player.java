package com.spielemarmelade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.*;
import java.util.stream.Stream;

public class Player {

    Animation<TextureRegion> runningAnimation;
    Texture texture;
    Texture runningTexture;
    Sprite runningSprite;
    Sprite sprite;
    Rectangle hitbox = new Rectangle();
    Boolean playerRight = true;
    int spriteWidth = 300;
    int spriteHeight = 300;
    int spriteOffsetX = -40;
    int spriteOffsetY = -15;
    Boolean isInAir = false;
    int movementSpeed = 18;
    Vector2 velocity = new Vector2(0, 0);
    Rectangle hitboxNextTick = new Rectangle();
    TextureRegion[][] runningFrames;
    TextureRegion[] runFrames;

    public Player() {
        hitbox.x = 500;
        hitbox.y = 500;
        hitbox.width = spriteWidth*0.5f;
        hitbox.height = spriteHeight*0.5f;
        hitboxNextTick.width = hitbox.width;
        hitboxNextTick.height = hitbox.height;

        texture = new Texture(Gdx.files.internal("player.png"));
        sprite = new Sprite(texture);

        runningTexture = new Texture(Gdx.files.internal("spritesheet_running.png"));
        runningFrames = TextureRegion.split(runningTexture, runningTexture.getWidth()/3, runningTexture.getHeight());
        runFrames = new TextureRegion[3];

        for(int x = 0; x < 3; x++)
            runFrames[x] = runningFrames[0][x];

        runningSprite = new Sprite(runningTexture);
        runningAnimation = new Animation<TextureRegion>(0.07f, runFrames);
    }


    public void updateInput(Level level) {

        xMovement();

        yMovement();

        for(LevelBox box : level.boxes)
            collision(box);

        hitboxNextTick.x = hitbox.x + velocity.x;
        hitboxNextTick.y = hitbox.y + velocity.y;

        hitbox.x = hitboxNextTick.x;
        hitbox.y = hitboxNextTick.y;
    }



    private void collision(LevelBox box) {

        hitboxNextTick.x = hitbox.x + velocity.x;
        hitboxNextTick.y = hitbox.y;

        if (hitboxNextTick.overlaps(box.hitbox)) {

            hitboxNextTick.x = hitbox.x;
            velocity.x = 0;
        }

        hitboxNextTick.x = hitbox.x;
        hitboxNextTick.y = hitbox.y  + velocity.y;

        if (hitboxNextTick.overlaps(box.hitbox)) {

            if(velocity.y < 0)
                isInAir = false;
            hitboxNextTick.y = hitbox.y;
            velocity.y = 0;
        }

        hitboxNextTick.x = hitbox.x + velocity.x;
        hitboxNextTick.y = hitbox.y  + velocity.y;

        if (hitboxNextTick.overlaps(box.hitbox)) {

            if(velocity.y < 0)
                isInAir = false;
            hitboxNextTick.x = hitbox.x;
            hitboxNextTick.y = hitbox.y;
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    public void xMovement() {
        //if both buttons are pressed and we are on the ground => STOP
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (!isInAir) {
                velocity.x = 0;
                return;
            }
        }

        //deal with just one input
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //if we are currently walking in the other direction => stop
            if (velocity.x < 0) {
                if (!isInAir)
                    velocity.x = 0;
            }

            //flip the sprite if its facing the wrong way
            if (!playerRight) {
                sprite.flip(true, false);
                playerRight = true;
            }
            velocity.x += movementSpeed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //if we are currently walking in the other direction => stop
            if (velocity.x > 0) {
                if (!isInAir)
                    velocity.x = 0;
            }

            //flip the sprite if its facing the wrong way
            if (playerRight) {
                sprite.flip(true, false);
                playerRight = false;
            }

            velocity.x -= movementSpeed * Gdx.graphics.getDeltaTime();
        }

        //if nothing is pressed and we are on the ground => stop
        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && !isInAir) {
            velocity.x = 0;
        }

        //cap the max movement speed
        int da = 70;
        if (velocity.x > da * movementSpeed * Gdx.graphics.getDeltaTime()) {
            velocity.x = da * movementSpeed * Gdx.graphics.getDeltaTime();
        }

        if (velocity.x < -da * movementSpeed * Gdx.graphics.getDeltaTime()) {
            velocity.x = -da * movementSpeed * Gdx.graphics.getDeltaTime();
        }
    }

    public void yMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (!isInAir) {
                velocity.y += 70 * movementSpeed * Gdx.graphics.getDeltaTime();
                isInAir = true;
            }
        }

        int gravity = -70;
        velocity.y += gravity * Gdx.graphics.getDeltaTime();
    }


    public void dispose() {
        texture.dispose();
    }
}