package copy;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class CopyTest1 {
	public static void main(String[] args) throws Exception {
		new CopyTest1();
	}

	public CopyTest1() throws Exception {

		String savesDir = "C:/Users/Manuel/.litwrl/games/LitWR.Basic/saves";

		Path src = FileSystems.getDefault().getPath(savesDir);
		System.out.println(src.getFileName());
		Path tar = FileSystems.getDefault().getPath("logs", "access.log");

		Files.copy(src, tar);
	}
}
