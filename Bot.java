package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Bot implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private int hpMax;
    private int hp;
    private float scale;
    private boolean active;
    private Circle hitArea;
    private Circle viewArea;
    private int radiusView;
    private Weapon weapon;
    private float fireTimer;

    private final float BASE_SIZE = 128.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2.0f;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getHp() {
        return hp;
    }

    public float getScale() {
        return scale;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public void deactivate() {
        active = false;
    }

    public Circle getViewArea() {
        return viewArea;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            return true;
        }
        return false;
    }

    public Bot(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("bot128");
        this.radiusView = 500;
        this.viewArea = new Circle(position, radiusView);
        this.weapon = new Weapon(
                gc, gc.getHero(), "Laser", 0.2f, 1, 500.0f, 320,
                new Vector3[]{
                        new Vector3(24, 90, 0),
                        new Vector3(24, -90, 0)
                }
        );
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        if (this.velocity.len() < 50.0f) {
            this.velocity.nor().scl(50.0f);
        }
        this.hpMax = (int) ((10 + gc.getLevel() * 4) * scale);
        this.hp = this.hpMax;
        this.hitArea.setPosition(position);
        this.active = true;
        this.scale = scale;
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.7f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - BASE_RADIUS, position.y - BASE_RADIUS, BASE_RADIUS, BASE_RADIUS, BASE_SIZE, BASE_SIZE, scale, scale, 0);
        if(position.x > GameController.SPACE_WIDTH - ScreenManager.HALF_SCREEN_WIDTH) {
            batch.draw(texture, position.x - BASE_RADIUS, position.y - BASE_RADIUS, BASE_RADIUS, BASE_RADIUS, BASE_SIZE, BASE_SIZE, scale, scale, 0);
        }
        if(position.x < ScreenManager.HALF_SCREEN_WIDTH) {
            batch.draw(texture, position.x - BASE_RADIUS, position.y - BASE_RADIUS, BASE_RADIUS, BASE_RADIUS, BASE_SIZE, BASE_SIZE, scale, scale, 0);
        }
    }

    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        viewArea.setPosition(position);
        if (position.x < -BASE_RADIUS * scale) {
            position.x = GameController.SPACE_WIDTH + BASE_RADIUS * scale;
        }
        if (position.x > GameController.SPACE_WIDTH + BASE_RADIUS * scale) {
            position.x = -BASE_RADIUS * scale;
        }
        if (position.y < -BASE_RADIUS * scale) {
            position.y = GameController.SPACE_HEIGHT + BASE_RADIUS * scale;
        }
        if (position.y > GameController.SPACE_HEIGHT + BASE_RADIUS * scale) {
            position.y = -BASE_RADIUS * scale;
        }
        hitArea.setPosition(position);
    }

    public void tryToFire() {
        if (fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            weapon.fire();
        }
    }

}
