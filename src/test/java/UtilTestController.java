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
