package entity;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;

public class Audio {
    private Music backgroundMusic;
    private Sound effectSound;

    public Audio(String musicFilePath, String soundEffectFilePath) {
        if (musicFilePath != null) {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(musicFilePath));
        }
        if (soundEffectFilePath != null) {
            effectSound = Gdx.audio.newSound(Gdx.files.internal(soundEffectFilePath));
        }
    }

    public void playMusic(boolean loop) {
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(loop);
            backgroundMusic.play();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void playEffect() {
        if (effectSound != null) {
            effectSound.play();
        }
    }

    public void dispose() {
    	if (backgroundMusic != null) {
    		backgroundMusic.dispose();
        }
    	if (effectSound != null) {
            effectSound.dispose();
        }
        
    }
}
