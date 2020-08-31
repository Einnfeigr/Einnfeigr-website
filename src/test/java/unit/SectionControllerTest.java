package unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.PageController;
import main.img.ImageData;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;
import main.section.Section;
import main.section.SectionController;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class SectionControllerTest {

	private final static Logger logger = 
			LoggerFactory.getLogger(SectionControllerTest.class);
	
	@Autowired
	SectionController sectionController;
	
	@Test
	public void testImageData() {
		Map<String, Section> sections = sectionController.getSections();
		for(Entry<String, Section> entry : sections.entrySet()) {
			Section section = entry.getValue();
			logger.info("=============================================");
			logger.info("section: "+section.getId()+" | "+section.getName());
			logger.info("images: ");
			for(ImageData image : section.getImages()) {
				logger.info(image.getId());
			}
			logger.info("sections: ");
			for(Section cSection : section.getSections()) {
				logger.info(cSection.getId()+" | "+cSection.getName());
			}
			assertThat(section.getImages().size() > 0);
		}
	}
	
}
