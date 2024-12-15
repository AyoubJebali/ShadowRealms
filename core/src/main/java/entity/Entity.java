package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    protected float x, y;
    protected float speed;
    protected Vector2 mapCord,screenCord;
    public Entity() {
        this.x = 0;
        this.y = 0;
        this.speed = 0;
    }
    public abstract void render(SpriteBatch batch);

    public abstract void handleInput();

    public abstract void dispose();
    
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public Vector2 getMapCord() {
    	return mapCord;
    }
    public void setMapCord(Vector2 v) {
    	mapCord = v;
    }
    
    public void setMapCord(float x , float y) {
    	mapCord = new Vector2(x,y);
    }
    public Vector2 getScreenCord() {
    	return screenCord;
    }
    public void setScreenCord(Vector2 v) {
    	screenCord = v;
    }
    public void setScreenCord(float x , float y) {
    	screenCord = new Vector2(x,y);
    }
}