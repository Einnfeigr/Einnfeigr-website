import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import main.ProjectConfiguration;
import main.controller.view.ViewController;
import main.exception.NotFoundException;
import main.security.SecurityConfiguration;
import main.security.SecurityController;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers= {ViewController.class})
@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
public class PageTest {

	private static final String MOBILE_USER_AGENT = 
			"Mozilla/5.0 (Linux; Android 7.1; Mi A1 Build/N2G47H)";
	
	@Autowired
	SecurityController securityController;
	
	@Autowired
    private MockMvc mvc;
	
	@Test
	public void main() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void portfolio() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/portfolio")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}	
	
	@Test
	public void retouch() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/retouch")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}	
	
	@Test
	public void contacts() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/contacts")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}	
	
	@Test
	public void about() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/about")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void notFound() {
		try {
			mvc.perform(MockMvcRequestBuilders
					.get("/a")
					.header("user-agent", MOBILE_USER_AGENT)
					.accept(MediaType.ALL))
					.andExpect(MockMvcResultMatchers.status().isNotFound());
			assertThat(false);
		} catch(Exception e) {
			assertThat(e instanceof NotFoundException);
		}
	}
	
	@Test
	public void params() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/?ver=null&target=null")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void mobile() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/?target=body")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				//Todo find "nav" pane in content
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
	