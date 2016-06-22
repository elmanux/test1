package testdata;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import data.Doc;

public class Testdata1 {
	public static void main(String[] args) throws Exception {
		Testdata1 t = new Testdata1();

		Set<Doc> write = t.write(10, 10);
		print(write);

		Set<Doc> read = t.read();
		print(read);

	}

	private File baseDir;

	private Random rand;

	public Testdata1() {
		baseDir = new File("D:/docOrgTest");
		rand = new Random();
	}

	private static void print(Set<Doc> docs) {
		System.out.println("################################");
		for (Doc d : docs) {
			System.out.println(d);
		}
	}

	private Set<Doc> write(int cntDocs, int cntTags) throws Exception {
		if (baseDir.exists()) {
			Process exec = Runtime.getRuntime().exec(
					"cmd /c rmdir /s /q " + baseDir.getAbsolutePath());
			exec.waitFor();
		}
		baseDir.mkdirs();
		Set<Doc> docs = new HashSet<>();

		for (int d = 0; d < cntDocs; d++) {

			String n = String.valueOf(rand.nextDouble()).substring(2, 15);
			File docFolder = new File(baseDir.getAbsolutePath() + "/" + n);
			File docDoc = new File(docFolder.getAbsoluteFile() + "/doc.txt");
			Doc doc = new Doc();
			doc.dir = docFolder;
			doc.doc = docDoc;
			while (doc.tags.size() < rand.nextInt(cntTags)) {
				int nt = rand.nextInt(cntTags);
				if (!doc.tags.contains("#" + nt)) {
					doc.tags.add("#" + nt);
				}
			}
			doc.write();
			docs.add(doc);
		}
		return docs;
	}

	private Set<Doc> read() {

		FileFilter ffDoc = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().equals("doc.txt");
			}
		};

		Set<Doc> docs = new HashSet<>();

		for (File f : baseDir.listFiles()) {
			if (f.isDirectory()) {
				File[] d = f.listFiles(ffDoc);
				if (d.length == 1) {
					Doc doc = new Doc();
					doc.dir = f;
					doc.doc = d[0];
					doc.read();
					docs.add(doc);
				}
			}
		}
		return docs;
	}
}
