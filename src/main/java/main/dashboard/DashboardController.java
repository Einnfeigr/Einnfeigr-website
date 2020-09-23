package main.dashboard;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.album.Album;
import main.drive.DriveUtils;
import main.drive.dao.PortfolioDriveDao;
import main.exception.ControllerException;
import main.misc.Util;
import main.template.Template;
import main.template.TemplateFactory;

@RestController
public class DashboardController {
	
	private static final String title = "Панель управления";
	private static final Logger logger = 
			LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private DriveUtils driveUtils;
	
	@Autowired
	private PortfolioDriveDao portfolioDao;
	
	@RequestMapping(value="/dashboard/login")
	public ModelAndView redirectToLogin(ModelMap model) {
		model.addAttribute("attribute", "redirectWithRedirectPrefix");
		String redirectUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
			  +"client_id="+System.getenv("clientId")+"&"
			  +"response_type=code&"
			  +"access_type=offline&"
			  +"state=state_parameter_passthrough_value&"
			  +"scope=https%3A//www.googleapis.com/auth/drive&"
			  +"redirect_uri="+System.getenv("currentUrl")+"/login&"
			  +"prompt=consent&"
			  +"include_granted_scopes=true";
		return new ModelAndView("redirect:"+redirectUrl, model);
		
	}
	
	@RequestMapping(value="/dashboard/code/set", method=RequestMethod.GET)
	public ModelAndView setCodeLoginForm() {
		try {
			Template template = TemplateFactory.buildTemplate(
					"templates/pages/dashboard/code/set");
			ModelAndView mav = new ModelAndView("index");
			mav.getModel().put("page", template.compile());
			mav.getModel().put("title", title); 
			return mav;
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping(value="/dashboard/code/set", method=RequestMethod.POST)
	public ResponseEntity<String> setCode(@RequestParam String password, 
			@RequestParam String code) {
		if(!password.equals(System.getenv("adminPassword"))) {
			return new ResponseEntity<String>("Wrong password", 
					HttpStatus.UNAUTHORIZED);
		}			
		try {
			driveUtils.setUserCode(code);
			List<Album> folders = portfolioDao.getAllFolders();
			for(Album folder : folders) {
				driveUtils.registerWatchService(
						String.valueOf(new Random().nextLong()), 
						folder.getId());
			}
			return new ResponseEntity<String>("Code have been set",
					HttpStatus.OK);
		} catch(IOException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			return new ResponseEntity<String>(e.getMessage(), 
					HttpStatus.BAD_GATEWAY);
		}
	}
	
	@RequestMapping(value="/dashboard/code/get", method=RequestMethod.GET)
	public ModelAndView getCodeLoginForm() {
		return new ModelAndView("pages/dashboard/code/get");
	}
	
	@RequestMapping(value="/dashboard/code/get", method=RequestMethod.POST)
	public ResponseEntity<String> getCode(@RequestParam String password) {
		if(password.equals(System.getenv("adminPassword"))) {
			return new ResponseEntity<String>(driveUtils.getUserCode(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Wrong password", 
					HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public ModelAndView showMain() {
		ModelAndView mav =  new ModelAndView("index");
		try {
			Template template = TemplateFactory.buildTemplate(
					"templates/pages/dashboard/main");
			mav.getModel().put("page", template.compile());
			mav.getModel().put("title", title);
			return mav;
		} catch (Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			throw new ControllerException(e);
		}
	}

}
