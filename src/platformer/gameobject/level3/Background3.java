package platformer.gameobject.level3;

import platformer.builder.Services;
import platformer.gameobject.Background;

public class Background3 extends Background {

	public Background3(Services services) {
		super(services);
	}

	@Override
	protected String getBackgroundFileName() {
		return "background3.png";
	}

}
