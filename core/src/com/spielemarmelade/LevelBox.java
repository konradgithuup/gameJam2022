package com.spielemarmelade;

import com.badlogic.gdx.math.Rectangle;

public class LevelBox {

    Rectangle hitbox = new Rectangle();

    public LevelBox(int width, int height, int x, int y){
        hitbox.x = x;
        hitbox.y = y;
        hitbox.width = width;
        hitbox.height = height;
    }
}
