package entity;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
    private Animation<TextureRegion> walkDown, walkUp, walkLeft, walkRight, idle;
    private Animation<TextureRegion> currentAnimation = idle;
    private float stateTime=0f; // Keeps track of the elapsed time for animation
    private int Direction; // Tracks the direction: 0 = down, 1 = up, 2 = left, 3 = right

    public Player() {
        super(1280, 100, 150); // Initial position (x, y) and speed
     // Load textures for all animations
        loadAnimations();
    }
    public Player(int x , int y , int speed) {
        super(x, y, speed); // Initial position (x, y) and speed

        // Load textures for all animations
        loadAnimations();
        // Default animation
        currentAnimation = idle;
        stateTime = 0f;
    }

    private Animation<TextureRegion> createAnimation(String texturePath , int numberFrames) {
        Texture texture = new Texture(Gdx.files.internal(texturePath));
        TextureRegion[][] tempFrames = TextureRegion.split(texture, 40, 40); // Assuming each frame is 40x40
        TextureRegion[] frames = new TextureRegion[numberFrames]; // Assuming 4 frames for animation

        for (int i = 0; i < numberFrames; i++) {
            frames[i] = tempFrames[0][i]; // Assuming 4 frames in one row
        }

        return new Animation<>(0.1f, frames); // Frame duration of 0.1 seconds
    }

    @Override
    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime(); // Update animation state time

        // Get the current frame based on animation state
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        // Draw the current frame at the player's position
        batch.draw(currentFrame, x, y ,currentFrame.getRegionWidth()*2, currentFrame.getRegionHeight()*2 );
        
    }

    @Override
    public void handleInput() {
        float deltaTime = Gdx.graphics.getDeltaTime(); // Handle movement based on deltaTime

        boolean isMoving = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * deltaTime;
            Direction = 2; // Update the direction to left
            currentAnimation = walkLeft;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * deltaTime;
            Direction = 3; // Update the direction to right
            currentAnimation = walkRight;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed * deltaTime;
            Direction = 1; // Update the direction to up
            currentAnimation = walkUp;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed * deltaTime;
            Direction = 0; // Update the direction to down
            currentAnimation = walkDown;
            isMoving = true;
        }

        // If not moving, reset to idle animation
        if (!isMoving) {
            currentAnimation = idle;
        }

        // Keep player within screen bounds
        
    }
    public float getX() {
    	return x;
    }
    public float getY() {
    	return x;
    }
    @Override
    public void dispose() {
        // Dispose of all textures used in the animations
        walkDown.getKeyFrames()[0].getTexture().dispose();
        walkUp.getKeyFrames()[0].getTexture().dispose();
        walkLeft.getKeyFrames()[0].getTexture().dispose();
        walkRight.getKeyFrames()[0].getTexture().dispose();
        idle.getKeyFrames()[0].getTexture().dispose();
    }
    void loadAnimations() {
    	 walkDown = createAnimation("run_down_40x40.png" ,6);
         walkUp = createAnimation("run_up_40x40.png" ,6);
         walkLeft = createAnimation("run_left_40x40.png",6);
         walkRight = createAnimation("run_right_40x40.png",6);
         idle = createAnimation("idle_down_40x40.png",4);
    }
}