package com.star.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StarGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Background background;
	private Hero hero;
	private Asteroid asteroid;

	public Hero getHero() {
		return hero;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Background(this);
		hero = new Hero();
		asteroid = new Asteroid();
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.render(batch);
		asteroid.render(batch);
		hero.render(batch);
		batch.end();
	}

	public void update(float dt){
		background.update(dt);
		hero.update(dt);
		asteroid.update(dt);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
