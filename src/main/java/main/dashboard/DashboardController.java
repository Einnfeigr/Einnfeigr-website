package main.dashboard;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import main.drive.DriveUtils;
import main.exception.ControllerException;
import main.http.Request;
import main.http.RequestBuilder;
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
	private ImageStorageService storageService;
	
	@RequestMapping(value="/dashboard/login")
	public ModelAndView redirectToLogin(ModelMap model) {
		model.addAttribute("attribute", "redirectWithRedirectPrefix");
		String redirectUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
			  +"client_id="+System.getenv("clientId")+"&"
			  +"response_type=code&"
			  +"state=state_parameter_passthrough_value&"
			  +"scope=https%3A//www.googleapis.com/auth/drive.file&"
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
	
	@RequestMapping(value="/dashboard/code/get", method=RequestMethod.GET)
	public ModelAndView getCodeLoginForm() {
		return new ModelAndView("pages/dashboard/code/get");
	}
	
	@RequestMapping(value="/dashboard/code/get", method=RequestMethod.POST)
	public ResponseEntity<String> getCode(@RequestParam String password) {
		if(password.equals(System.getenv("adminPassword"))) {
			return new ResponseEntity<String>(driveUtils.getUserCode(), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Wrong password", 
					HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/dashboard/code/set", method=RequestMethod.POST)
	public ResponseEntity<String> setCode(@RequestParam String password, 
			@RequestParam String code) {
		if(password.equals(System.getenv("adminPassword"))) {
			driveUtils.setUserCode(code);
			return new ResponseEntity<String>("Code have been set",
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
	
	@RequestMapping(value="/dashboard/download/{filePath}",
			method=RequestMethod.GET)
	public ResponseEntity<Resource> downloadFile(
			@PathVariable String filePath) {
		Resource file = storageService.loadAsResource(filePath);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@RequestMapping(value="/dashboard/upload", method=RequestMethod.GET)
	public ModelAndView showUploadForm() {
		return new ModelAndView("pages/dashboard/upload");
	}
	
	@RequestMapping(value="/dashboard/upload", method=RequestMethod.POST)
	public ResponseEntity<String> uploadFile(
			@RequestParam("file") MultipartFile[] files,
			@RequestParam("path") String path) {
		if(path == null) {
			path = "/img/portfolio/albums/ретушь/";
		}
		Template template;
		try {
			template = TemplateFactory.buildTemplate(
					"static/text/ru/dashboard/upload/success");
			for(MultipartFile file : files) {
				storageService.store(file, path);
				
			}
			template = TemplateFactory.buildTemplate(
					"templates/index");
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION)
					.body(template.compile());
		} catch (Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			return ResponseEntity.badRequest().body(null);
		}

	}
	
	/*@RequestMapping(value="/dashboard/exchange", method=RequestMethod.GET)
	public ModelAndView showExchangeForm() {
		
	}
	
	@RequestMapping(value="/dashboard/exchange", method=RequestMethod.POST)
	public ResponseEntity<String> exchange() {
		
	}*/
	
	@RequestMapping(value="/dashboard/watch", method=RequestMethod.GET)
	public ModelAndView showStartWatchForm() {
		try {
			ModelAndView mav = new ModelAndView("index");
			Template template = TemplateFactory.buildTemplate(
					"templates/pages/dashboard/watch");
			mav.getModel().put("title", title);
			mav.getModel().put("page", template.compile());
			return mav;
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping(value="/dashboard/watch", method=RequestMethod.POST)
	public ResponseEntity<String> startWatch(
			@RequestParam(required=false) String id) throws IOException {
		if(id != null) {
			id = id.trim();
		}
		if(!id.equals(System.getenv("drive.channelId"))) {
			return ResponseEntity.badRequest().body("Invalid id ("+id+")");
		}
		RequestBuilder builder = new RequestBuilder();
		Map<String, String> content = new HashMap<>();
		content.put("id", id);
		content.put("type", "webhook");
		content.put("address", System.getenv("currentUrl")
				+"/api/drive/callback");
		logger.info("current user code: "+driveUtils.getUserCode());
		Request request = builder.blank()
				.address("https://www.googleapis.com/drive/v3/files/"+
					DriveUtils.rootId+"/watch/")
				.method("POST")
				.content(new Gson().toJson(content))
				.authorization("Bearer "+driveUtils.getUserCode())
				.contentType("application/json")
				.build();
		return ResponseEntity.ok().body(request.perform().getContent());
	}
}
