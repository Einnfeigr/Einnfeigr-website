package main.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.drive.DriveUtils;
import main.exception.ControllerException;
import main.misc.Util;
import main.template.Template;
import main.template.TemplateFactory;
import main.template.data.PageTemplateData;

@Component
@RestController
public class SecurityController {
	
	@Autowired
	DriveUtils driveUtils;
	
	private final static Logger logger = 
			LoggerFactory.getLogger(SecurityController.class);
	
	@RequestMapping(value="/login", method= RequestMethod.GET)
	public ModelAndView login(@RequestParam(required=false) String code) {
		if(code != null) {
			try {
			Map<String, String> data = new HashMap<>();
			data.put("code", code);
			ModelAndView mav = new ModelAndView("index");
			String page = TemplateFactory.buildTemplate(
					"/templates/misc/confirmPass.mustache", data).compile();
			mav.getModel().put("title", "Подтверждение пароля");
			mav.getModel().put("page", page);
			return mav;
			} catch(IOException e) {
				logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			}
		}
		try {
			ModelAndView mav = new ModelAndView("index");
			PageTemplateData data = new PageTemplateData();
			data.setMobile(false);
			data.setTitle("Вход");
			Template template = TemplateFactory.buildTemplate(
					"templates/pages/login");
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
