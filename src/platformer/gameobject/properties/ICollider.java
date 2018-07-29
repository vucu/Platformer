package platformer.gameobject.properties;

import java.awt.*;

import platformer.datastructures.Position;

public interface ICollider extends ITransform {
	public Shape getCollisionMask(Position at);

	public void onCollision(ICollider other);
}
