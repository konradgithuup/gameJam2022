package com.spielemarmelade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
public class Eye implements Enemy {

    int health = 1;
    float dyingDuration = -1;
    int damage = 15;
    int score = 20;
    Animation<TextureRegion> flyingAnimation;
    Animation<TextureRegion> slicedAnimation;

    private final Player target;
    Rectangle hitbox = new Rectangle();
    int spriteWidth = 150;
    int spriteHeight = 150;
    int movementSpeed = 10;
    boolean disabled = false;

    Vector2 velocity = new Vector2(0, 0);
    Vector2 lookDirection = new Vector2(0, 0);
    Rectangle hitboxNextTick = new Rectangle();
    TextureRegion[][] flyingFrames;
    TextureRegion[] flyFrames;

    public Eye(Player target, float startX, float startY) {

        this.target = target;
        hitbox.x = startX;
        hitbox.y = startY;
        hitbox.width = spriteWidth * 0.75f;
        hitbox.height = spriteHeight * 0.75f;

        hitboxNextTick.width = hitbox.width;
        hitboxNextTick.height = hitbox.height;

        Texture runningTexture = new Texture(Gdx.files.internal("eye/flying_spritesheet.png"));
        flyingFrames = TextureRegion.split(runningTexture, runningTexture.getWidth() / 3, runningTexture.getHeight());
        flyFrames = new TextureRegion[3];

        for (int x = 0; x < 3; x++)
            flyFrames[x] = flyingFrames[0][x];

        this.flyingAnimation = new Animation<>(0.07f, flyFrames);

        Texture slicedTexture = new Texture(Gdx.files.internal("eye/sliced_spritesheet.png"));
        TextureRegion[][] slicedFrames = TextureRegion.split(slicedTexture, slicedTexture.getWidth() / 5, slicedTexture.getHeight());
        TextureRegion[] slcFrames = new TextureRegion[5];

        for (int x = 0; x < 5; x++)
            slcFrames[x] = slicedFrames[0][x];

        this.slicedAnimation = new Animation<>(0.07f, slcFrames);
    }


    public TextureRegion deriveSpriteFromCurrentState(float time) {

        if (this.dyingDuration < 0 && this.health < 1) {
            this.dyingDuration = time;
            this.target.health = (this.target.health + this.score > 100)? 100 : this.target.health + this.score;
        }

        if (this.dyingDuration > 0 && this.slicedAnimation.isAnimationFinished(time - dyingDuration)) {
            this.disabled = true;
            this.dyingDuration = 99;
            return this.slicedAnimation.getKeyFrames()[4];
        }

        if (this.health < 1)
            return this.slicedAnimation.getKeyFrame(time-dyingDuration, false);

        return this.flyingAnimation.getKeyFrame(time, true);
    }


    public void update(Level level) {

        if (health < 1 || this.disabled) return;

        setVelocity();

        for (LevelBox box : level.boxes)
            collision(box);

        hitboxNextTick.x = hitbox.x + velocity.x;
        hitboxNextTick.y = hitbox.y + velocity.y;

        hitbox.x = hitboxNextTick.x;
        hitbox.y = hitboxNextTick.y;
    }


    private void setVelocity() {

        Vector2 direction = new Vector2();
        direction.x = (target.hitbox.x) - this.hitbox.x;
        direction.y = (target.hitbox.y+this.target.hitbox.height/2) - this.hitbox.y;

        this.lookDirection = direction;
        velocity = direction.setLength(this.movementSpeed);
    }


    private void collision(LevelBox box) {

        if (hitboxNextTick.overlaps(target.attackHitbox) && target.blockingPlayerState == BlockingPlayerState.ATTACKING) {
            this.health = 0;
        }

        if (hitboxNextTick.overlaps((target.hitbox)) && target.blockingPlayerState == BlockingPlayerState.NONE) {
            this.disabled = true;
            this.target.setStaggered();
            this.target.health = Math.max(this.target.health - damage, 0);
        }

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