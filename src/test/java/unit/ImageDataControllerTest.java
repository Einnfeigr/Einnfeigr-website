package unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.img.ImageData;
import main.img.ImageDataController;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ImageDataControllerTest {

	private final static Logger logger = 
			LoggerFactory.getLogger(ImageDataControllerTest.class);
	
	@Autowired
	ImageDataController dataController;
	
	@Test 
	public void testImageDataComprasion() {
		ImageData data1 = new ImageData();
		ImageData data2 = new ImageData();
		data1.setTitle("abc");
		data2.setTitle("abd");
		ImageData data3 = new ImageData();	
		ImageData data4 = new ImageData();
		data3.setTitle("abc");
		data4.setTitle("abcd");
		ImageData data5 = new ImageData();
		ImageData data6 = new ImageData();
		data5.setTitle("abc");
		data6.setTitle("abc");
		logger.debug("1:"+data1.compareTo(data2));
		logger.debug("2:"+data3.compareTo(data4));
		logger.debug("3:"+data5.compareTo(data6));
		assertThat(data1.compareTo(data2) == 1 
				&& data3.compareTo(data4) == -1
				&& data5.compareTo(data6) == 0);
	}
	
	@Test
	public void testImageData() {
		assertThat(dataController.getLatestImages().size() > 0);
	}
	
}
