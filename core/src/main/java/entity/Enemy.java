package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Entity {

    private Player player;
    private float stateTime;
    private Animation<TextureRegion> idle, attack, currentAnimation, walk;
    private Texture idleTexture, attackTexture, walkTexture;
    private boolean isAttacking;

    // Constructor
    public Enemy(Player player) {
        super(300, 300, 300); // Start position and speed
        this.player = player;

        // Load textures and animations
        idleTexture = new Texture(Gdx.files.internal("Orc-Idle.png"));
        attackTexture = new Texture(Gdx.files.internal("Orc-Attack01.png"));
        walkTexture = new Texture(Gdx.files.internal("Orc-Walk.png"));

        idle = createAnimation(idleTexture, 6);
        attack = createAnimation(attackTexture, 6);
        walk = createAnimation(walkTexture, 6);
        currentAnimation = idle;

        stateTime = 0f;
        isAttacking = false;
    }

    private Animation<TextureRegion> createAnimation(Texture texture, int numberFrames) {
        TextureRegion[][] tempFrames = TextureRegion.split(texture, 100, 100); // Assuming 100x100 frames
        TextureRegion[] frames = new TextureRegion[numberFrames];

        for (int i = 0; i < numberFrames; i++) {
            frames[i] = tempFrames[0][i]; // First row only
        }

        return new Animation<>(0.1f, frames); // 0.1 seconds per frame
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float speed = 150; // Enemy speed in pixels per second
        float detectionRadius = 200; // Distance at which the enemy starts chasing the player
        float attackRadius = 40; // Distance at which the enemy starts attacking

        // Calculate the distance to the player
        float dx = player.getX() - this.x;
        float dy = player.getY() - this.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance <= attackRadius) {
            // Attack the player
            isAttacking = true;
            currentAnimation = attack;
        } else if (distance <= detectionRadius) {
            // Chase the player
            isAttacking = false;
            currentAnimation = walk;

            // Normalize movement vector and update position
            float normalizedDx = dx / distance;
            float normalizedDy = dy / distance;

            this.x += normalizedDx * speed * deltaTime;
            this.y += normalizedDy * speed * deltaTime;
        } else {
            // Player is out of detection range, idle
            isAttacking = false;
            currentAnimation = idle;
        }
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public void dispose() {
        idleTexture.dispose();
        attackTexture.dispose();
        walkTexture.dispose();
    }

    @Override
    public void handleInput() {
        // Handle input if needed (not necessary for AI enemy)
    }
}
