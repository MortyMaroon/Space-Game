package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.game.Background;
import com.star.app.game.GameController;
import com.star.app.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font24;
    private Stage stage;
    private int score;

    public GameOverScreen(SpriteBatch batch, int score) {
        super(batch);
        this.score = score;
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
    }

    public void touched() {
        if (Gdx.input.justTouched()){
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
        touched();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "GAME OVER", 0, 600, 1280, 1, false);
        font24.draw(batch, "Statistics:", 0, 400, 1280, 1, false);
        font24.draw(batch, "Your Score: " + score, 0, 370, 1280, 1, false);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
