package unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.img.ImageDataController;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ImageDataControllerTest {

	@Autowired
	ImageDataController dataController;
	
	@Test
	public void testImageData() {
		assertThat(dataController.getLatestImages().size() > 0);
	}
	
}
