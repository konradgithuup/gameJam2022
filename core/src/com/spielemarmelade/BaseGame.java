package com.spielemarmelade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseGame extends ApplicationAdapter {
	SpriteBatch batch;
	Player player;
	OrthographicCamera camera;
	static float cameraDeltaX = 0;
	static float cameraDeltaY = 0;
	ShapeRenderer shapeRenderer;

	EnemyManager manager;
	int sceneTransition = 0;
	static final int CAMERA_SLIDE_SPEED = 12;
	static final int CAMERA_TOP_BOUND = (int)(2160/1.7);
	static final int CAMERA_BOTTOM_BOUND = 900;
	boolean flag = false;
	Level level;
	List<Level> backgroundLevel;

	float time = 0;

	private DeathHud deathHud;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		player = new Player();
		manager = new EnemyManager(player);
		this.level = new Level("level/foreground_front.png", 0);
		level.addLevelboxes(new LevelBox[]{new LevelBox(1300, 180, 0, 0),
				new LevelBox(6000, 84, -1000, 0),
				new LevelBox(300, 3000, -300, 0), // right border
				new LevelBox(300, 1370, 3745, 0), // right wall
				new LevelBox(40, 2000, 45, 792-2000), // lower tree trunk
				new LevelBox(37, 690, 27, 2160-690), // upper tree trunk
				new LevelBox(175, 34, 1557, 384), // wood step
				new LevelBox(835, 64, 1720, 574-64), // mud white arc
				new LevelBox(750, 73, 2768, 853-73), // mud red arc
				new LevelBox(470, 60, 25, 2076-60), //Decke
				new LevelBox(1060, 1670, 25, 2179), //Decke
				new LevelBox(446, 85, 0, 870-85), // leafs
				new LevelBox(940, 240, 0, 1110-240), // leafs
				new LevelBox(1160, 140, 0, 1250-140), // leafs
				new LevelBox(675, 230, 0, 1480-230), // leafs
				new LevelBox(134, 70, 1528, 1200-70), // left cloud
				new LevelBox(796, 85, 1902, 1460-85), // middle cloud
				new LevelBox(240, 73, 3128, 1488-85), // right cloud
				new LevelBox(57, 90, 3690, 1100-90), // right wall stone
				new LevelBox(300, 3000, 3840, 0), // right wall
		});
		Level levelSky = new Level("level/sky.png", 0.8f);
		Level levelHorizon = new Level("level/horizon.png", 0.25f);
		Level levelBackForegroundLayer = new Level("level/foreground_back.png", 0);
		this.backgroundLevel = new ArrayList<>();
		this.backgroundLevel.addAll(Arrays.asList(levelSky, levelHorizon, levelBackForegroundLayer));

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.zoom = 1.6f;
		camera.setToOrtho(false, 1920, 1080);
		batch = new SpriteBatch();
		flag = false;

	}

	@Override
	public void render () {
		if (flag) return;
		ScreenUtils.clear(0,0.02f,0.07f, 1);
		time += Gdx.graphics.getDeltaTime();


		player.updateInput(level);
		manager.update(time, cameraDeltaX, cameraDeltaY);
		updateEnemies();
		updateCamera();
		Interface.updateHp(player.health);
		player.updateHp(time);
		updateEnvironmentAssets();



		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.begin();
		//batch.draw(player.runningSprite, player.hitbox.x + player.spriteOffsetX, player.hitbox.y + player.spriteOffsetY, player.spriteWidth, player.spriteHeight);



		// render level background
		for (Level l : this.backgroundLevel) {
			batch.draw(l.sprite, l.sprite.getX(), l.sprite.getY(), l.sprite.getWidth(), l.sprite.getHeight());
		}

		// render character
		TextureRegion currentPlayerFrame = player.deriveSpriteFromCurrentState(time);
		int playerOffset = 0;
		if (!player.playerRight && (currentPlayerFrame.getRegionWidth() != currentPlayerFrame.getRegionHeight())) {
			playerOffset = 80;
		}
		batch.draw(currentPlayerFrame,
				player.hitbox.x-90-playerOffset,
				player.hitbox.y-25,
				player.spriteHeight*((float)currentPlayerFrame.getRegionWidth()/currentPlayerFrame.getRegionHeight()),
				player.spriteHeight);

		renderEnemies();

		// render level foreground
		batch.draw(level.sprite,0,0,level.sprite.getWidth(), level.sprite.getHeight());
		if (player.health == 0) {
			player.animateDeath();
			this.renderDeathScreen();
		}
		batch.end();

		// render gui

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(Interface.hpBorder.x, Interface.hpBorder.y, Interface.hpBorder.width, Interface.hpBorder.height);
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(Interface.hpBackground.x, Interface.hpBackground.y, Interface.hpBackground.width, Interface.hpBackground.height);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(Interface.hpBar.x, Interface.hpBar.y, Interface.hpBar.width, Interface.hpBar.height);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(player.hitbox.x, player.hitbox.y, player.hitbox.width, player.hitbox.height);
		shapeRenderer.rect(player.attackHitbox.x, player.attackHitbox.y, player.attackHitbox.width, player.attackHitbox.height);
		shapeRenderer.rect(manager.rect.x, manager.rect.y, manager.rect.width, manager.rect.height);

		for(LevelBox box : level.boxes)
			shapeRenderer.rect(box.hitbox.x, box.hitbox.y, box.hitbox.width, box.hitbox.height);
		shapeRenderer.end();
	}

	private void updateCamera() {
		int camLeftBorder = (int) (level.sprite.getX() - player.hitbox.x);
		int camRightBorder = (int) (level.sprite.getX()+level.sprite.getWidth() - player.hitbox.x);

		this.cameraDeltaX = 0;
		this.cameraDeltaY = 0;

		if(camLeftBorder < -1534)
			if(camRightBorder > 1537) {
				this.cameraDeltaX = player.hitbox.x - camera.position.x;
				camera.position.x = player.hitbox.x;
			}


		if (sceneTransition > 0) {
			if (camera.position.y < CAMERA_TOP_BOUND) {
				this.cameraDeltaY = CAMERA_SLIDE_SPEED;

				camera.position.y = (camera.position.y + CAMERA_SLIDE_SPEED < CAMERA_TOP_BOUND)?
						camera.position.y + CAMERA_SLIDE_SPEED : CAMERA_TOP_BOUND;
			} else {
				sceneTransition = 0;
			}
		} else if (sceneTransition < 0) {
			if (camera.position.y > CAMERA_BOTTOM_BOUND) {
				this.cameraDeltaY = -CAMERA_SLIDE_SPEED;

				camera.position.y = (camera.position.y - CAMERA_SLIDE_SPEED > CAMERA_BOTTOM_BOUND)?
						camera.position.y - CAMERA_SLIDE_SPEED : CAMERA_BOTTOM_BOUND;
			} else {
				sceneTransition = 0;
			}
		}
		else if (player.hitbox.y > 1080 && camera.position.y < CAMERA_TOP_BOUND) {
			sceneTransition = 1;
		} else if (player.hitbox.y < 1080 && camera.position.y > CAMERA_BOTTOM_BOUND) {
			sceneTransition = -1;
		}

		camera.update();
	}

	private void renderDeathScreen() {
		Sprite s = new Sprite(new Texture(Gdx.files.internal("death_message.png")));

		batch.draw(s,Interface.hpBackground.x - 30 + 1200,Interface.hpBackground.x - 30 + 350, 1000,500);

		if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			flag = true;
			cameraDeltaX = 0;
			cameraDeltaY = 0;

			sceneTransition = 0;


			time = 0;


			player = new Player();
			manager = new EnemyManager(player);
			this.level = new Level("level/foreground_front.png", 0);
			level.addLevelboxes(new LevelBox[]{new LevelBox(1300, 180, 0, 0),
					new LevelBox(6000, 84, -1000, 0),
					new LevelBox(300, 3000, -300, 0), // right border
					new LevelBox(300, 1370, 3745, 0), // right wall
					new LevelBox(40, 2000, 45, 792-2000), // lower tree trunk
					new LevelBox(37, 690, 27, 2160-690), // upper tree trunk
					new LevelBox(175, 34, 1557, 384), // wood step
					new LevelBox(835, 64, 1720, 574-64), // mud white arc
					new LevelBox(750, 73, 2768, 853-73), // mud red arc
					new LevelBox(470, 60, 25, 2076-60), //Decke
					new LevelBox(1060, 1670, 25, 2179), //Decke
					new LevelBox(446, 85, 0, 870-85), // leafs
					new LevelBox(940, 240, 0, 1110-240), // leafs
					new LevelBox(1160, 140, 0, 1250-140), // leafs
					new LevelBox(675, 230, 0, 1480-230), // leafs
					new LevelBox(134, 70, 1528, 1200-70), // left cloud
					new LevelBox(796, 85, 1902, 1460-85), // middle cloud
					new LevelBox(240, 73, 3128, 1488-85), // right cloud
					new LevelBox(57, 90, 3690, 1100-90), // right wall stone
					new LevelBox(300, 3000, 3840, 0), // right wall
			});
			Level levelSky = new Level("level/sky.png", 0.8f);
			Level levelHorizon = new Level("level/horizon.png", 0.25f);
			Level levelBackForegroundLayer = new Level("level/foreground_back.png", 0);
			this.backgroundLevel = new ArrayList<>();
			this.backgroundLevel.addAll(Arrays.asList(levelSky, levelHorizon, levelBackForegroundLayer));


			camera = new OrthographicCamera();
			camera.zoom = 1.6f;
			camera.setToOrtho(false, 1920, 1080);
			flag = false;
		}
	}

	private void updateEnvironmentAssets() {

		for (Level bg : this.backgroundLevel) {
			bg.sprite.translate(
					(float) (cameraDeltaX * bg.getParallaxFactor()),
					(float) (cameraDeltaY * bg.getParallaxFactor()));
		}
	}

	private void updateEnemies() {

		for (Eye eye : manager.getEyes()) {
			if (eye.disabled) continue;
			eye.update(level);
		}
	}

	private void renderEnemies() {

		for (Eye eye : manager.getEyes()) {

			if (eye.disabled) continue;
			batch.draw(eye.deriveSpriteFromCurrentState(time),
					eye.hitbox.x, eye.hitbox.y,
					eye.spriteHeight/2, eye.spriteHeight/2,
					eye.spriteHeight,
					eye.spriteHeight,
					1,
					1,
					eye.lookDirection.angleDeg() + 135);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();
	}
}
