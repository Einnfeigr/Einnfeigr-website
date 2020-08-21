
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
import org.springframework.web.context.WebApplicationContext;

import main.PageController;
import main.misc.ProjectConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=ProjectConfiguration.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = EinnfeigrWebsite.class)
@WebMvcTest(PageController.class)
public class TestController {
	
	@Autowired
	WebApplicationContext wac;

	PageController pageController;
	
	@Autowired
    private MockMvc mvc;
	
	/*
	@Before
	public void setup() {
	    mvc = MockMvcBuilders.standaloneSetup(PageController.class).build();
	}*/
	
	@Test
	public void testPageController() throws Exception { 
		mvc.perform(MockMvcRequestBuilders
				.get("about")
				.accept(MediaType.ALL))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
}
