package platformer.gameobject;

import platformer.builder.Services;
import platformer.datastructures.Position;

public class SpeedEffect extends PowerupEffect {

	@Override
	protected void effect(Player player) {
		player.speedUp();
	}

	@Override
	protected String getImageFileName() {
		return "powerup-speed.png";
	}

}
