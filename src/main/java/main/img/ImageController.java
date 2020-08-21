package main.img;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ImageController {

	@RequestMapping(value="image/{imgPath}")
	public ModelAndView showPreview() {
		return null;
	}
	
}
