package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.exception.ControllerException;
import main.misc.Util;
import main.page.PageTemplateData;
import main.template.EssentialMapTemplate;
import main.template.Template;

@Component
@RestController
public class SecurityController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(SecurityController.class);
	
	@RequestMapping(value="/login", method= RequestMethod.GET)
	public ModelAndView login() {
		try {
			ModelAndView mav = new ModelAndView("index");
			PageTemplateData data = new PageTemplateData();
			data.setMobile(false);
			data.setTitle("Вход");
			Template template = new EssentialMapTemplate() {};
			template.setTemplatePath("templates/pages/login");
			data.setPage(template.compile());
			mav.getModel().put("title", data.getTitle());
			mav.getModel().put("page", data.getPage());
			return mav;
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException ex = new ControllerException(e);
			throw ex;
		}
	}
	
	
	
}
