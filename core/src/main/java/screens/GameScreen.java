package screens;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.games.shadowrealms.ShadowRealms;

import entity.Enemy;
import entity.HealthBar;
import entity.Player;
import map.TiledMapBench;
import scenes.Hud;

public class GameScreen implements Screen {
		
	private ShadowRealms game;
	
	//basic playscreen variables
    private OrthographicCamera camera;
    private Viewport gamePort;
    private Hud hud;
    
    // Virtual Screen resolution
    float virtualWidth = 320;  
    float virtualHeight = 240; 
    private Player player;
	//Tiled map variables
	private TiledMapBench mapGen;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	public Integer enemyDefeated;
	private Array<Enemy> enemies;
	private ArrayList<Vector2> enemyPositions;
	public GameScreen(ShadowRealms game) {
			this.game=game;
			
			game.batch = new SpriteBatch();
			
			this.enemyDefeated=0;
			// Generate the map
			mapGen= new TiledMapBench();
			map = mapGen.create();
			this.renderer = new OrthogonalTiledMapRenderer(map);
			
			// a camera object to follow the player
			camera = new OrthographicCamera();
			camera.setToOrtho(false, virtualWidth, virtualHeight);
			
			// a FitViewport to maintain virtual aspect ratio despite screen size
			gamePort = new FitViewport(virtualWidth, virtualHeight, camera);
			
			//initially set our gamcam to be centered correctly at the start of of map
			camera.position.set(mapGen.findFirstWalkable().x*16, mapGen.findFirstWalkable().y*16, 0);
			
			//create our game HUD for scores/timers/level info
	        hud = new Hud(game.batch, this);
	        
			// initiate player
			player = new Player(this);
			// enemy for test 
			HealthBar MonsterHealth = new HealthBar(0,0,40,5,10,10);
			//this.enemy = new Enemy(this,mapGen.findFirstWalkable().x*16,mapGen.findFirstWalkable().y*16, "Orc", MonsterHealth);
			
			this.enemyPositions = mapGen.getEnemySpawn();
			enemies = new Array<Enemy>();
			
			for(Vector2 position : this.enemyPositions) {
				enemies.add(new Enemy(this,position.x,position.y, "Orc", MonsterHealth));
				
			}
			
			
		}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	public void update(float delta) {
		player.update();
		Iterator<Enemy> iterator = enemies.iterator();
		while (iterator.hasNext()) {
		    Enemy enemy = iterator.next();
		    enemy.update();

		    if (enemy.getState() == 2) {
		        Hud.addScore(1);
		        enemy.dispose();
		        iterator.remove(); // Safely remove the enemy
		    }
		}
		
		hud.update(delta);
		camera.position.set(player.getMapCord().x,player.getMapCord().y, 0);
		camera.update();
		renderer.setView(camera); 
		
		if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
		
	}
	@Override
	public void render(float delta) {
		update(delta);
		//Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//render our game map
        renderer.render();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        
        for(Enemy enemy : enemies) {
			enemy.render(game.batch);
		}
        player.render(game.batch);
        game.batch.end();
        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		gamePort.update(width,height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		map.dispose();
		player.dispose();
		
		hud.dispose();
	}
	
	public TiledMapBench getMapGen() {
		return this.mapGen;
	}
	public Player getPlayer() {
		return player;
	}
	public Hud getHud(){ return hud; }
	
	public Integer getEnemiesDefeated() {
		return enemyDefeated;
	}
	public boolean gameOver(){
        if(player.isDead() == true && player.getStateTime() > 1f){
            return true;
        }
        return false;
    }
}