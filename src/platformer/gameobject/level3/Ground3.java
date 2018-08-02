package platformer.gameobject.level3;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.Ground;

public class Ground3 extends Ground {
	private final int x;
	private final int y;
	private final int w;
	private final int h;
	
	public Ground3(Services services, int x, int y, int w, int h) {
		super(services);
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	@Override
	public Position getPosition() {
		return new Position(this.x,this.y);
	}

	@Override
	public int getWidth() {
		return this.w;
	}

	@Override
	public int getHeight() {
		return this.h;
	}

}
