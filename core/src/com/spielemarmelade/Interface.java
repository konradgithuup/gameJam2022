package com.spielemarmelade;

import com.badlogic.gdx.math.Rectangle;

public class Interface {
     static Rectangle hpBar = new Rectangle(30,30, 500, 60);

    static Rectangle hpBackground = new Rectangle(30,30, 500, 60);
    static Rectangle hpBorder = new Rectangle(25,25,510,70);

    private Interface(){
    }

    public static void updateHp(int health){
        hpBorder.x += BaseGame.cameraDeltaX;
        hpBorder.y += BaseGame.cameraDeltaY;

        hpBar.x += BaseGame.cameraDeltaX;
        hpBar.y += BaseGame.cameraDeltaY;

        hpBackground.x += BaseGame.cameraDeltaX;
        hpBackground.y += BaseGame.cameraDeltaY;

        hpBar.width = health*5;
    }
}
