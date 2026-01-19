package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import screens.GameScreen;

public class Enemy extends Entity{
    private float x, y;
    private float baseSpeed;
    private float speed;
    private Player player;
    private float stateTime;
    private float deathTime;
    private Animation<TextureRegion> idle, walk, attack, currentAnimation, Dead;
    
    private HealthBar MonsterHealth;
    private int state = 0; // 0 = alive, 1 = dying, 2 = dead
    private GameScreen screen;
    private float detectionRadius;
    private float attackRadius;
    private float playerAttackRadius = 50f;
    private float attackCooldown;
    private float lastAttackTime = 0;
    private int attackDamage;
    private int maxHealth;
    public int Blood;
    
    // AI improvements
    private float stuckTimer = 0f;
    private float lastX, lastY;
    private static final float STUCK_THRESHOLD = 0.5f;
    private float circleTimer = 0f;
    private boolean isStuck = false;
    
    // Enemy type
    private EnemyType type;
    
    public enum EnemyType {
        NORMAL,   // Standard balanced enemy
        FAST,     // Quick but weak
        TANK,     // Slow but strong
        AGGRESSIVE // Medium speed, high damage
    }
    
    // Constructor with enemy type
    public Enemy(GameScreen screen, float x, float y, String MonsterName, HealthBar MonsterHealth, EnemyType type) {
        super();
        this.x = x;
        this.y = y;
        this.lastX = x;
        this.lastY = y;
        this.stateTime = 0f;
        this.deathTime = 0f;
        this.MonsterHealth = MonsterHealth;
        this.screen = screen;
        this.player = screen.getPlayer();
        this.type = type;
        
        // Set stats based on enemy type
        initializeStats();
        
        // Load the animations with proper frame splitting and timing adjustments
        idle = createAnimation(MonsterName + "-Idle.png", 6, 0.25f);
        walk = createAnimation(MonsterName + "-Walk.png", 8, 0.30f); 
        attack = createAnimation(MonsterName + "-Attack02.png", 6, 0.15f);
        Dead = createAnimation(MonsterName + "-Death.png", 4, 0.3f);
        currentAnimation = idle;
    }
    
    // Original constructor defaults to NORMAL type
    public Enemy(GameScreen screen, float x, float y, String MonsterName, HealthBar MonsterHealth) {
        this(screen, x, y, MonsterName, MonsterHealth, EnemyType.NORMAL);
    }
    
    public Enemy(Player player, String MonsterName, HealthBar MonsterHealth) {
    	super();
        this.player = player;
        this.x = 300f;
        this.y = 300f;
        this.lastX = x;
        this.lastY = y;
        this.stateTime = 0f;
        this.MonsterHealth = MonsterHealth;
        this.Blood = 1;
        this.type = EnemyType.NORMAL;
        initializeStats();

        idle = createAnimation(MonsterName + "-Idle.png", 6, 0.25f);
        walk = createAnimation(MonsterName + "-Walk.png", 8, 0.30f);
        attack = createAnimation(MonsterName + "-Attack02.png", 6, 0.15f);
        Dead = createAnimation(MonsterName + "-Death.png", 4, 0.5f);
        currentAnimation = idle;
    }
    
    private void initializeStats() {
        switch(type) {
            case FAST:
                baseSpeed = 110f;
                maxHealth = 60;
                attackDamage = 5;
                detectionRadius = 180f;
                attackRadius = 18f;
                attackCooldown = 0.8f;
                break;
            case TANK:
                baseSpeed = 50f;
                maxHealth = 150;
                attackDamage = 20;
                detectionRadius = 120f;
                attackRadius = 25f;
                attackCooldown = 1.5f;
                break;
            case AGGRESSIVE:
                baseSpeed = 85f;
                maxHealth = 80;
                attackDamage = 15;
                detectionRadius = 200f;
                attackRadius = 22f;
                attackCooldown = 0.7f;
                break;
            case NORMAL:
            default:
                baseSpeed = 75f;
                maxHealth = 100;
                attackDamage = 10;
                detectionRadius = 150f;
                attackRadius = 20f;
                attackCooldown = 1.0f;
                break;
        }
        speed = baseSpeed;
        Blood = maxHealth;
    }

    private Animation<TextureRegion> createAnimation(String texturePath, int frames, float frameDuration) {
        Texture texture = new Texture(Gdx.files.internal(texturePath), true);
        int frameWidth = texture.getWidth() / frames;
        int frameHeight = texture.getHeight();
        TextureRegion[][] tempFrames = TextureRegion.split(texture, frameWidth, frameHeight);
        TextureRegion[] framesArray = new TextureRegion[frames];

        for (int i = 0; i < frames; i++) {
            framesArray[i] = tempFrames[0][i];
        }

        return new Animation<>(frameDuration, framesArray);
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float dx = player.getX() - x - 12;
        float dy = player.getY() - y - 15;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        // Handle dying state
        if(state == 1) {
            deathTime += deltaTime;
            if(Dead.isAnimationFinished(deathTime)) {
                state = 2;
            }
            return;
        }
        
        // Don't update if dead
        if(state == 2) {
            return;
        }
        
        // Check if stuck (hasn't moved much)
        float distanceMoved = (float) Math.sqrt(Math.pow(x - lastX, 2) + Math.pow(y - lastY, 2));
        if (distanceMoved < 0.5f && (currentAnimation == walk)) {
            stuckTimer += deltaTime;
            if (stuckTimer > STUCK_THRESHOLD) {
                isStuck = true;
            }
        } else {
            stuckTimer = 0f;
            isStuck = false;
        }
        
        lastX = x;
        lastY = y;
        
        // Check if player can attack enemy
        if (distance <= playerAttackRadius) {
            if (player.AttackEnemy()) {
                this.takeDamage(100);
            }
            
            // Enemy attacks if in range
            if (distance <= attackRadius) {
                currentAnimation = attack;
                if (stateTime - lastAttackTime >= attackCooldown) {
                    player.takeDamage(attackDamage);
                    lastAttackTime = stateTime;
                }
            } else {
                // Chase player with improved pathfinding
                moveTowardsPlayer(dx, dy, distance, deltaTime);
            }
        }
        // Chase if in detection range
        else if (distance <= detectionRadius) {
            moveTowardsPlayer(dx, dy, distance, deltaTime);
        } 
        else {
            currentAnimation = idle;
            isStuck = false;
            stuckTimer = 0f;
        }

        stateTime += deltaTime;
    }
    
    private void moveTowardsPlayer(float dx, float dy, float distance, float deltaTime) {
        currentAnimation = walk;
        float normalizedDx = dx / distance;
        float normalizedDy = dy / distance;
        
        float newX = x;
        float newY = y;
        
        // If stuck, try circling around obstacle
        if (isStuck) {
            circleTimer += deltaTime;
            // Add perpendicular movement to try to get unstuck
            float perpX = -normalizedDy;
            float perpY = normalizedDx;
            
            // Alternate circling direction
            if ((int)(circleTimer * 2) % 2 == 0) {
                perpX = -perpX;
                perpY = -perpY;
            }
            
            newX = x + (normalizedDx * 0.3f + perpX * 0.7f) * speed * deltaTime;
            newY = y + (normalizedDy * 0.3f + perpY * 0.7f) * speed * deltaTime;
        } else {
            circleTimer = 0f;
            newX = x + normalizedDx * speed * deltaTime;
            newY = y + normalizedDy * speed * deltaTime;
        }
        
        // Check collision with walls before moving
        if (screen != null && !checkCollision(newX, newY)) {
            x = newX;
            y = newY;
        } else if (screen != null) {
            // Try moving only in X direction
            if (!checkCollision(newX, y)) {
                x = newX;
            }
            // Try moving only in Y direction
            else if (!checkCollision(x, newY)) {
                y = newY;
            } else {
                // Completely blocked, mark as stuck
                isStuck = true;
            }
        } else {
            // No collision detection available
            x = newX;
            y = newY;
        }
    }
    
    private boolean checkCollision(float checkX, float checkY) {
        if (screen == null || screen.getMapGen() == null) {
            return false;
        }
        
        // Check multiple points around the enemy's bounding box
        // Enemy sprite is roughly 24x24 pixels (40 * 3/5)
        float hitboxSize = 12f; // Half the sprite size for safety margin
        
        // Check center
        if (screen.getMapGen().checkBlockedTile(checkX, checkY)) {
            return true;
        }
        
        // Check corners of the hitbox
        if (screen.getMapGen().checkBlockedTile(checkX - hitboxSize, checkY - hitboxSize)) {
            return true; // Bottom-left
        }
        if (screen.getMapGen().checkBlockedTile(checkX + hitboxSize, checkY - hitboxSize)) {
            return true; // Bottom-right
        }
        if (screen.getMapGen().checkBlockedTile(checkX - hitboxSize, checkY + hitboxSize)) {
            return true; // Top-left
        }
        if (screen.getMapGen().checkBlockedTile(checkX + hitboxSize, checkY + hitboxSize)) {
            return true; // Top-right
        }
        
        // Check mid-points of edges for better accuracy
        if (screen.getMapGen().checkBlockedTile(checkX, checkY - hitboxSize)) {
            return true; // Bottom
        }
        if (screen.getMapGen().checkBlockedTile(checkX, checkY + hitboxSize)) {
            return true; // Top
        }
        if (screen.getMapGen().checkBlockedTile(checkX - hitboxSize, checkY)) {
            return true; // Left
        }
        if (screen.getMapGen().checkBlockedTile(checkX + hitboxSize, checkY)) {
            return true; // Right
        }
        
        return false;
    }
    
    public void takeDamage(int damage) {
        Blood -= damage;
        MonsterHealth.updateHealth(-damage);
        
        // Speed boost when low health (enrage)
        if (Blood < maxHealth * 0.3f && state == 0) {
            speed = baseSpeed * 1.3f; // 30% speed boost when low health
        }

        if (Blood <= 0 && state == 0) {
            Blood = 0;
            deathTime = 0f;
            currentAnimation = Dead;
            state = 1;
            System.out.println("Monster is dead!");
        }
    }

    public void render(SpriteBatch batch) {
        if(state == 2) {
            return;
        }
        
        TextureRegion frame;
        if(state == 1) {
            frame = Dead.getKeyFrame(deathTime, false);
        } else {
            frame = currentAnimation.getKeyFrame(stateTime, true);
        }
        
        // Draw enemy sprite
        batch.draw(frame, x, y, frame.getRegionWidth() * 3/5f, frame.getRegionHeight() * 3/5f);
        
        // Render health bar above enemy (only when alive)
        if (state == 0 && MonsterHealth != null) {
            MonsterHealth.render(batch, x, y + 25);
        }
    }

    public void dispose() {
        if (idle != null) {
            idle.getKeyFrames()[0].getTexture().dispose();
        }
        if (walk != null) {
            walk.getKeyFrames()[0].getTexture().dispose();
        }
        if (attack != null) {
            attack.getKeyFrames()[0].getTexture().dispose();
        }
        if (Dead != null) {
            Dead.getKeyFrames()[0].getTexture().dispose();
        }
    }

    public int getState() {
    	return this.state;
    }
    
    public EnemyType getType() {
        return this.type;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
}

