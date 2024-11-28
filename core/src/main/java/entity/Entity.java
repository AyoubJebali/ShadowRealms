package entity;

import com.badlogic.gdx.math.Vector2;

public class Entity {
	 	
	protected String id;
    //protected ResourceManager rm;

    // animation
    //protected AnimationManager am;
    protected boolean pauseAnim = false;
    // battle scene animation
    //protected AnimationManager bam;

    // position (x,y) in map coordinates (tile * tileSize)
    protected Vector2 position;

    // map
    //protected TileMap tileMap;

    /******** RPG ASPECTS *********/

    //protected Moveset moveset;

    protected boolean dead = false;

    protected int hp;
    protected int maxHp;
    // for animation to keep track of hp difference between attacks
    protected int previousHp;
    // for applying the hp change after the dialogue is finished
    protected int damage = 0;
    
    // 0-100 in % points
    protected int accuracy;
    // damage range
    protected int minDamage;
    protected int maxDamage;

   
    // level up information
    protected int level;

    // move type used default -1
    protected int prevMoveUsed = -1;
    protected int moveUsed = -1;

    public Entity(String id) {
        this.id = id;
        

        position = new Vector2();
    }
		
}
