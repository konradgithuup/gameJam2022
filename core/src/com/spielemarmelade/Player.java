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
    int spriteWidth = 200;
    int spriteHeight = 200;
    int spriteOffsetX = -40;
    int spriteOffsetY = -15;
    Boolean isInAir = false;
    int movementSpeed = 18;

    Vector2 velocity = new Vector2(0, 0);

    public Player() {
        hitbox.x = 500;
        hitbox.y = 500;
        hitbox.width = 110;
        hitbox.height = 160;
    }


    public void updateInput(LevelBox box) {

        xMovement();

        yMovement();

        /*
        Rectangle futureHitbox = new Rectangle();
        futureHitbox.x = hitbox.x + velocity.x;
        futureHitbox.y = hitbox.y + velocity.y;
        if (futureHitbox.y < 0) {
            futureHitbox.y = 0;
        }

        if(box.hitbox.overlaps(futureHitbox)){
            System.out.println("OVERLAP");
            velocity.x = 0;
            velocity.y = 0;
        }
         */

        hitbox.x += velocity.x;
        hitbox.y += velocity.y;
        if (hitbox.y <= 0) {
            hitbox.y = 0;
            velocity.y = 0;
            isInAir = false;
        }
    }

    public void xMovement(){
        //if both buttons are pressed and we are on the ground => STOP
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.A)){
            if(!isInAir){
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
            if (playerRight){
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

    public void yMovement(){
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