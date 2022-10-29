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

	Texture playerTexture;
	Sprite playerSprite;

	Boolean playerRight = true;
	int movespeed = 700;
	OrthographicCamera camera;
	Rectangle player;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		playerTexture = new Texture(Gdx.files.internal("player.png"));
		playerSprite = new Sprite(playerTexture);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		batch = new SpriteBatch();

		player = new Rectangle();
		player.x = 500;
		player.y = 500;
		player.width = 200;
		player.height = 200;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0,0.02f,0.07f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(playerSprite, player.x, player.y, player.width, player.height);
		batch.end();

		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			player.x += movespeed * Gdx.graphics.getDeltaTime();
			if(!playerRight){
				playerSprite.flip(true, false);
				playerRight = true;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			player.x -= movespeed * Gdx.graphics.getDeltaTime();
			if(playerRight) {
				playerSprite.flip(true, false);
				playerRight = false;
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerTexture.dispose();
	}
}
