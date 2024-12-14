package entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HealthBar {
    private float x, y, width, height;
    private int currentHealth, maxHealth;
    private ShapeRenderer shapeRenderer;


    public HealthBar(float x, float y, float width, float height, int currentHealth, int maxHealth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        shapeRenderer = new ShapeRenderer();
    }

    
 
    // Render the health bar
 // Render the health bar at a given position
    public void render(float playerX, float playerY) {
    	
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Border
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(playerX - 2, playerY - 2, width + 4, height + 4);

        // Background
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(playerX, playerY, width, height);

        // Foreground with gradient
        float healthPercentage = (float) currentHealth / maxHealth;
        shapeRenderer.setColor(new Color(1 - healthPercentage, healthPercentage, 0, 1)); // Gradient from red to green
        shapeRenderer.rect(playerX, playerY, width * healthPercentage, height);

        shapeRenderer.end();
      
    }


    // Update health
    public void updateHealth(int healthChange) {
        this.currentHealth = Math.max(0, Math.min(this.currentHealth + healthChange, this.maxHealth));
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;  // Set it to null after disposal to avoid multiple dispose calls
        }
    }
}
