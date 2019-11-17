package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private Texture texture;
    private Vector2 position;
    private float angle;

    public Asteroid(){
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(MathUtils.random(-150 , ScreenManager.SCREEN_WIDTH + 150), MathUtils.random(-150 , ScreenManager.SCREEN_HEIGHT + 150));
        this.angle = MathUtils.random(0.0f, 360.0f);
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128, 256, 256, 1, 1 , angle, 0, 0, 256, 256, false, false);
    }

    public void update(float dt){
        position.x += (float)Math.cos(Math.toRadians(angle)) * 50.0f * dt;
        position.y += (float)Math.sin(Math.toRadians(angle)) * 50.0f * dt;
        if (position.x < -120.0f){
            position.x = ScreenManager.SCREEN_WIDTH + 120.0f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + 120.0f) {
            position.x = -120.0f;
        }
        if (position.y < -120.0f){
            position.y = ScreenManager.SCREEN_HEIGHT + 120.0f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + 120.0f) {
            position.y = -120.0f;
        }
    }
}
