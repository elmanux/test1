package copy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

public class CopyUtil {

	public static String BACKUP_DIR = "backup";

	private static SimpleDateFormat s = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");

	public File getBackupDir(File worldDir) {
		String n = worldDir.getName();
		String p = worldDir.getParent();
		String t = s.format(new Date());
		return new File(p + "/" + BACKUP_DIR + "/" + n + "_" + t);
	}

	public void backup(File worldDir) {

		try {
			FileUtils.copyDirectory(worldDir, getBackupDir(worldDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restore(File backupDir, File worldDir) {

		try {
			Map<File, Long> existingBackups = getBackups(worldDir);
			if (existingBackups.values().contains(getChecksum(backupDir))) {
				System.out.println("Y");
			} else {
				System.out.println("N");
			}

			FileUtils.copyDirectory(worldDir, getBackupDir(worldDir));
			FileUtils.deleteDirectory(worldDir);
			FileUtils.copyDirectory(backupDir, worldDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Map<File, Long> getBackups(File dirWorld) {
		File backupDir = new File(dirWorld.getParent() + "/" + CopyUtil.BACKUP_DIR);
		if (backupDir.exists() == false) {
			backupDir.mkdir();
		}
		Map<File, Long> map = new TreeMap<>();
		for (File f : backupDir.listFiles()) {
			if (f.isDirectory() && f.getName().startsWith(dirWorld.getName())) {
				System.out.println(f.getAbsolutePath());
				map.put(f, getChecksum(f));
			}
		}
		return map;
	}

	public long getChecksum(File dir) {
		long l = 0;
		try {
			for (File f : dir.listFiles()) {
				if (f.isDirectory()) {
					// l += getChecksum(f);
				} else {
					l += FileUtils.checksumCRC32(f);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}
}
