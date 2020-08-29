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

import main.drive.DriveDao;
import main.img.ImageData;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;
import main.section.Section;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DriveDaoTest {

	private final static Logger logger = 
			LoggerFactory.getLogger(DriveDaoTest.class);
	
	@Autowired
	DriveDao dao;
	
	@Test
	public void getAllFilesTest() throws IOException {
		List<ImageData> dataList = dao.getAllFiles();
		logger.info("Obtained "+dataList.size()+" files");
		logger.info("1: "+dataList.get(0).getId());
		logger.info("2: "+dataList.get(1).getId());
		assertThat(dataList.get(0));
		assertThat(dataList.get(1));
	}
	
	@Test
	public void getAllFoldersTest() throws IOException {
		List<Section> sections = dao.getAllFolders();

		logger.info("obtained "+sections.size()+" folders");
		for(Section cSection : sections) {
			logger.info(cSection.getId());
		}
		assertThat(sections.size() > 1 
				&& sections.get(0).getSections().size() > 0 
				|| sections.get(1).getSections().size() > 0);
	}
	
}
