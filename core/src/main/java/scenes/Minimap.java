package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import entity.Enemy;
import entity.Player;
import map.BSPNode;
import screens.GameScreen;

import java.util.List;

public class Minimap implements Disposable {
    
    private ShapeRenderer shapeRenderer;
    private GameScreen screen;
    
    // Minimap settings
    private float minimapWidth = 150f;
    private float minimapHeight = 150f;
    private float minimapX; // Position from left
    private float minimapY; // Position from bottom
    private float mapScale = 0.05f; // Scale factor for map to minimap
    
    // Colors
    private Color wallColor = new Color(0.3f, 0.3f, 0.3f, 0.9f); // Dark gray
    private Color floorColor = new Color(0.6f, 0.6f, 0.6f, 0.5f); // Light gray
    private Color playerColor = new Color(0.2f, 0.8f, 0.2f, 1.0f); // Green
    private Color enemyColor = new Color(0.9f, 0.2f, 0.2f, 1.0f); // Red
    private Color borderColor = new Color(1f, 1f, 1f, 0.8f); // White
    private Color bgColor = new Color(0.1f, 0.1f, 0.1f, 0.7f); // Dark background
    
    public Minimap(GameScreen screen, float screenHeight) {
        this.screen = screen;
        this.minimapX = 10f; // Left margin
        this.minimapY = 10f; // Bottom margin - we'll update this in resize
        
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }
    
    public void render(Player player, Array<Enemy> enemies) {
        // Enable blending for transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        // Use screen coordinates
        shapeRenderer.setProjectionMatrix(shapeRenderer.getProjectionMatrix().setToOrtho2D(
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Draw background
        shapeRenderer.setColor(bgColor);
        shapeRenderer.rect(minimapX, minimapY, minimapWidth, minimapHeight);
        
        // Draw rooms
        List<BSPNode> rooms = screen.getMapGen().getRooms();
        if (rooms != null) {
            for (BSPNode room : rooms) {
                float roomX = minimapX + (room.getRoomX() * 16 * mapScale);
                float roomY = minimapY + (room.getRoomY() * 16 * mapScale);
                float roomWidth = room.getRoomWidth() * 16 * mapScale;
                float roomHeight = room.getRoomHeight() * 16 * mapScale;
                
                // Draw room floor
                shapeRenderer.setColor(floorColor);
                shapeRenderer.rect(roomX, roomY, roomWidth, roomHeight);
            }
        }
        
        // Draw enemies
        if (enemies != null) {
            shapeRenderer.setColor(enemyColor);
            for (Enemy enemy : enemies) {
                if (enemy.getState() != 2) { // Don't show dead enemies
                    float enemyX = minimapX + (enemy.getX() * mapScale);
                    float enemyY = minimapY + (enemy.getY() * mapScale);
                    shapeRenderer.circle(enemyX, enemyY, 1.5f);
                }
            }
        }
        
        // Draw player
        if (player != null) {
            shapeRenderer.setColor(playerColor);
            float playerX = minimapX + (player.getX() * mapScale);
            float playerY = minimapY + (player.getY() * mapScale);
            shapeRenderer.circle(playerX, playerY, 2.5f);
        }
        
        shapeRenderer.end();
        
        // Draw border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(borderColor);
        Gdx.gl.glLineWidth(2);
        shapeRenderer.rect(minimapX, minimapY, minimapWidth, minimapHeight);
        shapeRenderer.end();
        
        // Disable blending
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    
    public void resize(int width, int height) {
        // Keep minimap in top-left, so calculate Y position from bottom
        minimapY = height - minimapHeight - 10f;
    }
    
    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
    
    // Getters for customization
    public void setMinimapSize(float width, float height) {
        this.minimapWidth = width;
        this.minimapHeight = height;
    }
    
    public void setMinimapPosition(float x, float y) {
        this.minimapX = x;
        this.minimapY = y;
    }
    
    public void setMapScale(float scale) {
        this.mapScale = scale;
    }
}
