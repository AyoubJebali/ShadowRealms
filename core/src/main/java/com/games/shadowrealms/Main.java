package com.games.shadowrealms;


import com.badlogic.gdx.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.Player;
import entity.Enemy;

import map.TiledMapBench;

public class Main extends Game {
    private SpriteBatch batch;
    private Player player;
    private Enemy enemy;

    private TiledMapBench map;

    @Override
    public void create() {
        batch = new SpriteBatch();
        map = new TiledMapBench();
        map.create();

        // Initialize the player
        player = new Player();
        enemy = new Enemy(player);
    }

    @Override
    public void render() {
        // Update game logic
        player.handleInput();

        // Render the game map
        map.render();

        // Render player and other entities
        batch.begin();
        player.render(batch);
        enemy.render(batch);
        batch.end();

        // Render health bar separately
       

    }

    @Override
    public void dispose() {
        batch.dispose();

        player.dispose();
        map.dispose();

    }
}
