package platformer.gameobject.properties;

import java.awt.*;

public interface ICollider {
	public Shape getCollisionMask();

	public void onCollision(ICollider other);
}
