package main.img;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.exception.ControllerException;

@RestController
public class ImageController {

	private final static Logger logger = 
			LoggerFactory.getLogger(ImageController.class);
	
	@RequestMapping(value="/image", method= RequestMethod.GET)
	public ModelAndView showPreview(@RequestParam String imgPath) {
		try {
			ModelAndView mav = new ModelAndView("misc/img/preview");
			mav.getModel().put("id", imgPath.hashCode());
			mav.getModel().put("imagePath", imgPath);
			return mav;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new ControllerException(e);
		}
	}
	
}
