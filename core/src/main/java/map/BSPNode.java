package map;

import java.util.Random;

public class BSPNode {
    int x, y, width, height; // The bounds of this node.
    BSPNode left, right = null;
    Boolean isRoom = false;
    int roomX,roomY,roomWidth,roomHeight;
    // Children nodes (sub-regions).
    private static int MIN_SIZE = 46; // min room size
    private static Random rnd = new Random();
    // Constructor
    public BSPNode(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void setIsRoom(Boolean b , int x , int y , int height , int width) {
    	roomHeight=height;
    	roomWidth = width;
    	roomX=x;
    	roomY=y;
    	isRoom=b;
    }
    public Boolean isRoom() {
    	return isRoom;
    }
    public int getRoomX() {
    	if(isRoom) {
    		return roomX;
    	}else return -1;
    }
    public int getRoomY() {
    	if(isRoom) {
    		return roomY;
    	}else return -1;
    }
    public int getRoomHeight() {
    	if(isRoom) {
    		return roomHeight;
    	}else return -1;
    }
    public int getRoomWidth() {
    	if(isRoom) {
    		return roomWidth;
    	}else return -1;
    }
    
    // Check if the node is a leaf (has no children).
    public boolean isLeaf() {
        return left == null && right == null;
    }
    
}

