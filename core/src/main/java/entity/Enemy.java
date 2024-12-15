package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Entity{
    private float x, y;
    private float speed = 100f;
    private Player player;
    private float stateTime;
    private Animation<TextureRegion> idle, walk, attack, currentAnimation,Dead;
    
    private HealthBar MonsterHealth;

    private float detectionRadius = 200f;  // The radius within which the enemy detects the player
    private float attackRadius = 1f;      // The radius within which the enemy will attack the player
    private float attackCooldown = 1.0f;   // Time between attacks
    private float lastAttackTime = 0; 
    public int Blood ;
    // Time of the last attack
   
    

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
        attack = createAnimation( MonsterName+"-Attack02.png", 6, 0.25f);
        Dead = createAnimation( MonsterName+"-Death.png", 4, 0.25f);// Adjust timing for attack animation
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
        float dx = player.getX() - x - 42;
        float dy = player.getY() - y - 35;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // If the enemy is within attack range, attack the player
        if (distance <= attackRadius) {
            
            currentAnimation = attack;
            if (player.AttackEnemy()) {
            	
            	this.takeDamage(1000);
				
			}

            if (stateTime - lastAttackTime >= attackCooldown) {
                player.takeDamage(10);
                // Attack the player
                
                lastAttackTime = stateTime;
            }
        } 
        // If the enemy is within detection range, chase the player
        else if (distance <= detectionRadius) {
            
            currentAnimation = walk;
            // Normalize the direction and move towards the player
            float normalizedDx = dx / distance;
            float normalizedDy = dy / distance;

            x += normalizedDx * speed * deltaTime ;
            y += normalizedDy * speed * deltaTime;
        } 
        // If the player is out of range, return to idle state
        else {
            
            currentAnimation = idle;
        }

        stateTime += deltaTime;
    }
    public void takeDamage(int damage) {
        Blood -= damage; // Subtract health
        MonsterHealth.updateHealth(-damage); // Update health bar

        if (Blood <= 0) {
            Blood = 0; // Prevent negative health
            stateTime = 0;
            currentAnimation = Dead;
            System.out.println("Monster is dead!");
            // You can trigger a death animation or remove the enemy here
        }
    }

   

    // Render the enemy using the current animation
    public void render(SpriteBatch batch) {
    	TextureRegion frame = currentAnimation.getKeyFrame(stateTime, true);
    	batch.draw(frame, x, y,frame.getRegionWidth()*2,frame.getRegionHeight()*2);
        MonsterHealth.render(batch,x+40, y+80);
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

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	
}

