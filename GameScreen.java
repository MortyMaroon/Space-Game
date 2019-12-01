package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.game.GameController;
import com.star.app.game.WorldRenderer;
import com.star.app.screen.utils.Assets;
import com.star.app.screen.utils.OptionsUtils;

public class GameScreen extends AbstractScreen {
    private GameController gameController;
    private WorldRenderer worldRenderer;
    private Stage stage;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.gameController = new GameController();
        this.worldRenderer = new WorldRenderer(gameController, batch);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        Button btnPause = new Button(skin.getDrawable("Pause"));
        Button btnExitMenu = new Button(skin.getDrawable( "Menu"));
        btnPause.setPosition(1152, 656);
        btnExitMenu.setPosition(1216, 656);

        btnPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Realization Pause
            }
        });

        btnExitMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        stage.addActor(btnPause);
        stage.addActor(btnExitMenu);
        skin.dispose();

        if (!OptionsUtils.isOptionsExists()) {
            OptionsUtils.createDefaultProperties();
        }
    }

    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        gameController.update(delta);
        worldRenderer.render();
        stage.draw();
    }

    @Override
    public void dispose() {
        gameController.dispose();
    }
}
