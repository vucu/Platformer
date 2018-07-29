package platformer.datastructures;

public class Vector {
	public final int angle;
	public final double magnitude;

	public Vector() {
		this(0, 0);
	}

	public Vector(int angle, double magnitude) {
		this.angle = angle;
		this.magnitude = magnitude;
	}

	public Vector(double x, double y, double magnitude) {
		double rad = Math.atan2(y, x);
		double deg = Math.toDegrees(rad);
		int d = (int) deg;
		if (d < 0)
			d += 360;
		d = d % 360;
		
		this.angle = d;
		this.magnitude = magnitude;
	}

	public Vector(Position src, Position dest) {
		this(dest.x - src.x, dest.y - src.y, 1);
	}

	public double getX() {
		double radians = Math.toRadians(angle);
		return magnitude * Math.cos(radians);
	}

	public double getY() {
		double radians = Math.toRadians(angle);
		return magnitude * Math.sin(radians);
	}

	public Vector getNewVectorWithRotation(int degree) {
		int newAngle = angle + degree;
		if (newAngle < 0)
			newAngle += 360;
		newAngle = newAngle % 360;
		return new Vector(newAngle, this.magnitude);
	}

	public Vector getNewVectorWithLength(int magnitude) {
		return new Vector(this.angle, magnitude);
	}

	@Override
	public String toString() {
		return "(" + angle + "," + magnitude + ") (" + getX() + "," + getY() + ")";
	}
}
