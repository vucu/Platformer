package platformer.gameobject;

import platformer.builder.Services;

public class InvincibleEffect extends PowerupEffect {

	@Override
	protected void effect(Player player) {
		player.invincible();
	}

	@Override
	protected String getImageFileName() {
		return "powerup-invincible.png";
	}
	
}
