package unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.JsonSyntaxException;

import main.img.ImageDataDao;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ImageDataDaoTest {

	@Autowired
	ImageDataDao dao;
	
	@Test
	public void getFilesTest() throws JsonSyntaxException, IOException {
		assertThat(dao.getAll().size() == 2);
	}
	
}
