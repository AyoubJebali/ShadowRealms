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
    private float speed = 75f;
    private Player player;
    private float stateTime;
    private float deathTime;  // NEW: Separate timer for death animation
    private Animation<TextureRegion> idle, walk, attack, currentAnimation,Dead;
    
    private HealthBar MonsterHealth;
    private int state =0; // 0 = alive , 1 = dying , 2 = dead
    private GameScreen screen;
    private float detectionRadius = 150f;
    private float attackRadius = 20f;
    private float playerAttackRadius = 50f;
    private float attackCooldown = 1.0f;
    private float lastAttackTime = 0; 
    public int Blood;
    
    public Enemy(GameScreen screen, float x, float y , String MonsterName, HealthBar MonsterHealth) {
        super();
        this.x = x;
        this.y = y;
        this.stateTime = 0f;
        this.deathTime = 0f;  // NEW: Initialize death timer
        this.MonsterHealth = MonsterHealth;
        this.Blood = 100;
        this.screen = screen;
        this.player = screen.getPlayer();
        // Load the animations with proper frame splitting and timing adjustments
        idle = createAnimation(MonsterName+"-Idle.png", 6, 0.25f);
        walk = createAnimation(MonsterName+"-Walk.png", 8, 0.30f); 
        attack = createAnimation( MonsterName+"-Attack02.png", 6, 0.15f);
        Dead = createAnimation( MonsterName+"-Death.png", 4, 0.3f);  // Slightly slower for visibility
        // Default animation is idle
        currentAnimation = idle;  
    }
    
    public Enemy(Player player, String MonsterName, HealthBar MonsterHealth) {
    	super();
        this.player = player;
        this.x = 300f;
        this.y = 300f;
        this.stateTime = 0f;
        this.MonsterHealth = MonsterHealth;
        this.Blood = 1;


        // Load the animations with proper frame splitting and timing adjustments
        idle = createAnimation(MonsterName+"-Idle.png", 6, 0.25f); // 4 frames per animation, 0.25f time per frame
        walk = createAnimation(MonsterName+"-Walk.png", 8, 0.30f); // Adjust timing for walking animation
        attack = createAnimation( MonsterName+"-Attack02.png", 6, 0.15f);
        Dead = createAnimation( MonsterName+"-Death.png", 4, 0.5f);// Adjust timing for death animation
        currentAnimation = idle;  // Default animation is idle
         // Adjust path as needed
            }

    // Create the animation with custom frame size and timing
    private Animation<TextureRegion> createAnimation(String texturePath, int frames, float frameDuration) {
        Texture texture = new Texture(Gdx.files.internal(texturePath), true);  // Load with transparency support
        int frameWidth = texture.getWidth() / frames;
        int frameHeight = texture.getHeight();
        TextureRegion[][] tempFrames = TextureRegion.split(texture, frameWidth, frameHeight);
        TextureRegion[] framesArray = new TextureRegion[frames];

        for (int i = 0; i < frames; i++) {
            framesArray[i] = tempFrames[0][i];  // Assuming all frames are in the first row
        }

        return new Animation<>(frameDuration, framesArray);  // Create animation with custom frame duration
    }

    // Update the enemy's behavior based on the player's position
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float dx = player.getX() - x - 12;
        float dy = player.getY() - y - 15;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        // Handle dying state separately
        if(state == 1) {
            deathTime += deltaTime;  // Use separate death timer
            // Check if death animation is complete (4 frames * 0.3f = 1.2 seconds)
            if(Dead.isAnimationFinished(deathTime)) {
                state = 2;
            }
            return;  // Don't process other states while dying
        }
        
        // Don't update if dead
        if(state == 2) {
            return;
        }
        
        // Check if player can attack enemy (player has longer range)
        if (distance <= playerAttackRadius) {
            // Player can attack from further away
            if (player.AttackEnemy()) {
                this.takeDamage(100);
            }
            
            // Enemy can only attack if within its shorter attack range
            if (distance <= attackRadius) {
                currentAnimation = attack;
                if (stateTime - lastAttackTime >= attackCooldown) {
                    player.takeDamage(10);
                    lastAttackTime = stateTime;
                }
            } else {
                // Enemy is in player's range but not close enough to attack - chase
                currentAnimation = walk;
                float normalizedDx = dx / distance;
                float normalizedDy = dy / distance;
                x += normalizedDx * speed * deltaTime;
                y += normalizedDy * speed * deltaTime;
            }
        }
        // If the enemy is within detection range, chase the player
        else if (distance <= detectionRadius) {
            currentAnimation = walk;
            float normalizedDx = dx / distance;
            float normalizedDy = dy / distance;
            x += normalizedDx * speed * deltaTime;
            y += normalizedDy * speed * deltaTime;
        } 
        else {
            currentAnimation = idle;
        }

        stateTime += deltaTime;
    }
    
    public void takeDamage(int damage) {
        Blood -= damage;
        MonsterHealth.updateHealth(-damage);

        if (Blood <= 0 && state == 0) {  // Only trigger death once
            Blood = 0;
            deathTime = 0f;  // Reset death timer, not stateTime
            currentAnimation = Dead;
            state = 1;
            System.out.println("Monster is dead!");
        }
    }

    // Render the enemy using the current animation
    public void render(SpriteBatch batch) {
        // Don't render if fully dead
        if(state == 2) {
            return;
        }
        
        TextureRegion frame;
        if(state == 1) {
            // Use deathTime for death animation, don't loop
            frame = Dead.getKeyFrame(deathTime, false);
        } else {
            // Normal animations can loop
            frame = currentAnimation.getKeyFrame(stateTime, true);
        }
        
        batch.draw(frame, x, y, frame.getRegionWidth() * 3/5f, frame.getRegionHeight() * 3/5f);
    }

    // Dispose of textures to free up resources
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
    }

    public int getState() {
    	return this.state;
    }

	
}

