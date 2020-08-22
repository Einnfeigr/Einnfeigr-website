package unit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.misc.Util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RunWith(SpringJUnit4ClassRunner.class)
public class UtilTestController {
	
	@Test
	public void testCreateFile() {
		File unexistingFile = new File("test/file.txt");
		File unexistingDirectory = new File("test/directory");
		File directoryWithUnexistingParent = new File("test/unexisting/directory");
		File unexistingParent = directoryWithUnexistingParent.getParentFile();
		try {
			Util.createFile(unexistingFile);
			Util.createFile(unexistingDirectory);
			Util.createFile(directoryWithUnexistingParent);
			assertThat(unexistingFile.exists());
			assertThat(unexistingDirectory.exists());
			assertThat(directoryWithUnexistingParent.exists());
		} finally {
			unexistingFile.delete();
			unexistingDirectory.delete();
			directoryWithUnexistingParent.delete();
			unexistingParent.delete();
		}
	}
	
	@Test 
	public void testIsImage() {
		File image = new File("image.jpeg");
		File directory = new File("notAnImage");
		File file = new File("file.txt");
		assertThat(Util.isImage(image));
		assertThat(!Util.isImage(directory));
		assertThat(!Util.isImage(file));
	}
	
	@Test
	public void testStringToUpperCase() {
		assertThat(Util.toUpperCase("test/url").equals("Test/Url"));
		assertThat(Util.toUpperCase("/test/url").equals("/Test/Url"));
		assertThat(Util.toUpperCase("test").equals("Test"));
	}
	
	@Test
	public void testCopy() throws IOException {
		File srcFile = Util.createFile("static/test/test.txt");
		Files.write(srcFile.toPath(), "Testing with кириллица characters".getBytes());
		File destFile = new File(Util.toAbsoluteUrl("static/test/copyTest.txt"));
		if(destFile.exists()) {
			destFile.delete();
		}
		Util.copyFile(srcFile, destFile);	
		assertThat(destFile.exists());
		assertThat(Files.readAllLines(srcFile.toPath())
			      .equals(Files.readAllLines(destFile.toPath())));
	}
	
}
