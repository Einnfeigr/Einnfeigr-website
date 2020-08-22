package unit;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

import main.pojo.TemplateData;
import main.template.TemplateController;

@RunWith(SpringJUnit4ClassRunner.class)
public class TemplateTestController {

	@Test
	public void testCompileTemplate() throws IOException {
		@SuppressWarnings("unused")
		TemplateData data = new TemplateData() {
			String text = "test";
		};
		assertThat(TemplateController.compileTemplate("test", data)
				.equals("<div class=\"test\">test</div>"));
	}
	
}
