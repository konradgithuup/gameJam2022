package com.spielemarmelade;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnemyManager {

    int ENTITY_CAP = 5;
    private List<Eye> eyes = new ArrayList<>();
    private float lastSpawn = -99;
    private Player target;
    Rectangle rect = new Rectangle();

    private float spawnY = 1800;
    private float spawnX = 500;

    EnemyManager(Player target) {

        this.target = target;
        rect.x = 500;
        rect.y = 2000;
        rect.width = 3000;
        rect.height = 100;
    }

    void update(float time, float cameraDeltaX, float cameraDeltaY) {

        spawnY += cameraDeltaY;
        spawnX += cameraDeltaX;

        this.eyes = this.eyes.stream().filter(e -> !e.disabled).collect(Collectors.toList());

        if (time - this.lastSpawn > 3 && eyes.size() < ENTITY_CAP) {
            this.lastSpawn = time;
            eyes.add(new Eye(target, rect.x + (float)Math.random()*rect.width, rect.y + (float)Math.random()*rect.height));
        }
    }

    List<Eye> getEyes() {

        return this.eyes;
    }
}
