package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
    protected float x, y;
    protected float speed;

    public Entity(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public abstract void render(SpriteBatch batch);

    public abstract void handleInput();

    public abstract void dispose();
}