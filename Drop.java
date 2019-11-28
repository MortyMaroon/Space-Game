package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Drop implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float scale;
    private boolean active;
    private Circle hitArea;
    private int money;
    private int health;
    private int bullet;

    private final float BASE_SIZE = 62.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2.0f;

    public int getBullet() { return bullet; }

    public float getScale() { return scale; }

    public int getMoney() { return money; }

    public int getHealth() { return health; }

    public Circle getHitArea() { return hitArea; }

    @Override
    public boolean isActive() { return active; }

    public void deactivate() {
        active = false;
    }

    public Drop(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("drop");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.scale = 1.0f;
        this.health = 10;
        this.money = 10;
        this.bullet = 10;
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.hitArea.setPosition(position);
        this.active = true;
        this.scale = scale;
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, scale, scale, 0);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < -BASE_RADIUS) {
            position.x = ScreenManager.SCREEN_WIDTH + BASE_RADIUS;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + BASE_RADIUS) {
            position.x = -BASE_RADIUS;
        }
        if (position.y < -BASE_RADIUS) {
            position.y = ScreenManager.SCREEN_HEIGHT + BASE_RADIUS;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + BASE_RADIUS) {
            position.y = -BASE_RADIUS;
        }
        hitArea.setPosition(position);
    }
}
