import java.awt.Point;
import java.io.File;

public class WpInf {
	String n;

	int rX, rY, bX, bY;

	File f;

	Point p;

	public WpInf(File f, int bX, int bY) {
		this.f = f;
		this.n = f.getName().split("_{1,2}")[0];
		this.rX = bX / 511;
		this.rY = bY / 511;
		this.bX = bX;
		this.bY = bY;
		this.p = new Point(bX, bY);
	}

	@Override
	public String toString() {
		return n + "\t" + rX + "/" + rY + "\t" + bX + "/" + bY;
	}
}
