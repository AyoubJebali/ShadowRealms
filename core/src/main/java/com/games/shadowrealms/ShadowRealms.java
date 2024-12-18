package com.games.shadowrealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import screens.GameScreen;

public class ShadowRealms extends Game {
	
	public SpriteBatch batch ;
	private String playerName="";
	
    
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
		
		setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		//batch.dispose();
	}
	public String getPlayerName() {
		return this.playerName;
	}
	public void setPlayerName(String name) {
		 this.playerName=name;
	}
	
	@Override
	public void render () {
		super.render();
	}
}
