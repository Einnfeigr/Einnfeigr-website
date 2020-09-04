package unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.drive.DriveDao;
import main.drive.DriveUtils;
import main.img.ImageData;
import main.img.ImageUtils;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DriveTest {

	private final static Logger logger = 
			LoggerFactory.getLogger(DriveDaoTest.class);
	
	@Autowired
	DriveDao dao;
	
	@Test
	public void createImagePreviewTest() throws IOException {
		for(ImageData data : dao.getAllFiles()) {
			BufferedImage image = ImageIO.read(new URL(
					DriveUtils.getDownloadUrl(data.getId())));
			logger.info("width: "+image.getWidth()
					+" | height: "+image.getHeight());
			image = ImageUtils.resizeBySmaller(image, 100);
			logger.info("width: "+image.getWidth()
					+" | height: "+image.getHeight());
			assertThat(image.getWidth() > 0 && image.getHeight() > 0);
		}
	}
}
