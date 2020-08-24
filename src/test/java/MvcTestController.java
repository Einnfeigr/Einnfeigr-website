
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import main.PageController;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@RunWith(SpringRunner.class)
@WebMvcTest(PageController.class)
@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
public class MvcTestController {

	@Autowired
	PageController pageController;
	
	@Autowired
    private MockMvc mvc;
	
	@Test
	public void testPageController() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/")
				.header("user-agent", "Mozilla/5.0 (Linux; Android 7.1; Mi A1 Build/N2G47H")
				.accept(MediaType.ALL))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
}
