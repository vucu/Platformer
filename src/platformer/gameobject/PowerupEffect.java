package platformer.gameobject;

public abstract class PowerupEffect {
	// The child class will decide what effect this powerup has 
	protected abstract void effect(Player player);
	
	// The child class will decide the image to use
	protected abstract String getImageFileName();
}
