package com.games.shadowrealms;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import entity.Player;
import map.TiledMapBench;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    private TiledMapBench map = new TiledMapBench();
    private Texture image;
    private int x, y;
    TextureRegion[][] tmp;
    TextureRegion[] animationFrames;
    float frameDuration = 0.1f; // Time per frame in seconds
    Animation<TextureRegion> animation;
    private float speed = 50;
    private Player player;
    OrthographicCamera camera = new OrthographicCamera();
    @Override
    public void create() {
        camera.setToOrtho(false, 320, 180);
    	map.create();
    	Vector2 v ;
    	v = map.findFirstWalkable();
        batch = new SpriteBatch();
        x=(int)v.x*16;
        y=(int)v.y*16;
        player = new Player(640,390,50);
//        image = new Texture("run_down_40x40.png");
//        // Assume the sprite sheet has 4 frames of equal width and height
//        int frameCols = 6; // Number of columns (frames horizontally)
//        int frameRows = 1; // Number of rows (frames vertically)
        
//        tmp = TextureRegion.split(image, 
//        		image.getWidth() / frameCols, 
//        		image.getHeight() / frameRows);
//
//        // Flatten the 2D array into a 1D array
//        animationFrames  = new TextureRegion[frameCols * frameRows];
//        int index = 0;
//        for (int i = 0; i < frameRows; i++) {
//            for (int j = 0; j < frameCols; j++) {
//                animationFrames[index++] = tmp[i][j];
//            }
//        }
        
    }
    
    float stateTime = 0f;
    @Override
    public void render() {
    	//input();
    	map.render();
        //ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        stateTime += Gdx.graphics.getDeltaTime();
        
        
        // Update player input and render
        player.handleInput();
        
        
        camera.update();
        // Clear the screen
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        player.render(batch);
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
        
    }
    
    @Override
    public void dispose() {
        batch.dispose();
       
    }
}
