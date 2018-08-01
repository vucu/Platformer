package platformer.gameobject.level2;

import platformer.builder.Services;
import platformer.gameobject.Background;

public class Background2 extends Background {
	
	public Background2(Services services) {
		super(services);
	}

	@Override
	protected String getBackgroundFileName() {
		return "background2.png";
	}
}