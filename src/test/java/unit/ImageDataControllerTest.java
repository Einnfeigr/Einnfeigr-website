package unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.img.ImageData;
import main.img.ImageDataComparator;
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
		Comparator<ImageData> dataComparator = new ImageDataComparator();
		logger.debug("1:"+dataComparator.compare(data1, data2));
		logger.debug("2:"+dataComparator.compare(data3, data4));
		logger.debug("3:"+dataComparator.compare(data5, data6));
		assertThat(dataComparator.compare(data1, data2) == 1 
				&& dataComparator.compare(data3, data4) == -1
				&& dataComparator.compare(data5, data6) == 0);
	}
	
	@Test
	public void testImageData() {
		assertThat(dataController.getLatestImages().size() > 0);
	}
	
}
