package com.spielemarmelade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class BaseGame extends ApplicationAdapter {
	SpriteBatch batch;
	Player player;
	OrthographicCamera camera;
	ShapeRenderer shapeRenderer;

	Level level;

	float time = 0;


	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		player = new Player();
		level = new Level(new LevelBox[]{new LevelBox(1300, 180, 0, 0),
				new LevelBox(6000, 84, -1000, 0),
				new LevelBox(300, 3000, -300, 0),
				new LevelBox(300, 1370, 3737, 0),
				new LevelBox(40, 2000, 45, 792-2000),
				new LevelBox(37, 690, 27, 2160-690),
				new LevelBox(200, 34, 1557, 384),
				new LevelBox(835, 64, 1720, 574-64),
				new LevelBox(750, 73, 2768, 853-73),
				new LevelBox(470, 60, 25, 2076-60), //Decke
				new LevelBox(1060, 1670, 25, 2179), //Decke
				new LevelBox(446, 85, 0, 870-85),
				new LevelBox(940, 240, 0, 1110-240),
				new LevelBox(1160, 140, 0, 1250-140),
				new LevelBox(675, 230, 0, 1480-230),
				new LevelBox(134, 70, 1528, 1200-70),
				new LevelBox(796, 85, 1902, 1460-85),
				new LevelBox(180, 73, 3228, 1488-73),
				new LevelBox(57, 95, 3680, 1100-95),
		});
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.zoom = 1.6f;
		camera.setToOrtho(false, 1920, 1080);
		batch = new SpriteBatch();


	}

	@Override
	public void render () {
		ScreenUtils.clear(0,0.02f,0.07f, 1);
		time += Gdx.graphics.getDeltaTime();

		player.updateInput(level);
		updateCamera();

		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(level.sprite,0,0,level.sprite.getWidth(), level.sprite.getHeight());
		//batch.draw(player.runningSprite, player.hitbox.x + player.spriteOffsetX, player.hitbox.y + player.spriteOffsetY, player.spriteWidth, player.spriteHeight);
		System.out.println(time);
		batch.draw(player.runningAnimation.getKeyFrame(time, true), player.hitbox.x, player.hitbox.y, player.spriteWidth, player.spriteHeight);
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(player.hitbox.x, player.hitbox.y, player.hitbox.width, player.hitbox.height);
		for(LevelBox box : level.boxes)
			shapeRenderer.rect(box.hitbox.x, box.hitbox.y, box.hitbox.width, box.hitbox.height);
		shapeRenderer.end();
	}

	private void updateCamera() {
		int camLeftBorder = (int) (level.sprite.getX() - player.hitbox.x);
		int camRightBorder = (int) (level.sprite.getX()+level.sprite.getWidth() - player.hitbox.x);

		if(camLeftBorder < -1534)
			if(camRightBorder > 1537)
				camera.position.x = player.hitbox.x;

		camera.update();
	}

	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();
	}
}
