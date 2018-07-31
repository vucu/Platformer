package platformer.services.delegates;

import java.awt.Rectangle;

import platformer.datastructures.Position;

public class SideScrollingCamera extends Camera {
	public SideScrollingCamera(Rectangle world, int w, int h) {
		super(world, w, h);
	}
	
	public SideScrollingCamera(Rectangle world, int screenX, int screenY, int w, int h) {
		super(world, screenX, screenY, w, h);
	}
	
	public SideScrollingCamera(Rectangle world, int screenX, int screenY, int w, int h, double scale) {
		super(world, screenX, screenX, screenY, w, h);
	}

	// Update every frame
	@Override
	public void update() {
		if (trackingObject == null) {
			return;
		}
		
		int gap = 150;
		Position objectPosition = trackingObject.getPosition();
		
		this.x = objectPosition.x - gap;
		
		// The camera cannot see beyond the end of the world
		if (x + w > world.x + world.width) {
			x = world.x + world.width - w;
		}
		
		// The camera cannot see before the beginning of the world
		if (x < world.x) {
			x = world.x;
		}
	}
}
