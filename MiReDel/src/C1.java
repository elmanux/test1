import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class C1 {
	Set<RegInf> regs;
	Set<WpInf> wps;

	public C1() {
		regs = getRegs();
		wps = getWps();
	}

	private Set<RegInf> getRegs() {
		String dirp = "c:\\Users\\elman\\AppData\\Roaming\\.technic\\modpacks\\minecraft-after-humans\\saves\\world\\region\\";
		File dir = new File(dirp);
		File[] files = dir.listFiles();
		Set<RegInf> regs = new HashSet<>();
		for (File f : files) {
			String n = f.getName();
			String[] s = n.split("\\.");
			int x = Integer.valueOf(s[1]);
			int y = Integer.valueOf(s[2]);
			regs.add(new RegInf(f, x, y));
		}
		return regs;
	}

	private Set<WpInf> getWps() {

		String dirp = "C:\\Users\\elman\\AppData\\Roaming\\.technic\\modpacks\\minecraft-after-humans\\journeymap\\data\\sp\\world\\waypoints";
		File dir = new File(dirp);
		File[] files = dir.listFiles();
		Set<WpInf> wps = new HashSet<>();
		for (File f : files) {
			String n = f.getName();
			if (!n.startsWith("N#")) {
				String[] s = n.split("_{1,2}")[1].split("\\.")[0].split(",");
				int x = Integer.valueOf(s[0]);
				int y = Integer.valueOf(s[2]);
				wps.add(new WpInf(f, x, y));
			}
		}
		return wps;
	}

}
