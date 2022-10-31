package com.spielemarmelade;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Enemy {

    void update(Level level);

    TextureRegion deriveSpriteFromCurrentState(float time);
}
