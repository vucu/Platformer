package platformer.gameobject;

import platformer.gameobject.properties.IDestructible;

public abstract class GameObject implements IDestructible {
	private boolean destroyed = false;
	
	public boolean isDestroyed() {
		return this.destroyed;
	};

	public void destroy() {
		this.destroyed = true;
	}
}
