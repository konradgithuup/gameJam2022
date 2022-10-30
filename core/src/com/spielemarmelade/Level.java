package com.spielemarmelade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class Level {
    ArrayList<LevelBox> boxes = new ArrayList<>();
    Texture texture = new Texture(Gdx.files.internal("level.png"));
    Sprite sprite = new Sprite(texture);

    public Level(LevelBox[] boxes){
        this.boxes.addAll(Arrays.asList(boxes));
    }
}
