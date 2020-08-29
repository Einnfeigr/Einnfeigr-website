package unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.JsonSyntaxException;

import main.img.ImageData;
import main.img.ImageDataDao;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ImageDataDaoTest {

	private final static Logger logger = 
			LoggerFactory.getLogger(ImageDataDaoTest.class);
	
	@Autowired
	ImageDataDao dao;
	
	@Test
	public void getFilesTest() throws JsonSyntaxException, IOException {
		List<ImageData> dataList = dao.getAll();
		logger.info("Obtained "+dataList.size()+" files");
		logger.info("1: "+dataList.get(0).getId());
		logger.info("2: "+dataList.get(1).getId());
		assertThat(dataList.get(0));
		assertThat(dataList.get(1));
	}
	
}
