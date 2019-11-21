package com.star.app.game;

public class GameController {
    private Background background;
    private Asteroid asteroid;
    private Hero hero;

    public Background getBackground() {
        return background;
    }

    public Asteroid getAsteroid() {
        return asteroid;
    }

    public Hero getHero(){
        return hero;
    }

    public GameController(){
        this.background = new Background(this);
        this.asteroid = new Asteroid();
        this.hero = new Hero();
    }

    public void update(float delta){
        background.update(delta);
        asteroid.update(delta);
        hero.update(delta);
    }

}
