package platformer.services;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import platformer.gameobject.properties.*;
import platformer.maincomponents.*;
import platformer.services.delegates.Remover;

//Manage collisions and raise collision events
public class CollisionService {
	public boolean checkCollisionAtPoint(Point2D p, Class type) {
		for (ICollider c : colliders) {
			if (type.isAssignableFrom(c.getClass())) {
				Shape mask = c.getCollisionMask();
				if (mask.contains(p))
					return true;
			}
		}

		return false;
	}

	private final Remover remover;

	private List<ICollider> colliders;

	public CollisionService(Remover remover) {
		this.remover = remover;

		colliders = new ArrayList<>();
	}

	public void register(ICollider collider) {
		colliders.add(collider);
	}

	public void update() {
		// Remove objects if they are destroyed
		remover.removeIfDestroyed(colliders);

		// Check collisions between colliders
		for (ICollider me : colliders) {
			for (ICollider other : colliders) {
				if (me != other) {
					Shape myShape = me.getCollisionMask();
					Shape otherShape = other.getCollisionMask();
					
					boolean intersect = false;
					if (myShape instanceof Rectangle2D) {
						intersect = otherShape.intersects((Rectangle2D) myShape);
					}
					else if (otherShape instanceof Rectangle2D) {
						intersect = myShape.intersects((Rectangle2D) otherShape);
					}
					else {
						intersect = testIntersection(myShape, otherShape);
					}
					
					if (intersect) {
						me.onCollision(other);
					}
				}
			}
		}
	}

	private boolean testIntersection(Shape shapeA, Shape shapeB) {
		Area areaA = new Area(shapeA);
		areaA.intersect(new Area(shapeB));
		return !areaA.isEmpty();
	}
}
