package com.spielemarmelade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class Level {
    ArrayList<LevelBox> boxes = new ArrayList<>();
    final Texture texture;
    final Sprite sprite;

    public Level(String relativeAssetPath){

        this.texture = new Texture(Gdx.files.internal(relativeAssetPath));
        this.sprite = new Sprite(texture);
    }

    void addLevelboxes(LevelBox[] boxes) {

        this.boxes.addAll(Arrays.asList(boxes));
    }
}
