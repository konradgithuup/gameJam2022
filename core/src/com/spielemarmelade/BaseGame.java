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


	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		player = new Player();
		level = new Level(new LevelBox[]{new LevelBox(1300, 180, 0, 0),
				new LevelBox(6000, 84, -1000, 0),
				new LevelBox(300, 3000, -300, 0),
				new LevelBox(300, 3000, 3800, 0),

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

		player.updateInput(level);
		updateCamera();

		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(level.sprite,0,0,level.sprite.getWidth(), level.sprite.getHeight());
		batch.draw(player.sprite, player.hitbox.x + player.spriteOffsetX, player.hitbox.y + player.spriteOffsetY, player.spriteWidth, player.spriteHeight);
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
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
