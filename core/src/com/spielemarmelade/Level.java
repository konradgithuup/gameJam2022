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
    final float parallaxFactor;

    public Level(String relativeAssetPath, float parallaxFactor){

        this.texture = new Texture(Gdx.files.internal(relativeAssetPath));
        this.sprite = new Sprite(texture);
        this.parallaxFactor = parallaxFactor;
    }

    void addLevelboxes(LevelBox[] boxes) {

        this.boxes.addAll(Arrays.asList(boxes));
    }


    double getParallaxFactor() {

        return this.parallaxFactor;
    }
}
