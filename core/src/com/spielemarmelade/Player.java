package com.spielemarmelade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {

    Texture texture = new Texture(Gdx.files.internal("player.png"));
    Sprite sprite = new Sprite(texture);
    Rectangle hitbox = new Rectangle();
    Boolean playerRight = true;

    Vector2 velocity = new Vector2(0, 0);

    public Player() {
        hitbox.x = 500;
        hitbox.y = 500;
        hitbox.width = 200;
        hitbox.height = 200;
    }

    Boolean isJumping = false;
    Boolean isWalking = false;

    public void updateInput() {
        int movementSpeed = 18;


        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (velocity.x < 0) {
                if (!isJumping)
                    velocity.x = 0;
            }

            if (!playerRight){
                sprite.flip(true, false);
                playerRight = true;
            }
            isWalking = true;
            velocity.x += movementSpeed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (velocity.x > 0) {
                if (!isJumping)
                    velocity.x = 0;
            }

            if (playerRight){
                sprite.flip(true, false);
                playerRight = false;
            }

            isWalking = true;
            velocity.x -= movementSpeed * Gdx.graphics.getDeltaTime();
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && isWalking && !isJumping) {
            velocity.x = 0;
        }
        int da = 70;
        if (velocity.x > da * movementSpeed * Gdx.graphics.getDeltaTime()) {
            velocity.x = da * movementSpeed * Gdx.graphics.getDeltaTime();
        }

        if (velocity.x < -da * movementSpeed * Gdx.graphics.getDeltaTime()) {
            velocity.x = -da * movementSpeed * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (!isJumping) {
                velocity.y += 70 * movementSpeed * Gdx.graphics.getDeltaTime();
                isJumping = true;
            }
        }

        int gravity = -70;
        velocity.y += gravity * Gdx.graphics.getDeltaTime();

        hitbox.x += velocity.x;
        hitbox.y += velocity.y;
        if (hitbox.y <= 0) {
            hitbox.y = 0;
            velocity.y = 0;
            isJumping = false;
        }
    }


    public void dispose() {
        texture.dispose();
    }
}