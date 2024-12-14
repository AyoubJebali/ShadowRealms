package entity;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {
    private Animation<TextureRegion> walkDown, walkUp, walkLeft, walkRight, idleDown, idleUp, idleRight, idleLeft;
    private Animation<TextureRegion> attackDown, attackUp, attackLeft, attackRight;
    private Animation<TextureRegion> DeathDown, DeathUp, DeathLeft, DeathRight;
    private Animation<TextureRegion> currentAnimation;
    private float stateTime;
    private float x, y;
    private float speed = 100f; // Movement speed
    public int direction; // 0 = down, 1 = up, 2 = left, 3 = right
    private int health;
    private HealthBar healthBar;
    private Audio audio;
    public boolean isAttacking;

    public Player(HealthBar healthBar ) {
        this.x = 100f;
        this.y = 100f;
        this.stateTime = 0f;
        this.health = 100; // Default health
        this.healthBar = healthBar;

        // Load animations (replace with correct paths)

        walkDown = createAnimation("run_down_40x40.png",6);
        walkUp = createAnimation("run_up_40x40.png",6);
        walkLeft = createAnimation("run_left_40x40.png",6);
        walkRight = createAnimation("run_right_40x40.png",6);
        attackDown = createAnimation("attack_down_40x40.png",7);
        attackUp = createAnimation("attack_up_40x40.png",7);
        attackLeft = createAnimation("attack_left_40x40.png",7);
        attackRight = createAnimation("attack_right_40x40.png",7);
        idleDown = createAnimation("idle_down_40x40.png",4);
        idleUp = createAnimation("idle_up_40x40.png",4);
        idleRight = createAnimation("idle_right_40x40.png",4);
        idleLeft = createAnimation("idle_left_40x40.png",4);
        // Load animations (replace with correct paths)
        DeathDown = createAnimation("death_down_40x40.png",9);
        DeathUp = createAnimation("death_up_40x40.png", 9);
        DeathLeft = createAnimation("death_left_40x40.png", 9);
        DeathRight = createAnimation("death_right_40x40.png", 9);
        audio = new Audio("legends never die (slowed + reverb).mp3", null);
        audio.playMusic(true);
        // Default to idle animation
        currentAnimation = idleDown;
    }

    private Animation<TextureRegion> createAnimation(String texturePath, int frmes) {

        Texture texture = new Texture(Gdx.files.internal(texturePath));


        TextureRegion[][] tempFrames = TextureRegion.split(texture, 40, 40);

        // Dynamically create the frames array
        TextureRegion[] frames = new TextureRegion[frmes];

        for (int i = 0; i < frmes; i++) {
            frames[i] = tempFrames[0][i]; // Extract the first row of frames

        }

        return new Animation<>(0.1f, frames); // 0.1 seconds per frame
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);


        // Draw player sprite
       batch.begin();
        // Draw the current frame at the player's position
        batch.draw(currentFrame, x, y ,currentFrame.getRegionWidth()*2, currentFrame.getRegionHeight()*2 );
        // Render health bar at a position relative to the player
        healthBar.render(x, y + 50);
       batch.end(); 

    }

    public void handleInput() {

    	
        float deltaTime = Gdx.graphics.getDeltaTime(); // Handle movement based on deltaTime


        boolean isMoving = false;
        

        // Handle movement inputs
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * deltaTime;
            direction = 2; // Left

            currentAnimation = walkLeft;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * deltaTime;

            direction = 3; // Right

            currentAnimation = walkRight;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed * deltaTime;

            direction = 1; // Up

            currentAnimation = walkUp;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed * deltaTime;

            direction = 0; // Down

            currentAnimation = walkDown;
            isMoving = true;
        }


        // Handle attack input
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            switch (direction) {
                case 0 -> currentAnimation = attackDown;
                case 1 -> currentAnimation = attackUp;
                case 2 -> currentAnimation = attackLeft;
                case 3 -> currentAnimation = attackRight;
            }
            isMoving = true;
                     
            
        }

        // If not moving or attacking, use idle animation


        if (!isMoving) {
            switch (direction) {
                case 0 -> currentAnimation = idleDown;
                case 1 -> currentAnimation = idleUp;
                case 2 -> currentAnimation = idleLeft;
                case 3 -> currentAnimation = idleRight;
            }
        }



        // Keep the player within screen bounds
        x = Math.max(0, Math.min(x, Gdx.graphics.getWidth() - 40));
        y = Math.max(0, Math.min(y, Gdx.graphics.getHeight() - 40));
    }


    public void takeDamage(int damage) {
        health -= damage;
        healthBar.updateHealth(-damage); // Update health bar

        if (health <= 0) {
            health = 0; // Prevent negative health
            stateTime = 0; 
            System.out.println("Player is dead! Switching to death animation.");
            switch (direction) {
                case 0 -> currentAnimation = DeathDown;
                case 1 -> currentAnimation = DeathUp;
                case 2 -> currentAnimation = DeathLeft;
                case 3 -> currentAnimation = DeathRight;
            }
            System.out.println("Current Health: " + health); 
        }
    }
    public boolean AttackEnemy() {
    if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
    	return true;
    }
	return false;}
      
    
    
    

    public int getHealth() {
        return health;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public void dispose() {
        // Dispose animations and health bar
        healthBar.dispose();
    }
    


    void loadAnimations() {
    	 walkDown = createAnimation("run_down_40x40.png" ,6);
         walkUp = createAnimation("run_up_40x40.png" ,6);
         walkLeft = createAnimation("run_left_40x40.png",6);
         walkRight = createAnimation("run_right_40x40.png",6);
         idle = createAnimation("idle_down_40x40.png",4);
    }



	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}
	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}

}


