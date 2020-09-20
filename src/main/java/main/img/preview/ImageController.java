package main.img.preview;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public interface ImageController {
	
	@RequestMapping(value="/preview/{size}/{id}", 
			produces = MediaType.IMAGE_JPEG_VALUE)
	ModelAndView getPreview(@PathVariable String size, @PathVariable String id);
	
	@RequestMapping(value="/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	ModelAndView getImage(@PathVariable String id);
	
}