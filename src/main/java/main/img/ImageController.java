package main.img;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ImageController {

	@RequestMapping(value="/image", method= RequestMethod.GET)
	public ModelAndView showPreview(@RequestParam String imgPath) {
		ModelAndView mav = new ModelAndView("misc/img/preview");
		mav.getModel().put("imagePath", imgPath);
		return mav;
	}
	
}
