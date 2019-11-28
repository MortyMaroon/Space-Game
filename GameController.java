package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private Hero hero;
    private DropController dropController;
    private ParticleController particleController;

    public ParticleController getParticleController() { return particleController; }

    public DropController getDropController() { return dropController; }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public GameController() {
        this.particleController = new ParticleController();
        this.dropController = new DropController(this);
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController();
        for (int i = 0; i < 3; i++) {
            this.asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH), MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), 1.0f);
        }
    }

    public void update(float dt) {
        dropController.update(dt);
        particleController.update(dt);
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid asteroid = asteroidController.getActiveList().get(i);
            if (asteroid.getHitArea().contains(hero.getPosition())) {
                asteroid.takeDamage(1);
                hero.takeDamage(10);
            }
        }

        for (int i = 0; i < dropController.getActiveList().size(); i++) {
            Drop drop = dropController.getActiveList().get(i);
            if (drop.getHitArea().contains(hero.getHitArea())){
                drop.deactivate();
                switch (MathUtils.random(0,3)){
                    case 1:
                        hero.takeMoney(drop.getMoney());
                        break;
                    case 2:
                        hero.takeBullet(drop.getBullet());
                        break;
                    case 3:
                        hero.takeHealth(drop.getHealth());
                        break;
                }
            }
        }
    }
}