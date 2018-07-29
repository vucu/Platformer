package platformer.datastructures;

public class Position {
	public final int x;
	public final int y;

	public Position() {
		this(0, 0);
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Position clone() {
		return new Position(x, y);
	}

	public Position getTranslation(int tx, int ty) {
		return new Position(x + tx, y + ty);
	}

	public Position getTranslation(Vector v) {
		return new Position(x + (int) v.getX(), y + (int) v.getY());
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
