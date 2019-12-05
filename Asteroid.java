package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Asteroid implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private int hpMax;
    private int hp;
    private float scale;
    private float angle;
    private float rotationSpeed;
    private boolean active;
    private Circle hitArea;
    private int force;

    private final float BASE_SIZE = 256.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2.0f;

    public int getHpMax() {
        return hpMax;
    }

    public int getForce() {
        return force;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public Asteroid(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
        this.hpMax = (int) (10 * scale);
        this.force = (int) (10 * scale);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            gc.getHero().addAsteroidsDestroyed(1);
            if (scale > 0.9f) {
                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, (int) (hpMax / scale), (int) (force / scale));
                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, (int) (hpMax / scale) , (int) (force / scale));
            } else if (scale > 0.25f) {
                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, (int) (hpMax / scale) , (int) (force / scale));
                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, (int) (hpMax / scale) , (int) (force / scale));
                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, (int) (hpMax / scale) , (int) (force / scale));
            }
            return true;
        }
        return false;
    }

    public void activate(float x, float y, float vx, float vy, float scale, int upgradeHp, int upgradeForce) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.hpMax *= upgradeHp;
        this.hp = this.hpMax;
        this.force *= upgradeForce;
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.hitArea.setPosition(position);
        this.rotationSpeed = MathUtils.random(-60.0f, 60.0f);
        this.active = true;
        this.scale = scale;
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128, 256, 256, scale, scale, angle);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;
        if (position.x < -BASE_RADIUS * scale) {
            position.x = ScreenManager.SCREEN_WIDTH + BASE_RADIUS * scale;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + BASE_RADIUS * scale) {
            position.x = -BASE_RADIUS * scale;
        }
        if (position.y < -BASE_RADIUS * scale) {
            position.y = ScreenManager.SCREEN_HEIGHT + BASE_RADIUS * scale;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + BASE_RADIUS * scale) {
            position.y = -BASE_RADIUS * scale;
        }
        hitArea.setPosition(position);
    }
}