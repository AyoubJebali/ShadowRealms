package com.games.shadowrealms;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.GL20;
import entity.Player;
import entity.Enemy;
import entity.HealthBar;

import map.TiledMapBench;

import entity.Audio;

public class Main extends Game {
    private SpriteBatch batch;
    private Player player;
    private Enemy enemy;
    private TiledMapBench map; // Map handler
    private Audio audio;
    private HealthBar healthBar, MonsterHealth;


    @Override
    public void create() {
        batch = new SpriteBatch();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Initialize health bar
        healthBar = new HealthBar(0, 0, 40, 5, 100, 100);
        MonsterHealth = new HealthBar(0,0,40,5,10,10);

        // Create the map
        map = new TiledMapBench();
        map.create();
        
        
        // Initialize the player
        // Create player and enemy
        player = new Player(map);
        
        enemy = new Enemy(player, "Orc", MonsterHealth);
        


        // Initialize audio if needed (optional)
         //audio = new Audio(); // Uncomment if `Audio` has a proper constructor


    }

    @Override
    public void render() {

        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


       


        batch.begin();
        player.render(batch);
        
        enemy.render(batch);
        batch.end();

    }
    
    @Override
    public void dispose() {
        // Dispose resources
        batch.dispose();
        player.dispose();
        enemy.dispose();


        // Dispose audio safely
        if (audio != null) {
            audio.dispose();
        }

        // Dispose the health bar
        healthBar.dispose();
        MonsterHealth.dispose();

    }
}

