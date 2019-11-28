package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private GameController gc;
    private StringBuilder stringBuilder;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private float fireTimer;
    private int bullet;
    private int money;
    private int score;
    private int scoreView;
    private int health;
    private Circle hitArea;
    private boolean rightOrLeftSocket;

    public float getAngle() { return angle; }

    public void takeBullet(int bullet) { this.bullet += bullet; }

    public void takeDamage(int damage) { health -= damage; }

    public void takeHealth (int health) { this.health += health;}

    public void addScore(int amount) {
        score += amount;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Circle getHitArea() { return hitArea; }

    public void takeMoney(int money) { this.money += money; }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 750.0f;
        this.health = 100;
        this.hitArea = new Circle(position, 28.0f);
        this.stringBuilder = new StringBuilder();
        this.money = 0;
        this.bullet = 100;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font){
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(scoreView).append("\n");
        stringBuilder.append("MONEY: ").append(money).append("\n");
        stringBuilder.append("BULLET: ").append(money);
        font.draw(batch, stringBuilder, 20, 700);
        stringBuilder.clear();
        stringBuilder.append("HEALTH: ").append(health);
        font.draw(batch, stringBuilder, 550, 700);
    }

    public void update(float delta) {
        fireTimer += delta;
        updateScore(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tryToFair();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += (float) Math.cos(Math.toRadians(angle)) * enginePower * delta;
            velocity.y += (float) Math.sin(Math.toRadians(angle)) * enginePower * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= (float) Math.cos(Math.toRadians(angle)) * enginePower * delta / 2.0f;
            velocity.y -= (float) Math.sin(Math.toRadians(angle)) * enginePower * delta / 2.0f;
        }
        position.mulAdd(velocity, delta);
        hitArea.setPosition(position);
        float stopKoef = 1.0f - 2.0f * delta;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);
        if (velocity.len() > 50.0f) {
            float bx, by;
            bx = position.x - 28.0f * (float) Math.cos(Math.toRadians(angle));
            by = position.y - 28.0f * (float) Math.sin(Math.toRadians(angle));
            for (int i = 0; i < 5; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f
                );
            }
        }
        checkSpaceBorders();

    }

    public void tryToFair(){
        if (fireTimer > 0.04f) {
            fireTimer = 0.0f;
            float wx, wy ;
            rightOrLeftSocket = !rightOrLeftSocket;
            if (rightOrLeftSocket) {
                wx = position.x + (float) Math.cos(Math.toRadians(angle + 90)) * 25;
                wy = position.y + (float) Math.sin(Math.toRadians(angle + 90)) * 25;
                gc.getBulletController().setup(wx, wy, (float) Math.cos(Math.toRadians(angle)) * 600 + velocity.x, (float) Math.sin(Math.toRadians(angle)) * 600 + velocity.y, angle);
            } else {
                wx = position.x + (float) Math.cos(Math.toRadians(angle - 90)) * 25;
                wy = position.y + (float) Math.sin(Math.toRadians(angle - 90)) * 25;
                gc.getBulletController().setup(wx, wy, (float) Math.cos(Math.toRadians(angle)) * 600 + velocity.x, (float) Math.sin(Math.toRadians(angle)) * 600 + velocity.y, angle);
            }
        }
    }

    public void checkSpaceBorders(){
        if (position.x < hitArea.radius) {
            position.x = hitArea.radius;
            velocity.x *= -1;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - hitArea.radius) {
            position.x = ScreenManager.SCREEN_WIDTH - hitArea.radius;
            velocity.x *= -1;
        }
        if (position.y < hitArea.radius) {
            position.y = hitArea.radius;
            velocity.y *= -1;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - hitArea.radius) {
            position.y = ScreenManager.SCREEN_HEIGHT - hitArea.radius;
            velocity.y *= -1;
        }
    }

    public void updateScore(float delta){
        if (scoreView < score) {
            float scoreSpeed = (score - scoreView) / 2.0f;
            if (scoreSpeed < 2000.0f) {
                scoreSpeed = 2000.0f;
            }
            scoreView += scoreSpeed * delta;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }
}
