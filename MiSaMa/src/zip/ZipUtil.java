package zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class ZipUtil {

	public static void main(String[] args) {
		ZipUtil u = new ZipUtil();
		u.zipDir(new File("D:\\aman"));
		u.unZipIt("D:\\aman.zip");
	}

	public void unZipIt(String zipFile) {

		try {
			File folder = new File("D:\\unzipFolder");
			if (!folder.exists()) {
				folder.mkdir();
			}
			try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
				ZipEntry ze = zis.getNextEntry();
				while (ze != null) {
					String fileName = ze.getName();
					File newFile = new File(folder.getAbsolutePath() + File.separator + fileName);
					System.out.println("file unzip : " + newFile.getAbsoluteFile());
					new File(newFile.getParent()).mkdirs();
					try (FileOutputStream fos = new FileOutputStream(newFile)) {
						IOUtils.copy(zis, fos);
					}
					ze = zis.getNextEntry();
				}
				zis.closeEntry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void zipDir(File dir) {

		Path pathBase = Paths.get(dir.getAbsolutePath());
		String zipAbsolutePath = pathBase + ".zip";
		List<String> files = getFileNames(dir);

		System.out.println(files.size());

		try (FileOutputStream fos = new FileOutputStream(zipAbsolutePath); //
				ZipOutputStream zos = new ZipOutputStream(fos)) {

			for (String file : files) {

				try (FileInputStream fis = new FileInputStream(file)) {
					zos.putNextEntry(new ZipEntry(pathBase.relativize(Paths.get(file)).toString()));
					IOUtils.copy(fis, zos);
					zos.closeEntry();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> getFileNames(File dir) {
		List<String> names = new ArrayList<>();
		collect(names, dir);
		return names;
	}

	private void collect(List<String> names, File f) {

		if (f.isDirectory()) {
			for (File x : f.listFiles()) {
				collect(names, x);
			}
		} else {
			names.add(f.getAbsolutePath());
		}
	}

}
