package platformer.gameobject.level2;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.Ground;

public class Ground2 extends Ground {
	private final int x;
	private final int y;
	private final int w;
	
	public Ground2(Services services, int x, int y, int w) {
		super(services);
		
		this.x = x;
		this.y = y;
		this.w = w;
	}

	@Override
	public Position getPosition() {
		return new Position(this.x, this.y);
	}

	@Override
	public int getWidth() {
		return this.w;
	}

	@Override
	public int getHeight() {
		return 25;
	}

}
