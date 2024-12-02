package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.badlogic.gdx.math.Vector2;
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
	private static Random rnd = new Random();
	private List<BSPNode> rooms= new ArrayList<>();;
	int tileSize = 16;
	//
	
	
	private TextureRegion  wallTexture;
	private TextureRegion  wallUpTexture;
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
		camera.setToOrtho(false,3200, 3200);
		camera.update();
		cameraController = new OrthoCamController(camera);
		Gdx.input.setInputProcessor(cameraController);
		
		font = new BitmapFont();
		batch = new SpriteBatch();
		BSPNode node = new BSPNode(0,0,mapWidth,mapHeight);
		split(node,50);
		createRooms(node);
		connectRooms(node);
		tiles = new Texture("Dungeon_Tileset.png");
		TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16);
		floorTexture = splitTiles[1][6];
		wallTexture = splitTiles[7][8];
		wallUpTexture = splitTiles[5][1];
		camera.position.set(1600, 1600, 0);
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
		TiledMapTileLayer layer1 = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);
		for (int x = 0; x < mapWidth; x++) {
	        for (int y = 0; y < mapHeight; y++) {
	        	Cell cell = new Cell();
	            if (mapArray[x][y] == 1) {
	            	if(y>0 && mapArray[x][y-1] == 0) {
	            		cell.setTile(new StaticTiledMapTile(wallUpTexture));
	            	}else {
	            		
	            		cell.setTile(new StaticTiledMapTile(wallTexture));
	            	}
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
		System.out.println(camera.position.x);
		batch.begin();
		//font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
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
	    if (node.width <= minRoomSize*2  && node.height <= minRoomSize*2) return;

	    boolean splitHorizontally = node.width < node.height; // Favor splitting the longer dimension.
	    if (node.width == node.height) splitHorizontally = Math.random() > 0.5;

	    if (splitHorizontally) {
	        int splitPoint = (int) (rnd.nextInt(node.height - minRoomSize * 2)) + minRoomSize;
	        node.left = new BSPNode(node.x, node.y, node.width, splitPoint);
	        node.right = new BSPNode(node.x, node.y + splitPoint, node.width, node.height - splitPoint);
	    } else {
	        int splitPoint = (int) (rnd.nextInt(node.width - minRoomSize * 2)) + minRoomSize;
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
	        int roomWidth = (int) (Math.random() * (node.width - 10)) + 8; // Random room size.
	        int roomHeight = (int) (Math.random() * (node.height - 10)) + 8;
	        int roomX = node.x + (int) (Math.random() * (node.width - roomWidth));
	        int roomY = node.y + (int) (Math.random() * (node.height - roomHeight));
	        node.setIsRoom(true, roomX, roomY, roomHeight, roomWidth);
	        for (int x = roomX; x < roomX + roomWidth; x++) {
	            for (int y = roomY; y < roomY + roomHeight; y++) {
	                mapArray[x][y] = 0; // Mark room tiles as empty space.
	            }
	        }
	    }
	}
	void connect2Rooms(BSPNode node1 ,BSPNode node2) {
		BSPNode left = node1;
        BSPNode right = node2;
        if(left.isRoom() == true && right.isRoom()==true) {
        	
        	int x1 = left.getRoomX() + left.getRoomWidth() / 2;
        	int y1 = left.getRoomY() + left.getRoomHeight() / 2;
        	int x2 = right.getRoomX() + right.getRoomWidth() / 2;
        	int y2 = right.getRoomY() + right.getRoomHeight() / 2;
        	
        	// Create a horizontal corridor.
        	// add different Texture for entry and exit as doors 
        	while (x1 != x2) {
        		mapArray[x1][y1] = 0;
        		mapArray[x1][y1+1] = 0;// Example: marking the path as walkable
        		x1 += (x2 > x1) ? 1 : -1; // Move left or right
        	}
        	
        	// Move vertically towards y2
        	while (y1 != y2) {
        		mapArray[x1][y1] = 0;
        		mapArray[x1+1][y1] = 0;// Example: marking the path as walkable
        		y1 += (y2 > y1) ? 1 : -1; // Move up or down
        	}
        }
    }
	
	void findRooms(BSPNode node, List<BSPNode> rms) {
		if(node.isLeaf()){
			if(node.isRoom()) {
				rms.add(node);
			}else {
				return;
			}
		}else {
			findRooms(node.left,rms);
			findRooms(node.right,rms);
			
		}
	}
	void connectRooms(BSPNode node) {
		
		findRooms(node,rooms);
		
		for (int i = 0; i < rooms.size()-1; i ++) {
			connect2Rooms(rooms.get(i),rooms.get(i+1));
		}
	    
	}
	
	public Vector2 findFirstWalkable() {
		
		BSPNode n ;
		n = rooms.getFirst();
		
		return new Vector2(n.getRoomX()+n.getRoomWidth()/2, n.getRoomY()+n.getRoomHeight()/2);
	    
	}

}