import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import main.drive.DriveUtils;
import main.exception.NotFoundException;
import main.page.PageController;
import main.security.SecurityConfiguration;
import main.security.SecurityController;
import unit.DriveTest;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers= {DashboardController.class})
@ContextConfiguration(
		classes= {ProjectConfiguration.class, SecurityConfiguration.class})
public class DashboardTest {
	
	private static Logger logger; 
			
	
	public DashboardTest() {
		logger = (ch.qos.logback.classic.Logger) 
				LoggerFactory.getLogger(DashboardTest.class);
		((ch.qos.logback.classic.Logger)logger)
				.setLevel(ch.qos.logback.classic.Level.DEBUG);
	}
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	DriveUtils driveUtils;
	
	@WithMockUser(value = "studiedlist")
	@Test
	public void codeRetrieveAndExchange() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.post("/dashboard/code/set")
				.param("code", System.getenv("userCode"))
				.param("password", System.getenv("adminPassword"))
				).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		mvc.perform(MockMvcRequestBuilders
				.post("/dashboard/watch")
				.param("password", System.getenv("adminPassword"))
				).andExpect(MockMvcResultMatchers.status().isOk());
	}
}
