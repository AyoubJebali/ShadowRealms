package entity;

import com.badlogic.gdx.graphics.Color;
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

    // Update the position of the health bar
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Render the health bar
    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Background (empty health bar)
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, width, height);

        // Foreground (filled health bar)
        float healthPercentage = (float) currentHealth / maxHealth;
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width * healthPercentage, height);

        shapeRenderer.end();
    }

    // Update health
    public void updateHealth(int healthChange) {
        this.currentHealth = Math.max(0, Math.min(this.currentHealth + healthChange, this.maxHealth));
    }

    // Dispose the shape renderer
    public void dispose() {
        shapeRenderer.dispose();
    }
}
