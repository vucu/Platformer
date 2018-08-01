package platformer.services;

import platformer.builder.Builder;
import platformer.datastructures.Level;

public class SceneManagerService {
	boolean transition = false;
	boolean enable = true;
	Level scene = Level.Level1;

	public void goTo(Level scene) {
		transition = true;
		this.scene = scene;
	}

	public void update() {
		if (transition && enable) {
			enable = false;

			// Calll builder to build the correct level
			switch (scene) {
			case Level1:
				Builder.buildLevel1();
				break;
			case Level2:
				Builder.buildLevel2();
				break;
			default:
				// User enters an invalid scene, nothing will change
				System.out.println("Invalid scene!");
				transition = false;
				enable = true;
			}
		}
	}
}
