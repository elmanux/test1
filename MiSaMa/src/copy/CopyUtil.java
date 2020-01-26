package copy;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

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
		long l = -1;
		if (dir != null && dir.exists()) {
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
		}
		return l;
	}

	public String getBackupSizeInGb(File dirWorld) {
		if (null == dirWorld) {
			return null;
		}
		Map<File, Long> backups = getBackups(dirWorld);
		long sizebyte = backups.keySet().stream().mapToLong(f -> size(Paths.get(f.getAbsolutePath()))).sum();
		return humanReadableByteCount(sizebyte, false);
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static long size(Path path) {

		final AtomicLong size = new AtomicLong(0);

		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

					size.addAndGet(attrs.size());
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {

					System.out.println("skipped: " + file + " (" + exc + ")");
					// Skip folders that can't be traversed
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

					if (exc != null)
						System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
					// Ignore errors traversing a folder
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
		}

		return size.get();
	}
}
