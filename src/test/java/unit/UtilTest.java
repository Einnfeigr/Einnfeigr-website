package unit;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.img.ImageUtils;
import main.misc.Util;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
public class UtilTest {

	private final static Logger logger = 
			LoggerFactory.getLogger(UtilTest.class);
	
	@Test
	public void testResize() throws IOException {
		BufferedImage image;
		BufferedImage resized;
		image = new BufferedImage(200, 400, BufferedImage.TYPE_INT_RGB);
		//resizeByLarger tesing
		resized = ImageUtils.resizeByLarger(image, 200);
		assertThat(resized.getHeight() == 200 && resized.getWidth() == 100);
		resized = ImageUtils.resizeByLarger(image, 800);
		assertThat(resized.getHeight() == 800 && resized.getWidth() == 400);
		resized = ImageUtils.resizeByLarger(image, 350);
		assertThat(resized.getHeight() == 350 && resized.getWidth() == 175);
		//resizeBySmaller testing
		resized = ImageUtils.resizeBySmaller(image, 400);
		assertThat(resized.getHeight() == 800 && resized.getWidth() == 400);
		resized = ImageUtils.resizeBySmaller(image, 100);
		assertThat(resized.getHeight() == 200 && resized.getWidth() == 100);
		resized = ImageUtils.resizeBySmaller(image, 150);
		assertThat(resized.getHeight() == 300 && resized.getWidth() == 150);
		//resize testing
		resized = ImageUtils.resize(image, 250, 500);
		assertThat(resized.getWidth() == 250 && resized.getHeight() == 500);
		image = new BufferedImage(400, 200, BufferedImage.TYPE_INT_RGB);
		//resizeByLarger tesing
		resized = ImageUtils.resizeByLarger(image, 200);
		assertThat(resized.getHeight() == 100 && resized.getWidth() == 200);
		resized = ImageUtils.resizeByLarger(image, 800);
		assertThat(resized.getHeight() == 400 && resized.getWidth() == 800);
		resized = ImageUtils.resizeByLarger(image, 350);
		assertThat(resized.getHeight() == 175 && resized.getWidth() == 350);
		//resizeBySmaller testing
		resized = ImageUtils.resizeBySmaller(image, 400);
		assertThat(resized.getHeight() == 400 && resized.getWidth() == 800);
		resized = ImageUtils.resizeBySmaller(image, 100);
		assertThat(resized.getHeight() == 100 && resized.getWidth() == 200);
		resized = ImageUtils.resizeBySmaller(image, 150);
		assertThat(resized.getHeight() == 150 && resized.getWidth() == 300);
		//resize testing
		resized = ImageUtils.resize(image, 250, 500);
		assertThat(resized.getWidth() == 250 && resized.getHeight() == 500);
		image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		//resizeByLarger tesing
		resized = ImageUtils.resizeByLarger(image, 200);
		assertThat(resized.getHeight() == 200 && resized.getWidth() == 200);
		resized = ImageUtils.resizeByLarger(image, 800);
		assertThat(resized.getHeight() == 800 && resized.getWidth() == 800);
		resized = ImageUtils.resizeByLarger(image, 350);
		assertThat(resized.getHeight() == 350 && resized.getWidth() == 350);
		//resizeBySmaller testing
		resized = ImageUtils.resizeBySmaller(image, 400);
		assertThat(resized.getHeight() == 400 && resized.getWidth() == 400);
		resized = ImageUtils.resizeBySmaller(image, 100);
		assertThat(resized.getHeight() == 100 && resized.getWidth() == 100);
		resized = ImageUtils.resizeBySmaller(image, 150);
		assertThat(resized.getHeight() == 150 && resized.getWidth() == 150);
		//resize testing
		resized = ImageUtils.resize(image, 250, 500);
		assertThat(resized.getWidth() == 250 && resized.getHeight() == 500);		
	}
	
	@Test
	public void testIsAbsolute() throws FileNotFoundException {
		assertThat(!Util.isAbsolute("test"));
		assertThat(Util.isAbsolute(Util.toAbsoluteUrl("test")));
		assertThat(!Util.isAbsolute("test.jpg"));
		assertThat(Util.isAbsolute(Util.toAbsoluteUrl("test.jpg")));
		assertThat(!Util.isAbsolute(new File("test")));
		assertThat(Util.isAbsolute(new File(Util.toAbsoluteUrl("test"))));
		assertThat(!Util.isAbsolute(new File("test.jpg")));
		assertThat(Util.isAbsolute(new File(Util.toAbsoluteUrl("test.jpg"))));		
	}
	
	@Test
	public void testCreateFile() {
		File unexistingFile = new File("test/file.txt");
		File unexistingDirectory = new File("test/directory");
		File directoryWithUnexistingParent = new File(
				"test/unexisting/directory");
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
		Files.write(srcFile.toPath(), "Testing with кириллица characters"
				.getBytes());
		File destFile = new File(Util.toAbsoluteUrl(
				"static/test/copyTest.txt"));
		if(destFile.exists()) {
			destFile.delete();
		}
		Util.copyFile(srcFile, destFile);	
		assertThat(destFile.exists());
		assertThat(Files.readAllLines(srcFile.toPath())
			      .equals(Files.readAllLines(destFile.toPath())));
	}
	
}
