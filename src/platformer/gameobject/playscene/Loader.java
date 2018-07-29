package platformer.gameobject.playscene;

import platformer.builder.ServicePack;
import platformer.services.SoundService;

public class Loader {
	public Loader(SoundService soundService) {
		soundService.loadSound("Explosion_small.wav");
		soundService.loadSound("Explosion_large.wav");
		soundService.loadSound("Music.mp3");
		soundService.loadSound("Music.wav");
	}
}
