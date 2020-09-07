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

import main.album.Album;
import main.album.AlbumController;
import main.img.ImageData;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AlbumControllerTest {

	private final static Logger logger = 
			LoggerFactory.getLogger(AlbumControllerTest.class);
	
	@Autowired
	AlbumController albumController;
	
	@Test
	public void testImageData() {
		Map<String, Album> albums = albumController.getAllAlbums();
		for(Entry<String, Album> entry : albums.entrySet()) {
			Album album = entry.getValue();
			logger.info("=============================================");
			logger.info("album: "+album.getId()+" | "+album.getName());
			logger.info("images: ");
			for(ImageData image : album.getImages()) {
				logger.info(image.getId());
			}
			logger.info("albums: ");
			for(Album cAlbum : album.getAlbums()) {
				logger.info(cAlbum.getId()+" | "+cAlbum.getName());
			}
			assertThat(album.getImages().size() > 0);
		}
	}
	
}
