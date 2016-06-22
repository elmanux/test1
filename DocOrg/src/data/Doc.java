package data;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Doc {
	public File dir;

	public File doc;

	public Set<String> tags;

	public Doc() {
		// tags = new ArrayList<>();
		tags = new HashSet<String>();
	}

	private String LS = System.getProperty("line.separator");

	public void write() {
		try {
			writeInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeInt() throws Exception {
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!doc.exists()) {
			doc.createNewFile();
		}
		RandomAccessFile raf = new RandomAccessFile(doc, "rw");
		for (String t : tags) {
			raf.writeBytes(t + LS);
		}
		raf.close();
	}

	public void read() {
		try {
			readInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readInt() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(doc, "rw");
		while (raf.getFilePointer() < raf.length()) {
			tags.add(raf.readLine());
		}
		raf.close();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "\t" + dir.getName() + "\t"
				+ doc.getName() + "\t\t"
				+ StringUtils.join(tags.iterator(), " / ");
	}
}
