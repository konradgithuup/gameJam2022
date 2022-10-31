package com.spielemarmelade;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DeathHud {

    Stage stage;
    Viewport viewport;
    Label deathMessageLabel;

    public DeathHud(SpriteBatch sb) {

        viewport = new FitViewport(1920, 1080, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        deathMessageLabel = new Label("You Died!", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        deathMessageLabel.setFontScale(1000);
        table.add(deathMessageLabel).expandX().padTop(10);

        stage.addActor(table);
    }
}
