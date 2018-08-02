package platformer.gameobject.level2;

import platformer.builder.Services;
import platformer.gameobject.GameObject;
import platformer.gameobject.Player;
import platformer.gameobject.properties.IUpdatable;

// Create flying monsters the level
public class FlyingMonsterFactory extends GameObject
	implements IUpdatable {

	private final Services services;
	private final Player thePlayer;
	
	private final int maxCooldown = 200;
	private int cooldown = 0;
	
	public FlyingMonsterFactory(Services services, Player player) {
		services.updateService.register(this);
		
		this.services = services;
		this.thePlayer = player;
	}
	
	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		if (this.cooldown<0) {
			// Create a monster
			new FlyingMonster(this.services, this.thePlayer);
			
			// Reset the cooldown
			this.cooldown = this.maxCooldown;
		}
		else {
			this.cooldown--;
		}
	}

	@Override
	public void onUpdateEnd() {
		// TODO Auto-generated method stub
		
	}

}
