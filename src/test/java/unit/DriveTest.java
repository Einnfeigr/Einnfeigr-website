package unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

import main.ProjectConfiguration;
import main.drive.DriveDao;
import main.drive.DriveUtils;
import main.http.Request;
import main.http.RequestBuilder;
import main.img.ImageData;
import main.img.ImageUtils;
import main.security.SecurityConfiguration;

@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DriveTest {

	private final static Logger logger = 
			LoggerFactory.getLogger(DriveTest.class);
	
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
	
	@Test 
	public void exchangeToken() throws IOException {
		String code = "";
		RequestBuilder builder = new RequestBuilder();
		Map<String, String> content = new HashMap<>();
		content.put("client_id", "");
		content.put("client_secret", "");
		content.put("redirect_uri", "https://einnfeigr-website.herokuapp.com/login");
		content.put("code", code+"&");
		content.put("grant_type", "authorization_code");
		Request request = builder.post("https://oauth2.googleapis.com/token")
				.content(new Gson().toJson(content))
				.build();
		logger.info(request.perform().getContent());
	}
	
	@Test
	public void startWatch() throws IOException {
		RequestBuilder builder = new RequestBuilder();
		Map<String, String> content = new HashMap<>();
		content.put("kind", "api#channel");
		content.put("id", System.getenv("drive.channelId"));
		content.put("type", "webhook");
		content.put("address", 
				"https://einnfeigr-website.herokuapp.com/api/drive/callback");
		Request request = builder.post(
					"https://www.googleapis.com/drive/v3/files/"
					+DriveUtils.rootId+"/watch/")
				.content(new Gson().toJson(content))
				.authorization("Bearer")
				.contentType("application/json")
				.build();
		logger.info(request.perform().getContent());
	}
}
