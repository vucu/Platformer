package platformer.services;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import platformer.gameobject.properties.IDrawable;
import platformer.services.delegates.Camera;
import platformer.services.delegates.Remover;

public class ScreenDrawingService {
	private final Remover remover;

	public ScreenDrawingService(Remover remover) {
		this.remover = remover;
	}

	List<IDrawable> objects = new ArrayList<>();

	public void register(IDrawable object) {
		objects.add(object);
	}

	public void draw(Graphics g) {
		objects.forEach((o) -> {
			o.onDraw(g);
		});
	}

	// Update this drawing service
	public void update() {
		// Check if a object is destroyed. If that is the case don't draw them anymore
		remover.removeIfDestroyed(objects);
	}
}
