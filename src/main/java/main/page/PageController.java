package main.page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.album.Album;
import main.album.AlbumController;
import main.exception.ControllerException;
import main.exception.NotFoundException;
import main.exception.TemplateException;
import main.img.ImageData;
import main.img.ImageDataController;
import main.misc.Util;
import main.template.AlbumTemplate;
import main.template.Template;
import main.template.TemplateFactory;

@RestController
public class PageController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(PageController.class);
	
	@Autowired
	ImageDataController dataController;
	@Autowired 
	AlbumController albumController;
	
    @RequestMapping(value= {"/{page}", "/"}, method= RequestMethod.GET,
    		produces = "application/json", headers = "target=body")
    public ResponseEntity<Page> getPageBody(Device device, 
    		@RequestParam(required=false) String ver,
    		@PathVariable(value="page",required=false) Optional<String> pageName
    				) throws TemplateException {
    	try {
    		String name;
    		if(pageName.isPresent()) {
    			name = pageName.get();
    		} else {
    			name = "main";
    		}
	    	Page page = new Page();
	    	Map<String, String> data = getPageData(name);
	    	page.setTitle(data.get("title"));
	    	page.setContent(data.get("page"));
	    	return new ResponseEntity<Page>(page, HttpStatus.OK);
    	} catch(Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
    	}
    }
	
    @RequestMapping(value= {"/{page}", "/"}, method= RequestMethod.GET)
    public ModelAndView getPage(Device device, 
    		@RequestParam(required=false) String ver, 
    		@PathVariable(required=false) Optional<String> page) 
    				throws ControllerException {
    	try {
    		String name;
    		if(page.isPresent()) {
    			name = page.get();
    		} else {
    			name = "main";
    		}
	  		ModelAndView mav = new ModelAndView("index");
      		Map<String, String> data = getPageData(name);
      		if(isMobile(device, ver)) {
      			data.put("isMobile", "true");
      		}
      		mav.getModel().putAll(data);
	    	return mav;
    	} catch(NotFoundException e) {
    		throw e;
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
		}
    }
    
    private Map<String, String> getPageData(String page) throws IOException {
    	Map<String, String> data = new HashMap<>();
    	switch(page) {
			case("main"):
	        	data.put("page", compileMain());
				data.put("title", "Главная");	
				break;
			case("portfolio"):
				Template template = TemplateFactory.createAlbumListTemplate(
						albumController.getRootAlbums());
				data.put("text", template.compile());
	        	data.put("page", TemplateFactory.buildTemplate(
	        			"templates/pages/portfolio", data).compile());
				data.put("title", "Портфолио");
				break;
			case("retouch"):
				data.put("text", TemplateFactory.buildTemplate(
						"static/text/ru/retouch").compile());
				data.put("page", TemplateFactory.buildTemplate(
						"templates/pages/retouch", data).compile());
				data.put("title", "Ретушь");
				break;
			case("about"):
				data.put("text", TemplateFactory.buildTemplate(
						"static/text/ru/about").compile());
				data.put("page", TemplateFactory.buildTemplate( 
						"templates/pages/about", data).compile());
				data.put("title", "Обо всем");
				break;
			case("contacts"):
				data.put("text", TemplateFactory.buildTemplate(
						"static/text/ru/contacts").compile()); 
				data.put("text", TemplateFactory.buildTemplate(
						"templates/pages/contacts").compile());
				data.put("title", "Контакты");
				break;
			default: 
				logger.error("Invalid page address: "+page);
				throw new NotFoundException("Invalid page address: "+page);
    	} 	
    	return data;
    }
    
    @RequestMapping(value= "/portfolio/albums/{id}",
    		method = RequestMethod.GET, produces = "application/json",
    		headers = "target=body")
    public ResponseEntity<Page> getAlbumBody(Device device, 
    		@PathVariable() String id,
    		@RequestParam(required=false) String ver) throws TemplateException {
    	try {
	    	Page page = new Page();
	    	Album album = albumController.getAlbum(id);
	    	Template template = new AlbumTemplate(album);	    	
	    	page.setTitle(Util.toUpperCase(album.getName()));
	    	page.setContent(template.compile());
	    	return new ResponseEntity<Page>(page, HttpStatus.OK);
    	} catch(Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
    	}
    }
    
    @RequestMapping(value= "/portfolio/albums/{id}", 
    		method= RequestMethod.GET)
    public ModelAndView getAlbum(Device device,
    		@PathVariable() String id,
    		@RequestParam(required=false) String ver) throws TemplateException {
    	ModelAndView mav = null;
      	try {
      		mav = new ModelAndView("index");
	    	Album album = albumController.getAlbum(id);
	    	Template template = new AlbumTemplate(album);
	    	mav.getModel().put("name", album.getName());
	        mav.getModel().put("title", Util.toUpperCase(album.getName()));
			mav.getModel().put("page", template.compile());
			mav.getModel().put("isMobile", isMobile(device, ver));
			return mav;
    	} catch (Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
		}
    }
    
    private String compileMain() {
    	Template template;
		try {
    		Map<String, String> textData = new HashMap<>();
    		Map<String, String> pageData = new HashMap<>();
			List<ImageData> dataList = dataController.getLatestImages();
			if(dataList != null && dataList.size() > 0) {
				template = TemplateFactory.createImageListTemplate(dataList);
				textData.put("latest", template.compile());
			}
    		template = TemplateFactory.buildTemplate(
    				"static/text/ru/main", textData);
    		pageData.put("text", template.compile());
    		template = TemplateFactory.buildTemplate(
    				"templates/pages/main", pageData);
    		return template.compile();
    	} catch(IOException | IllegalArgumentException e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
        	return null;
        }
    }
 
    private boolean isMobile(Device device, String ver) {
    	if(ver == null) {
    		ver = "";
    	}
    	if(device.isNormal() && ver.toString().equals("") 
    			|| ver.equals("desktop")) {
    		return false;
	  	} else if((device.isMobile() && ver.toString().equals(""))
	  			|| (device.isTablet() && ver.toString().equals(""))
    			|| ver.toString().equals("mobile")) {
    		return true;
	  	} else {
	  		logger.warn("Cannot specify device! ver:"+ver);
	  		return true;
	  	}
    }
    
}
