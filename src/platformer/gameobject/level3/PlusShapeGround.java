package platformer.gameobject.level3;

import platformer.builder.Services;
import platformer.gameobject.GameObject;

// A plus shape ground is simply 2 grounds,
// one vertical and one horizontal
public class PlusShapeGround extends GameObject {
	
	public PlusShapeGround(Services services, int x, int y, int size) {
		// Make the horizontal ground
		new Ground3(services, x - size/2, y, size, 25);
		
		// Make the vertical ground
		new Ground3(services, x, y - size/2, 25, size);
		
		// Done, destroy this object
		this.destroy();
	}
}
