package com.games.shadowrealms;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import map.TiledMapBench;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    private TiledMapBench map = new TiledMapBench();
    private Texture image;
    private float x, y;
    TextureRegion[][] tmp;
    TextureRegion[] animationFrames;
    float frameDuration = 0.1f; // Time per frame in seconds
    Animation<TextureRegion> animation;
    private float speed = 50;
    
    @Override
    public void create() {
    	map.create();
        batch = new SpriteBatch();
        x=100;
        y=100;
        image = new Texture("run_down_40x40.png");
        
     // Assume the sprite sheet has 4 frames of equal width and height
        int frameCols = 6; // Number of columns (frames horizontally)
        int frameRows = 1; // Number of rows (frames vertically)

        tmp = TextureRegion.split(image, 
        		image.getWidth() / frameCols, 
        		image.getHeight() / frameRows);

        // Flatten the 2D array into a 1D array
        animationFrames  = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                animationFrames[index++] = tmp[i][j];
            }
        }
        
    }
    
    float stateTime = 0f;
    @Override
    public void render() {
    	input();
    	map.render();
    	animation = new Animation<>(frameDuration, animationFrames);
        //ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        stateTime += Gdx.graphics.getDeltaTime();
        
        // Get the current frame based on stateTime
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true); // 'true' for looping
        
        // Clear the screen
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(currentFrame, x, y , currentFrame.getRegionWidth()*2 , currentFrame.getRegionWidth()*2);
        batch.end();
    }
    private void input() {
    	float deltaTime = Gdx.graphics.getDeltaTime(); // Time elapsed since last frame

        // Move left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * deltaTime;
        }

        // Move right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * deltaTime;
        }

        // Move up
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed * deltaTime;
        }

        // Move down
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed * deltaTime;
        }
        x = Math.max(0, Math.min(x, Gdx.graphics.getWidth() - image.getWidth() / 6));
        y = Math.max(0, Math.min(y, Gdx.graphics.getHeight() - image.getHeight()*4));
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
