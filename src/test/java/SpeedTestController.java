
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import main.SecurityController;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;
import main.page.PageController;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers= {PageController.class, SecurityController.class})
@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
public class SpeedTestController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(SpeedTestController.class);
	
	private static final String MOBILE_USER_AGENT = 
			"Mozilla/5.0 (Linux; Android 7.1; Mi A1 Build/N2G47H)";
	
	@Autowired
	PageController pageController;
	
	@Autowired
	SecurityController securityController;
	
	@Autowired
    private MockMvc mvc;
	
	public void speedTest(String url, int count) {
		long startTime = System.nanoTime();
		List<Integer> timeList = new ArrayList<>();
		for(int x = 0; x < count; x++) {
	    	long time = System.nanoTime();
	    	try {
				mvc.perform(MockMvcRequestBuilders
						.get(url)
						.header("user-agent", MOBILE_USER_AGENT)
						.accept(MediaType.ALL))
						.andExpect(MockMvcResultMatchers.status().isOk());
	    	} catch(Exception e) {
	    		
	    	}
	    	timeList.add((int) ((System.nanoTime()-time)/(1000000)));
		}
		long mean = 0;
		for(int i : timeList) {
			mean += i;
			logger.info(String.valueOf(i));
		}
		mean = mean/timeList.size();
		logger.info("mean: "+mean);
		logger.info("total: "+(System.nanoTime()-startTime)/1000000);
	}
	
	@Test
	public void speedTestMain() {
		speedTest("/", 128);
	}
	
	@Test
	public void speedTestPortfolio() {
		speedTest("/portfolio", 2048);
	}
	
	@Test
	public void speedTestRetouch() {
		speedTest("/retouch", 1024);
	}
	
	@Test
	public void speedTestAbout() {
		speedTest("/about", 1024);
	}
	
	@Test
	public void speedTestNotFound() {
		speedTest("/a", 1024);
	}
}
