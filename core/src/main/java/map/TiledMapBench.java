package map;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

public class TiledMapBench extends InputAdapter {

	private TiledMap map;
	private Texture tiles;
	private static Random rnd = new Random();
	private List<BSPNode> rooms= new ArrayList<>();;
	int tileSize = 16;
	private TextureRegion  wallTexture, wallRightTexture , wallLeftTexture , wallUpTexture , wallDownTexture , floorTexture;
	int[][] mapArray ; // 2D array to store the map tiles.
	int[][] rotatedArray;
	int mapWidth = 200; // Map width in tiles. 
	int mapHeight = 200; // Map height in tiles.
	
	
	public TiledMap create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		mapArray = new int[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++) {
		    for (int j = 0; j < mapHeight; j++) {
		        mapArray[i][j] = 1;
		    }
		}
		BSPNode node = new BSPNode(0,0,mapWidth,mapHeight);
		split(node,50);
		createRooms(node);
		connectRooms(node);
		loadTiles();
		map = new TiledMap();
		TiledMapTileLayer layer1 = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);
		for (int x = 0; x < mapWidth; x++) {
	        for (int y = 0; y < mapHeight; y++) {
	        	Cell cell = new Cell();
	            if (mapArray[x][y] == 1) {
	            	if(y>0 && mapArray[x][y-1] == 0) {
	            		cell.setTile(new StaticTiledMapTile(wallUpTexture)); // Wall Up
	            	}else if(y<mapHeight-1 && mapArray[x][y+1] == 0){
	            		cell.setTile(new StaticTiledMapTile(wallDownTexture)); // Wall Down
	            	}else if(x>0 && mapArray[x-1][y] == 0) {
	            		cell.setTile(new StaticTiledMapTile(wallRightTexture)); // Wall Right
	            	}else if(x<mapWidth-1 && mapArray[x+1][y] == 0) {
	            		cell.setTile(new StaticTiledMapTile(wallLeftTexture)); // Wall Left
	            	}
	            	else{
	            		cell.setTile(new StaticTiledMapTile(wallTexture)); // Default wall outside dungeon
	            	}
	            	layer1.setCell(x, y, cell);
	            } else {
	            	
	            	
	            		cell.setTile(new StaticTiledMapTile(floorTexture)); // Ground 
	            		layer1.setCell(x, y, cell);
	            	
	            }
	        }
	    }
		
		
		map.getLayers().add(layer1);
		rotatedArray = new int[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                rotatedArray[mapHeight - j - 1][i] = mapArray[i][j];
            }
        }
		/*
		 * try (BufferedWriter writer = new BufferedWriter(new
		 * FileWriter("output.txt"))) { for (int x = 0; x < mapWidth; x++) { for (int y
		 * = 0; y < mapHeight; y++) { writer.write(rotatedArray[x][y] + " "); // Write
		 * each element separated by a space } writer.newLine(); // Move to the next
		 * line after each row } } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
		return map;
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
	void loadTiles() {
		tiles = new Texture("Dungeon_Tileset.png");
		TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16);
		floorTexture = splitTiles[1][6];
		wallTexture = splitTiles[7][8];
		wallUpTexture = splitTiles[5][1];
		wallRightTexture = splitTiles[1][5];
		wallLeftTexture = splitTiles[1][0];
		wallDownTexture = splitTiles[4][1];
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
	
	public boolean checkBlockedTile(float x, float y) {
		int newY = (int) (3200 - y);
		int tileX = (int) ((x+8) / tileSize);
        int tileY = (int) ((newY-4) / tileSize);
        // Check bounds
        if (tileX < 0 || tileY < 0 || tileY >= rotatedArray.length || tileX >= rotatedArray[0].length) {
            // If the position is outside the map, consider it a collision
            return true;
        }
        // Return true if it's a wall (1), false if it's walkable (0)
        return rotatedArray[tileY][tileX] == 1;
	}
	public TiledMap getMap() {
		return map;
	}
	
	public List<BSPNode> getRooms() {
		return rooms;
	}
	
	   /**
     * Generates random positions inside a rectangle where enemies can spawn.
     * @param startX Top-left corner X position.
     * @param startY Top-left corner Y position.
     * @param width  Width of the rectangle.
     * @param height Height of the rectangle.
     * @param enemyCount Number of enemy spawn positions to generate.
     * @return ArrayList of Vector2 positions within the rectangle.
     */
	public  ArrayList<Vector2> getRandomEnemySpawnPositions(int startX, int startY, int width, int height, int enemyCount) {
		// TODO correct room coordinates bcs enemy spawns out of the room
        ArrayList<Vector2> spawnPositions = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < enemyCount; i++) {
            // Generate random x and y within the rectangle's bounds
            float randomX = (startX-32) + random.nextInt(width-20);
            float randomY = (startY-32) + random.nextInt(height-20);
            spawnPositions.add(new Vector2(randomX, randomY));
        }

        return spawnPositions;
    }
	public ArrayList<Vector2> getEnemySpawn(){
		ArrayList<Vector2> spawnPositions = new ArrayList<>();
		
		// Get player starting position (first room center)
		Vector2 playerStart = findFirstWalkable();
		float playerStartX = playerStart.x * 16;
		float playerStartY = playerStart.y * 16;
		float safeRadius = 150f; // Minimum distance from player spawn
		
		int roomIndex = 0;
		for (BSPNode room : rooms) {
			// Skip the first room (player starting room) - keep it safe
			if (roomIndex == 0) {
				roomIndex++;
				continue;
			}
			
			// Reduce enemy count based on room distance from start
			// Closer rooms get fewer enemies, farther rooms get more
			int enemyCount;
			if (roomIndex == 1) {
				enemyCount = 2; // Very few in adjacent room
			} else if (roomIndex == 2) {
				enemyCount = 3;
			} else if (roomIndex <= 4) {
				enemyCount = 5;
			} else {
				enemyCount = 7; // Max 7 per room for distant rooms
			}
			
	        // Get spawn positions for the current room
	        ArrayList<Vector2> roomSpawns = getRandomEnemySpawnPositions(
	                room.getRoomX() * 16,
	                room.getRoomY() * 16,
	                room.getRoomWidth() * 16,
	                room.getRoomHeight() * 16,
	                enemyCount
	        );
	        
	        // Only filter spawns in room 1 (adjacent to start)
	        // Other rooms are far enough away already
	        if (roomIndex == 1) {
	        	for (Vector2 spawn : roomSpawns) {
		        	float dx = spawn.x - playerStartX;
		        	float dy = spawn.y - playerStartY;
		        	float distance = (float) Math.sqrt(dx * dx + dy * dy);
		        	
		        	// Only add spawn if it's far enough from player start
		        	if (distance >= safeRadius) {
		        		spawnPositions.add(spawn);
		        	}
		        }
	        } else {
	        	// For other rooms, add all spawns
	        	spawnPositions.addAll(roomSpawns);
	        }
	        
	        roomIndex++;
	    }
		
		System.out.println("Total enemies spawned: " + spawnPositions.size() + " across " + (roomIndex - 1) + " rooms");
		return spawnPositions;
	}

}