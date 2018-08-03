package platformer.services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundService {
	public SoundService() {
		// Allow Java FX features
		JFXPanel fxPanel = new JFXPanel(); 
	}
	
	private static Map<String,MediaPlayer> players = new HashMap<>();
	
	public void playSound(String fname) {
		this.loadSound(fname);
		
		MediaPlayer player = players.get(fname);
		player.stop();
		player.play();
	}
	
	public void playSoundLoop(String fname) {
		this.loadSound(fname);
		
		MediaPlayer player = players.get(fname);
		player.stop();
		player.setCycleCount(Integer.MAX_VALUE);
		player.play();
	}
	
	public void playSoundLoopNonStop(String fname) {
		boolean isPlaying = players.containsKey(fname);
		
		this.loadSound(fname);
		
		if (!isPlaying) {
			MediaPlayer player = players.get(fname);
			player.stop();
			player.setCycleCount(Integer.MAX_VALUE);
			player.play();	
		}
	}
	
	
	// Load the sound, but not playing it 
	private void loadSound(String fname) {
		if (!players.containsKey(fname)) {
			Media hit = new Media(new File(fname).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(hit);
			players.put(fname, mediaPlayer);
		}
	}
}
