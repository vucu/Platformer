package platformer.gameobject.level1;

import platformer.builder.Services;
import platformer.gameobject.Background;

public class Background1 extends Background {
	
	public Background1(Services services) {
		super(services);
	}

	@Override
	protected String getBackgroundFileName() {
		return "background1.png";
	}
}
