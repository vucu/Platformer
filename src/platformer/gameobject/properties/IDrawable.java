package platformer.gameobject.properties;

import java.awt.Graphics;

public interface IDrawable {
	public int getDepth();

	public void onDraw(Graphics g);
}
