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
	private final Remover remover;

	private List<ICollider> colliders;

	public CollisionService(Remover remover) {
		this.remover = remover;

		colliders = new ArrayList<>();
	}

	// *** Interface for GameObject ***
	public void register(ICollider collider) {
		colliders.add(collider);
	}
	
	public <T extends ICollider> boolean checkCollisionAtPoint(Class<T> type, Point2D p) {
		for (ICollider c : colliders) {
			if (type.isAssignableFrom(c.getClass())) {
				Shape mask = c.getCollisionMask();
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
		// Note: It only works with rectangles
		if (!(me.getCollisionMask() instanceof Rectangle2D)) {
			throw new RuntimeException("Only works with Rectangle");
		}
			
		for (ICollider c : colliders) {
			if (c != me && otherType.isAssignableFrom(c.getClass())) {
				Shape myMask = me.getCollisionMask();
				Shape otherMask = c.getCollisionMask();
				
				if (myMask instanceof Rectangle2D) {
					Rectangle2D rect = (Rectangle) myMask;
					rect.setRect(rect.getX()+xDisplacement, rect.getY()+yDisplacement, rect.getWidth(), rect.getHeight());
					if (otherMask.intersects(rect)) {
						return true;
					}
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
					Shape myShape = me.getCollisionMask();
					Shape otherShape = other.getCollisionMask();
					
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
