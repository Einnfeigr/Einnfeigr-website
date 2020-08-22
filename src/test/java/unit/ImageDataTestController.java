package unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.img.ImageData;
import main.misc.Util;

@RunWith(SpringJUnit4ClassRunner.class)
public class ImageDataTestController {

	@Test
	public void testImageData() {
		ImageData data = new ImageData();
		data.setFile(new File("test"));
		assertThat(data.getFile() != null);
		assertThat(data.getPath() != null);
		assertThat(data.getName() != null);
		assertThat(data.getPath().equals(Util.toAbsoluteUrl("test")));
	}
	
}
