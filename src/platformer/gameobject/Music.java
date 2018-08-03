package platformer.gameobject;

import platformer.builder.Services;
import platformer.gameobject.properties.IUpdatable;

public class Music extends GameObject implements IUpdatable {
	private final Services services;
	
	// Have a small delay in playing music, so the game
	// have time to load it to memory
	int delay = 5;
	
	public Music(Services services) {
		this.services = services;
		
		services.updateService.register(this);
	}
	
	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		delay--;
		
		if (delay<0) {
			services.soundService.playSoundLoopNonStop("Music.mp3");
			this.destroy();
		}
	}

	@Override
	public void onUpdateEnd() {
		// TODO Auto-generated method stub
		
	}
	
}
