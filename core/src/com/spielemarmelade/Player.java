package com.spielemarmelade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.w3c.dom.Text;

import java.util.Arrays;

enum BlockingPlayerState {
    NONE,
    STAGGERED,
    ATTACKING;
}

public class Player {

    private final Animation<TextureRegion> attackAnimation;
    int health = 100;

    BlockingPlayerState blockingPlayerState = BlockingPlayerState.NONE;
    static int JUMP_FACTOR = 100;
    Animation<TextureRegion> runningAnimation;
    Sprite idle;
    Sprite jumping;

    Animation<TextureRegion> blockingAnimation;
    Rectangle hitbox = new Rectangle();
    Rectangle attackHitbox = new Rectangle();
    Boolean playerRight = true;
    int spriteWidth = 300;
    int spriteHeight = 300;
    Boolean isInAir = false;
    int movementSpeed = 18;
    Vector2 velocity = new Vector2(0, 0);
    Rectangle hitboxNextTick = new Rectangle();
    TextureRegion[][] runningFrames;
    TextureRegion[] runFrames;
    float attackStartTime = -1;
    float blockingStartTime = -1;

    public Player() {
        hitbox.x = 500;
        hitbox.y = 500;
        hitbox.width = spriteWidth*0.4f;
        hitbox.height = spriteHeight*0.65f;

        attackHitbox.x = hitbox.x + hitbox.width;
        attackHitbox.y = hitbox.y + hitbox.height/4;
        attackHitbox.width = hitbox.width;
        attackHitbox.height = hitbox.height;

        hitboxNextTick.width = hitbox.width;
        hitboxNextTick.height = hitbox.height;

        idle = new Sprite(new Texture(Gdx.files.internal("player/idle.png")));
        jumping = new Sprite(new Texture(Gdx.files.internal("player/jumping.png")));

        Texture runningTexture = new Texture(Gdx.files.internal("player/spritesheet_running.png"));
        runningFrames = TextureRegion.split(runningTexture, runningTexture.getWidth()/3, runningTexture.getHeight());
        runFrames = new TextureRegion[3];

        for(int x = 0; x < 3; x++)
            runFrames[x] = runningFrames[0][x];

        this.runningAnimation = new Animation<>(0.07f, runFrames);

        Texture attackingSpriteSheet = new Texture((Gdx.files.internal("player/spritesheet_attacking.png")));
        TextureRegion[][] attackingFrames = TextureRegion.split(
                attackingSpriteSheet,
                attackingSpriteSheet.getWidth()/2,
                attackingSpriteSheet.getHeight()/2);

        TextureRegion[] frames = new TextureRegion[4];

        int mapping = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                frames[mapping] = attackingFrames[i][j];
                mapping++;
            }
        }
        this.attackAnimation = new Animation<>(0.07f, frames);

        Texture slicedTexture = new Texture(Gdx.files.internal("player/spritesheet_blocking.png"));
        TextureRegion[][] slicedFrames = TextureRegion.split(slicedTexture, slicedTexture.getWidth() / 5, slicedTexture.getHeight());
        TextureRegion[] slcFrames = new TextureRegion[5];

        for (int x = 0; x < 5; x++)
            slcFrames[x] = slicedFrames[0][x];

        this.blockingAnimation = new Animation<>(0.07f, slcFrames);
    }


    public TextureRegion deriveSpriteFromCurrentState(float time) {

        System.out.println(blockingPlayerState);

        if (this.attackStartTime < 0 && this.blockingPlayerState == BlockingPlayerState.ATTACKING) {
            this.attackStartTime = time;
        }

        if (this.attackAnimation.isAnimationFinished(time - attackStartTime) && this.blockingPlayerState != BlockingPlayerState.STAGGERED) {
            this.attackStartTime = -1;
            this.blockingPlayerState = BlockingPlayerState.NONE;
        }

        if (this.blockingPlayerState == BlockingPlayerState.ATTACKING)
            return this.attackAnimation.getKeyFrame(time, true);

        if (this.blockingStartTime < 0 && this.blockingPlayerState == BlockingPlayerState.STAGGERED) {
            this.blockingStartTime = time;
        }

        if (this.blockingAnimation.isAnimationFinished(time - blockingStartTime)) {
            this.blockingStartTime = -1;
            this.blockingPlayerState = BlockingPlayerState.NONE;
        }

        if (this.blockingPlayerState == BlockingPlayerState.STAGGERED)
            return this.blockingAnimation.getKeyFrame(time, true);

        if (isInAir) return this.jumping;

        if (this.velocity.x != 0) return this.runningAnimation.getKeyFrame(time, true);

        return this.idle;
    }

    public void setStaggered() {
        this.blockingPlayerState = BlockingPlayerState.STAGGERED;
    }

    public void updateInput(Level level) {

        setBlockingPlayerState();

        if (this.blockingPlayerState != BlockingPlayerState.NONE) return;

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

            velocity.x = 0;
        }

        hitboxNextTick.x = hitbox.x;
        hitboxNextTick.y = hitbox.y  + velocity.y;

        if (hitboxNextTick.overlaps(box.hitbox)) {

            if(velocity.y < 0)
                isInAir = false;
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

        if (playerRight) {
            attackHitbox.y = hitboxNextTick.y + hitbox.height/4;
            attackHitbox.x = hitboxNextTick.x + hitbox.width;
        } else {
            attackHitbox.y = hitboxNextTick.y + hitbox.height/4;
            attackHitbox.x = hitboxNextTick.x - hitbox.width;
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
                this.updateDirection();
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
                this.updateDirection();
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

    float lastHpReduceTime = 0;
    public void updateHp(float time){
        if(time > lastHpReduceTime + 0.1f){
            if(health > 0){

                health--;
                lastHpReduceTime = time;
            }
        }
    }

    public void yMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (!isInAir) {
                velocity.y += JUMP_FACTOR * movementSpeed * Gdx.graphics.getDeltaTime();
                isInAir = true;
            }
        }

        int gravity = -70;
        velocity.y += gravity * Gdx.graphics.getDeltaTime();
    }


    public void setBlockingPlayerState() {

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            blockingPlayerState = BlockingPlayerState.ATTACKING;
        }
    }


    public void dispose() {
        // pass (player character wont be disposed of until death of process, so no need for cleaning up)
    }


    private void updateDirection() {

        idle.flip(true, false);
        jumping.flip(true, false);
        Arrays.stream(runningAnimation.getKeyFrames()).forEach(r -> r.flip(true, false));
        Arrays.stream(blockingAnimation.getKeyFrames()).forEach(r -> r.flip(true, false));
        Arrays.stream(attackAnimation.getKeyFrames()).forEach(r -> {
            r.flip(true, false);
        });
    }
}