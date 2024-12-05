package entity;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {
    private Animation<TextureRegion> walkDown, walkUp, walkLeft, walkRight, idle;
    private Animation<TextureRegion> attackDown, attackUp, attackLeft, attackRight;
    private Animation<TextureRegion> currentAnimation;
    private float stateTime;
    private float x, y;
    private float speed = 100f; // Movement speed
    private int direction; // 0 = down, 1 = up, 2 = left, 3 = right

    public Player() {
        this.x = 100f;
        this.y = 100f;
        this.stateTime = 0f;

        // Load animations (replace with correct paths)

        walkDown = createAnimation("run_down_40x40.png");
        walkUp = createAnimation("run_up_40x40.png");
        walkLeft = createAnimation("run_left_40x40.png");
        walkRight = createAnimation("run_right_40x40.png");

        attackDown = createAnimation("attack_down_40x40.png");
        attackUp = createAnimation("attack_up_40x40.png");
        attackLeft = createAnimation("attack_left_40x40.png");
        attackRight = createAnimation("attack_right_40x40.png");
        idle = createAnimation("idle_down_40x40.png");

        // Default to idle animation
        currentAnimation = idle;

    }

    private Animation<TextureRegion> createAnimation(String texturePath) {
        Texture texture = new Texture(Gdx.files.internal(texturePath));

        TextureRegion[][] tempFrames = TextureRegion.split(texture, 40, 40);
        TextureRegion[] frames = new TextureRegion[4]; // Assuming 4 frames per animation

        for (int i = 0; i < 4; i++) {
            frames[i] = tempFrames[0][i]; // Extract the first row of frames
        }

        return new Animation<>(0.1f, frames); // 0.1 seconds per frame
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y);
    }

    public void handleInput() {
        float deltaTime = Gdx.graphics.getDeltaTime();
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
            currentAnimation = idle;
        }


        // Keep the player within screen bounds
        x = Math.max(0, Math.min(x, Gdx.graphics.getWidth() - 40));
        y = Math.max(0, Math.min(y, Gdx.graphics.getHeight() - 40));
    }

    public void dispose() {
        // Dispose of any resources
        if (walkDown != null) walkDown.getKeyFrames()[0].getTexture().dispose();
        if (walkUp != null) walkUp.getKeyFrames()[0].getTexture().dispose();
        if (walkLeft != null) walkLeft.getKeyFrames()[0].getTexture().dispose();
        if (walkRight != null) walkRight.getKeyFrames()[0].getTexture().dispose();
        if (attackDown != null) attackDown.getKeyFrames()[0].getTexture().dispose();
        if (attackUp != null) attackUp.getKeyFrames()[0].getTexture().dispose();
        if (attackLeft != null) attackLeft.getKeyFrames()[0].getTexture().dispose();
        if (attackRight != null) attackRight.getKeyFrames()[0].getTexture().dispose();
        if (idle != null) idle.getKeyFrames()[0].getTexture().dispose();
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

