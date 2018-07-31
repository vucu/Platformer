package platformer.services.delegates;

import java.awt.Rectangle;

import platformer.datastructures.Position;

// This is the camera in Tanks Wars project
public class ExploringCamera extends Camera {
	public ExploringCamera(Rectangle world, int w, int h) {
		super(world, w, h);
	}
	
	public ExploringCamera(Rectangle world, int screenX, int screenY, int w, int h) {
		super(world, screenX, screenY, w, h);
	}
	
	public ExploringCamera(Rectangle world, int screenX, int screenY, int w, int h, double scale) {
		super(world, screenX, screenX, screenY, w, h);
	}

	// This is the camera in Tank Wars
	@Override
	public void update() {
		if (trackingObject == null) {
			return;
		}
		int gap = 150;
		Position objectPosition = trackingObject.getPosition();
		
		// If the object x is on the far left of the camera
		// , updates the camera x to the left 
		// so that the object is within
		// the camera
		if (objectPosition.x < x + gap) {
			x = objectPosition.x - gap - 2;
			if (x < world.x) {
				x = world.x;
			}
		}
		
		// If the object x is on the far right of the camera 
		if (objectPosition.x > x + w - gap) {
			x = objectPosition.x - w + gap + 2;
			if (x + w > world.x + world.width) {
				x = world.x + world.width - w;
			}
		}
		
		// If the object y is on the far top of the camera,
		// updates the camera y upwards so that the object is within
		// the camera
		if (objectPosition.y < y + gap) {
			y = objectPosition.y - gap - 2;
			if (y < world.y) {
				y = world.y;
			}
		}
		
		// If the object y is on the far bottom of the camera
		if (objectPosition.y > y + h - gap) {
			y = objectPosition.y - h + gap + 2;
			if (y + h > world.y + world.height) {
				y = world.y + world.height - h;
			}
		}
	}
}
