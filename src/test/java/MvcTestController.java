
<<<<<<< HEAD
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
=======
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
>>>>>>> debug-branch-1
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
<<<<<<< HEAD

import main.PageController;
import main.SecurityController;
import main.misc.configuration.ProjectConfiguration;
import main.misc.configuration.SecurityConfiguration;

@RunWith(SpringRunner.class)
@WebMvcTest(PageController.class)
@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
@ImportResource({ "classpath:applicationContext.xml" })
public class MvcTestController {

	private static final String MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; Android 7.1; Mi A1 Build/N2G47H";
	
	@Autowired
	PageController pageController;
	
	@Autowired
	SecurityController securityController;
	
	@Autowired
    private MockMvc mvc;
	
	@Test
	public void testPageController() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testSecurityController() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("login")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk());
		mvc.perform(MockMvcRequestBuilders
				.get("dashboard")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
=======
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
>>>>>>> debug-branch-1
	}
	
}
