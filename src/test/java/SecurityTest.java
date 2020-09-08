import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import main.ProjectConfiguration;
import main.dashboard.DashboardController;
import main.security.SecurityConfiguration;
import main.security.SecurityController;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers= {DashboardController.class, SecurityController.class})
@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
public class SecurityTest {

	private static final String MOBILE_USER_AGENT = 
			"Mozilla/5.0 (Linux; Android 7.1; Mi A1 Build/N2G47H)";
	
	@Autowired
	SecurityController securityController;
	
	@Autowired
    private MockMvc mvc;
	
	@Test
	public void unautorizedCheck() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/dashboard")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	
	@Test 
	public void loginAvailable() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/login")
				.header("user-agent", MOBILE_USER_AGENT)
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	

	@WithMockUser(value = "studiedlist")
	@Test
	public void dashboard() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/dashboard/watch")
				.header("user-agent", MOBILE_USER_AGENT)
				.param("id", "id")
				.accept(MediaType.ALL))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}
}
