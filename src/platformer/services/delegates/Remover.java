package platformer.services.delegates;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import platformer.gameobject.properties.IDestructible;

// Remove objects that already been destroyed
public class Remover {
	public void removeIfDestroyed(Collection<?> collection) {
		Set<Object> toBeRemoved = new HashSet<>();

		for (Object object : collection) {
			if (object instanceof IDestructible) {
				IDestructible destructible = (IDestructible) object;
				if (destructible.isDestroyed()) {
					toBeRemoved.add(object);
				}
			}
		}

		collection.removeAll(toBeRemoved);
	}

}
