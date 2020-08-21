
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import main.PageController;
import main.misc.ProjectConfiguration;

@ContextConfiguration(classes=ProjectConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = EinnfeigrWebsite.class)
@WebAppConfiguration
public class TestController {
	
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	PageController pageController;
	
    private MockMvc mvc;
	
	@Before
	public void setup() throws Exception {
	    this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testPageController() throws Exception { 
		assertThat(pageController).isNotNull();
		mvc.perform(MockMvcRequestBuilders
				.get("/")
				.header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36")
				.accept(MediaType.ALL))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
}
