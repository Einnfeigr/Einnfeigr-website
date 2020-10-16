package main.img.preview;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public interface ImageController {
	
	@RequestMapping(value="/preview/{size}/{id}", 
			produces = MediaType.IMAGE_JPEG_VALUE)
	ModelAndView getPreview(@PathVariable String size, @PathVariable String id);
	
	@RequestMapping(value="/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody String getImage(HttpServletResponse response, 
			@PathVariable String id) throws FileNotFoundException, IOException;
	
}