import java.awt.Point;
import java.io.File;

public class RegInf {
	String name;

	int rX, rY, bX, bY;

	Point p;

	File f;

	WpInf minw;

	int mind;

	public RegInf(File f, int rX, int rY) {
		this.f = f;
		this.rX = rX;
		this.rY = rY;
		this.bX = rX * 511;
		this.bY = rY * 511;
		this.p = new Point(bX, bY);
	}

	public boolean del() {
		return f.delete();
	}

	@Override
	public String toString() {
		return f.getName() + "\t" + rX + "/" + rY + "\t" + bX + "/" + bY;
	}
}
