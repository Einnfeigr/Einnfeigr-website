package main.drive.preview;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public interface PreviewController {
	
	@RequestMapping(value="/preview/{size}/{id}")
	ModelAndView getPreview(
			@PathVariable String size,
			@PathVariable String id
			);
	
}