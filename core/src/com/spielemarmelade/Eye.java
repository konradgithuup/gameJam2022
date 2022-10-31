package com.spielemarmelade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class Eye implements Enemy {

    int health = 1;
    int damage = 20;
    Animation<TextureRegion> animation;
    Rectangle hitbox = new Rectangle();
    int spriteWidth = 150;
    int spriteHeight = 150;
    int movementSpeed = 10;
    Vector2 velocity = new Vector2(0, 0);
    Rectangle hitboxNextTick = new Rectangle();
    TextureRegion[][] runningFrames;
    TextureRegion[] runFrames;
    float attackStartTime = -1;

    public Eye(Player target) {
        hitbox.x = 500;
        hitbox.y = 500;
        hitbox.width = spriteWidth * 0.4f;
        hitbox.height = spriteHeight * 0.65f;

        hitboxNextTick.width = hitbox.width;
        hitboxNextTick.height = hitbox.height;

        Texture runningTexture = new Texture(Gdx.files.internal("eye/flying_spritesheet.png"));
        runningFrames = TextureRegion.split(runningTexture, runningTexture.getWidth() / 3, runningTexture.getHeight());
        runFrames = new TextureRegion[3];

        for (int x = 0; x < 3; x++)
            runFrames[x] = runningFrames[0][x];

        this.animation = new Animation<>(0.07f, runFrames);
    }


    public TextureRegion deriveSpriteFromCurrentState(float time) {

        return this.animation.getKeyFrame(time, true);
    }


    public void update(Level level) {

        for (LevelBox box : level.boxes)
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

            velocity.x = 0;
        }

        hitboxNextTick.x = hitbox.x;
        hitboxNextTick.y = hitbox.y + velocity.y;

        if (hitboxNextTick.overlaps(box.hitbox)) {

            velocity.y = 0;
        }

        hitboxNextTick.x = hitbox.x + velocity.x;
        hitboxNextTick.y = hitbox.y + velocity.y;

        if (hitboxNextTick.overlaps(box.hitbox)) {

            hitboxNextTick.x = hitbox.x;
            hitboxNextTick.y = hitbox.y;
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    public void dispose() {
        // pass (player character wont be disposed of until death of process, so no need for cleaning up)
    }
}