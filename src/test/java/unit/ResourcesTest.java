package unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.misc.Util;

@RunWith(SpringJUnit4ClassRunner.class)
public class ResourcesTest {

	@Test
	public void testGetFile() {
		File file;
		try {
			file = Util.getFile("test.mustache");
			assertThat(file.exists());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			file = Util.getFile("/test.mustache");
			assertThat(file.exists());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			file = Util.getFile("test");
			assertThat(file.exists());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			file = Util.getFile("/test");
			assertThat(file.exists());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
