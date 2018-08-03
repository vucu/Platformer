package platformer.services;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import platformer.gameobject.properties.*;
import platformer.maincomponents.*;
import platformer.services.delegates.Remover;

//Manage collisions and raise collision events
public class CollisionService {
	private final Remover remover;

	private List<ICollider> colliders;

	public CollisionService(Remover remover) {
		this.remover = remover;

		colliders = new CopyOnWriteArrayList<>();
	}

	// *** Interface for GameObject ***
	public void register(ICollider collider) {
		colliders.add(collider);
	}
	
	public <T extends ICollider> boolean checkCollisionAtPoint(Class<T> type, Point2D p) {
		for (ICollider c : colliders) {
			if (type.isAssignableFrom(c.getClass())) {
				Shape mask = c.getCollisionMask(c.getPosition());
				if (mask.contains(p))
					return true;
			}
		}

		return false;
	}
	
	public <T extends ICollider> boolean checkCollisionWith(
			ICollider me,
			int xDisplacement, 
			int yDisplacement,
			Class<T> otherType) {
		for (ICollider other : colliders) {
			if (other != me && otherType.isAssignableFrom(other.getClass())) {
				Shape myMask = me.getCollisionMask(me.getPosition().getTranslation(xDisplacement, yDisplacement));
				Shape otherMask = other.getCollisionMask(other.getPosition());
				
				if (this.testIntersectionEfficient(myMask, otherMask)) {
					return true;
				}
			}
		}
		
		return false;
	}

	// *** Interface for updater ***
	public void update() {
		// Remove objects if they are destroyed
		remover.removeIfDestroyed(colliders);

		// Check collisions between colliders
		for (ICollider me : colliders) {
			for (ICollider other : colliders) {
				if (me != other) {
					Shape myShape = me.getCollisionMask(me.getPosition());
					Shape otherShape = other.getCollisionMask(other.getPosition());
					
					boolean intersect = this.testIntersectionEfficient(myShape, otherShape);
					
					if (intersect) {
						me.onCollision(other);
					}
				}
			}
		}
	}

	private boolean testIntersectionEfficient(Shape shapeA, Shape shapeB) {
		if (shapeA instanceof Rectangle2D) {
			return shapeB.intersects((Rectangle2D) shapeA);
		}
		else if (shapeB instanceof Rectangle2D) {
			return shapeA.intersects((Rectangle2D) shapeB);
		}
		else {
			return testIntersection(shapeA, shapeB);
		}
	}
	
	private boolean testIntersection(Shape shapeA, Shape shapeB) {
		Area areaA = new Area(shapeA);
		areaA.intersect(new Area(shapeB));
		return !areaA.isEmpty();
	}
}
