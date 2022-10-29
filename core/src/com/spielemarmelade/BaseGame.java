package com.spielemarmelade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class BaseGame extends ApplicationAdapter {
	SpriteBatch batch;
	Player player;
	OrthographicCamera camera;


	
	@Override
	public void create () {
		player = new Player();
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		batch = new SpriteBatch();


	}

	@Override
	public void render () {
		ScreenUtils.clear(0,0.02f,0.07f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(player.sprite, player.hitbox.x, player.hitbox.y, player.hitbox.width, player.hitbox.height);
		batch.end();

		player.updateInput();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();
	}
}
