package platformer.gameobject.level1;

import java.awt.Rectangle;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.Ground;

public class Ground1 extends Ground {
	private final Rectangle world;
	private final int width;
	private final int x;
	
	public Ground1(Services services, int x, int width) {
		super(services);
		
		this.world = services.world;
		this.width = width;
		this.x = x;
	}
	
	@Override
	public Position getPosition() {
		int y = world.height - 50;
		return new Position(this.x, y);
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return 50;
	}
	
	
}
