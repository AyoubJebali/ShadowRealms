package map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.ScreenUtils;
import com.games.shadowrealms.*;

public class TiledMapBench extends InputAdapter implements ApplicationListener {

	private TiledMap map;
	private TiledMapRenderer renderer;
	private OrthographicCamera camera;
	private OrthoCamController cameraController;
	private AssetManager assetManager;
	private Texture tiles;
	private Texture texture;
	private BitmapFont font;
	private SpriteBatch batch;
	private int CameraSpeed = 20;
	int tileSize = 16;
	//
	
	private TextureRegion  wallTexture;
	private TextureRegion  floorTexture;
	int[][] mapArray ; // 2D array to store the map tiles.
	int mapWidth = 200; // Map width in tiles.
	int mapHeight = 200; // Map height in tiles.
	
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		mapArray = new int[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++) {
		    for (int j = 0; j < mapHeight; j++) {
		        mapArray[i][j] = 1;
		    }
		}
		camera = new OrthographicCamera();
		camera.setToOrtho(false,720, 720);
		camera.update();
		cameraController = new OrthoCamController(camera);
		Gdx.input.setInputProcessor(cameraController);
		
		font = new BitmapFont();
		batch = new SpriteBatch();
		BSPNode node = new BSPNode(0,0,mapWidth,mapHeight);
		split(node,30);
		createRooms(node);
		connectRooms(node);
		tiles = new Texture("Dungeon_Tileset.png");
		TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16);
		floorTexture = splitTiles[8][8];
		wallTexture = splitTiles[1][6];
		map = new TiledMap();
//		MapLayers layers = map.getLayers();
//		for (int l = 0; l < 20; l++) {
//			TiledMapTileLayer layer = new TiledMapTileLayer(512, 512, 16, 16);
//			for (int x = 0; x < 32; x++) {
//				for (int y = 0; y < 32; y++) {
//					int tx = (int)(Math.random() * 6);
//					int ty = (int)(Math.random() * 4);
//					Cell cell = new Cell();
//					cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
//					layer.setCell(x, y, cell);
//				}
//			}
//			layers.add(layer);
//		}
		TiledMapTileLayer layer1 = new TiledMapTileLayer(512, 512, 16, 16);
		for (int x = 0; x < mapWidth; x++) {
	        for (int y = 0; y < mapHeight; y++) {
	        	Cell cell = new Cell();
	            if (mapArray[x][y] == 1) {
	            	cell.setTile(new StaticTiledMapTile(wallTexture));
	            	layer1.setCell(x, y, cell);
	                //batch.draw(wallTexture, x * tileSize, y * tileSize , wallTexture.getRegionWidth()*4 , wallTexture.getRegionWidth()*4);
	            } else {
	            	cell.setTile(new StaticTiledMapTile(floorTexture));
	            	layer1.setCell(x, y, cell);
	                //batch.draw(floorTexture, x * tileSize, y * tileSize , floorTexture.getRegionWidth()*4 , floorTexture.getRegionWidth()*4);
	            }
	        }
	    }
		map.getLayers().add(layer1);
		renderer = new OrthogonalTiledMapRenderer(map);

	}

	@Override
	public void render () {
		ScreenUtils.clear(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		
		  input(); 
		  camera.update(); 
		  renderer.setView(camera); 
		  renderer.render();
		 
		batch.begin();
//		for (int x = 0; x < mapWidth; x++) {
//	        for (int y = 0; y < mapHeight; y++) {
//	            if (mapArray[x][y] == 1) {
//	                batch.draw(wallTexture, x * tileSize, y * tileSize , wallTexture.getRegionWidth()*4 , wallTexture.getRegionWidth()*4);
//	            } else {
//	                batch.draw(floorTexture, x * tileSize, y * tileSize , floorTexture.getRegionWidth()*4 , floorTexture.getRegionWidth()*4);
//	            }
//	        }
//	    }
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
		
	}
	private void input() {
    	float deltaTime = Gdx.graphics.getDeltaTime(); // Time elapsed since last frame
    	
        // Move left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= CameraSpeed ;
        }

        // Move right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	camera.position.x += CameraSpeed ;
        }

        // Move up
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	camera.position.y += CameraSpeed ;
        }

        // Move down
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	camera.position.y -= CameraSpeed ;
        }
        
    }
	
	void split(BSPNode node, int minRoomSize) {
	    if (node.width <= minRoomSize  && node.height <= minRoomSize) return;

	    boolean splitHorizontally = node.width < node.height; // Favor splitting the longer dimension.
	    if (node.width == node.height) splitHorizontally = Math.random() > 0.5;

	    if (splitHorizontally) {
	        int splitPoint = (int) (Math.random() * (node.height - minRoomSize * 2)) + minRoomSize;
	        node.left = new BSPNode(node.x, node.y, node.width, splitPoint);
	        node.right = new BSPNode(node.x, node.y + splitPoint, node.width, node.height - splitPoint);
	    } else {
	        int splitPoint = (int) (Math.random() * (node.width - minRoomSize * 2)) + minRoomSize;
	        node.left = new BSPNode(node.x, node.y, splitPoint, node.height);
	        node.right = new BSPNode(node.x + splitPoint, node.y, node.width - splitPoint, node.height);
	    }

	    // Recursively split the children nodes.
	    split(node.left, minRoomSize);
	    split(node.right, minRoomSize);
	}
	void createRooms(BSPNode node) {
	    if (!node.isLeaf()) {
	        if (node.left != null) createRooms(node.left);
	        if (node.right != null) createRooms(node.right);
	    } else {
	        int roomWidth = (int) (Math.random() * (node.width - 16)) + 3; // Random room size.
	        int roomHeight = (int) (Math.random() * (node.height - 16)) + 3;
	        int roomX = node.x + (int) (Math.random() * (node.width - roomWidth));
	        int roomY = node.y + (int) (Math.random() * (node.height - roomHeight));

	        for (int x = roomX; x < roomX + roomWidth; x++) {
	            for (int y = roomY; y < roomY + roomHeight; y++) {
	                mapArray[x][y] = 0; // Mark room tiles as empty space.
	            }
	        }
	    }
	}
	void connectRooms(BSPNode node) {
	    if (!node.isLeaf()) {
	        connectRooms(node.left);
	        connectRooms(node.right);

	        // Get a random point from each child node's room.
	        BSPNode left = node.left;
	        BSPNode right = node.right;

	        int x1 = left.x + left.width / 2;
	        int y1 = left.y + left.height / 2;
	        int x2 = right.x + right.width / 2;
	        int y2 = right.y + right.height / 2;

	        // Create a horizontal corridor.
	        // add different Texture for entry and exit as doors 
	        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
	            mapArray[x][y1] = 0;
	            mapArray[x][y1+1] = 0;
	        }
	        // Create a vertical corridor.
	        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
	            mapArray[x2][y] = 0;
	            mapArray[x2+1][y] = 0;
	        }
	    }
	}


}