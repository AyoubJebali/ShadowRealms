package entity;



import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import map.OrthoCamController;
import map.TiledMapBench;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player extends Entity {
	private OrthographicCamera camera;
	private TiledMapBench map;
	private OrthoCamController cameraController;
    private Animation<TextureRegion> walkDown, walkUp, walkLeft, walkRight, idle;
    private Animation<TextureRegion> attackDown, attackUp, attackLeft, attackRight;
    private Animation<TextureRegion> currentAnimation;
    private float stateTime;
    
    private float speed = 100f; // Movement speed
    private int direction; // 0 = down, 1 = up, 2 = left, 3 = right

    public Player(TiledMapBench map) {
    	super(0,0,0);
    	this.map = map;
    	// Get Screen width and height
    	float screenCenterX = Gdx.graphics.getWidth() / 2f;
    	float screenCenterY = Gdx.graphics.getHeight() / 2f;
    	// Calculate the Center of the screen 
        float startX = screenCenterX - (80 / 2f);
        float startY = screenCenterY - (80 / 2f);
        // Set player coordinates to the center of the screen
        this.setScreenCord(startX,startY);
        this.stateTime = 0f;
        // Initiate Camera
        cameraInit();
        //Set player Coordinates same as camera 
        setMapCord(camera.position.x,camera.position.y);
        // Load animations
        loadAnimations();
        
        map.setCamera(camera);
        // Default to idle animation
        currentAnimation = idle;

    }


    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        camera.update();
        // Draw the current frame at the player's position
        batch.draw(currentFrame, getScreenCord().x, getScreenCord().y ,currentFrame.getRegionWidth()*2, currentFrame.getRegionHeight()*2 );
        

    }
    private Animation<TextureRegion> createAnimation(String texturePath , int numberFrames) {
    	Texture texture = new Texture(Gdx.files.internal(texturePath));
    	TextureRegion[][] tempFrames = TextureRegion.split(texture, 40, 40);
    	TextureRegion[] frames = new TextureRegion[numberFrames]; 
    	for (int i = 0; i < numberFrames; i++) {
    		frames[i] = tempFrames[0][i]; // Extract the first row of frames
    		
    	}
    	return new Animation<>(0.1f, frames); // 0.1 seconds per frame
    }
    
    @Override
    public void handleInput() {
        float deltaTime = Gdx.graphics.getDeltaTime(); // Handle movement based on deltaTime
        boolean isMoving = false;
        float x = camera.position.x;
        float y = camera.position.y;
        // Handle movement inputs
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        	x -= speed * deltaTime;
            direction = 2; // Left
            currentAnimation = walkLeft;
            isMoving = true;
            if(checkForCollision(x,y)==false) {
            	camera.position.x=x;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	x += speed * deltaTime;
            direction = 3; // Right
            currentAnimation = walkRight;
            isMoving = true;
            if(!checkForCollision(x,y)) {
            	camera.position.x=x;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	y += speed * deltaTime;
            direction = 1; // Up
            currentAnimation = walkUp;
            isMoving = true;
            if(!checkForCollision(x,y)) {
            	camera.position.y=y;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	y -= speed * deltaTime;
            direction = 0; // Down
            currentAnimation = walkDown;
            isMoving = true;
            if(!checkForCollision(x,y)) {
            	camera.position.y=y;
            }
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
        
        
        
    }


    private boolean checkForCollision(float x, float y) {
    	
		return map.checkBlockedTile(x, y);
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

    void loadAnimations() {
    	walkDown = createAnimation("run_down_40x40.png",6);
        walkUp = createAnimation("run_up_40x40.png",6);
        walkLeft = createAnimation("run_left_40x40.png",6);
        walkRight = createAnimation("run_right_40x40.png",6);

        attackDown = createAnimation("attack_down_40x40.png",6);
        attackUp = createAnimation("attack_up_40x40.png",6);
        attackLeft = createAnimation("attack_left_40x40.png",6);
        attackRight = createAnimation("attack_right_40x40.png",6);
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
	void cameraInit() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false,320, 320);
		camera.position.set(map.findFirstWalkable().x*16, map.findFirstWalkable().y*16, 0);
		camera.update();
		cameraController = new OrthoCamController(camera);
		Gdx.input.setInputProcessor(cameraController);
	}
}


