package main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.exception.ControllerException;
import main.exception.TemplateException;
import main.img.ImageController;
import main.misc.Util;
import main.pojo.PageTemplateData;
import main.pojo.TextTemplateData;
import main.section.Section;
import main.section.SectionsController;
import main.template.TemplateController;

@RestController
public class PageController {
	
	private Logger logger = LoggerFactory.getLogger(ImageController.class);
	
    @RequestMapping(value= {"/{page}", "/"}, method= RequestMethod.GET)
    public ModelAndView getPage(Device device, 
    		@RequestParam(required=false) String path,
    		@RequestParam(required=false) String ver, 
    		@RequestParam(required=false) String target,
    		@PathVariable(required=false) String page) 
    				throws ControllerException {
    	ModelAndView mav;
    	PageTemplateData data = new PageTemplateData();
  		if(page == null) {
  			page = "main";
  		}
  		if(path == null) {
  			path = getPath(page);
  		}
    	try {
	  		mav = createModelAndView(device, ver, target, data);
      		data.setPath(path);
      		switch(page) {
	    		case("main"):
		        	data = loadPage(compileMain(data), "static/text/ru/main", 
	    					"templates/pages/main");
    				data.setTitle("Главная");	
	    			break;
	    		case("portfolio"):	    
	    			data.setText(TemplateController.compileSections(
	    					SectionsController.getSections(), data.getPath()));
		        	data = loadPage(data, null, 
    						"templates/pages/portfolio");
    				data.setTitle("Портфолио");
	    			break;
	    		case("retouch"):
	    			data = loadPage(compileRetouch(data), 
	    					"static/text/ru/retouch", 
	    					"templates/pages/retouch");
    				data.setTitle("Ретушь");
	    			break;
	    		case("about"):
	    			data = loadPage(data, "static/text/ru/about", 
	    					"templates/pages/about");
    				data.setTitle("Обо всем");
	    			break;
	    		case("contacts"):
	    			data = loadPage(data, "static/text/ru/contacts", 
	    					"templates/pages/contacts");
    				data.setTitle("Контакты");
	    			break;
	    		default: 
	    			logger.error("Invalid page address: "+page);
	    			throw new IOException("Invalid page address: "+page);
	    	}
	        mav.getModel().put("path", data.getPath());
	        mav.getModel().put("title", data.getTitle());
			mav.getModel().put("page", data.getPage());
			mav.getModel().put("isMobile", data.getIsMobile());
	    	return mav;
		} catch (Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			if(data != null) {
				exception.setPath(data.getPath());
			} else { 
				exception.setPath(getPath(path));
			}
			throw exception;
		}
    }
    
    @RequestMapping(value= "/portfolio/sections/{section}", 
    		method= RequestMethod.GET)
    public ModelAndView getSection( Device device,
    		@PathVariable("section") String sectionName,
    		@RequestParam(required=false) String ver,
    		@RequestParam(required=false) String target,
    		@RequestParam(required=false) String path
    		) throws TemplateException {
    	ModelAndView mav = null;
    	PageTemplateData data = new PageTemplateData();
    	try {
	  		mav = createModelAndView(device, ver, target, data);
	  		if(path == null) {
	    		path = "../../../";
	    	}
	    	data.setPath(path);
	    	data.setTitle(sectionName);
	        String title = Util.toUpperCase(data.getTitle());
	    	Section section = SectionsController.getSection(sectionName);
	    	data.setPage(TemplateController.compileSection(section, path));
	    	mav.getModel().put("name", section.getName());
	        mav.getModel().put("path", path);
	        mav.getModel().put("title", title);
			mav.getModel().put("page", data.getPage());
			return mav;
    	} catch (Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			exception.setPath(data.getPath());
			throw exception;
		}
    }
    
    private PageTemplateData compileMain(PageTemplateData data) {
    	try {
    		final String tPath = data.getPath();
        	@SuppressWarnings("unused")
        	TextTemplateData mainTextData = new TextTemplateData() {
        		String latestLoaded = TemplateController
        				.compileLatestLoaded(tPath);
        	};
        	data.setTextData(mainTextData);
    	} catch(IOException e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
    	}
    	return data;
    }
    
    private PageTemplateData compileRetouch(PageTemplateData data) {
    	final String tPath = data.getPath();
		@SuppressWarnings("unused")
		TextTemplateData retouchTextData = new TextTemplateData() {
			String path = tPath;
		};
		data.setTextData(retouchTextData);
		return data;
    }
    
    private String getPath(String url) {
    	if(url.equals("main")) {
    		return "../";
    	} else {
    		return "./";
    	}
    }
    
    private PageTemplateData loadPage(PageTemplateData data, String textPath, 
    		String pagePath) throws IOException {
        if(data == null || pagePath == null) {
        	throw new NullPointerException("Null passed as argument");
        }
    	if(textPath != null) {
    		if(data.getTextData() == null) {
    			data.setTextData(new TextTemplateData() {});
    		}
    		data.setText(TemplateController.compileTemplate(textPath,
    				data.getTextData()));
    	}
    	data.setPage(TemplateController.compileTemplate(pagePath, data));
        return data;
    }
    
    private ModelAndView createModelAndView(Device device,
    		String ver, String target, PageTemplateData data) {
    	if(ver == null) {
    		ver = "";
    	}
    	StringBuilder templatePath = new StringBuilder("");
    	if(device.isNormal() && ver.toString().equals("") 
    			|| ver.equals("desktop")) {
    		data.setIsMobile(null);
	  	} else if((device.isMobile() && ver.toString().equals(""))
	  			|| (device.isTablet() && ver.toString().equals(""))
    			|| ver.toString().equals("mobile")) {
    		data.setIsMobile(true);
	  	} else {
	  		throw new NullPointerException("Cannot specify device! ver:"+ver);
	  	}
    	if(target == null) {
    		target = "";
    	}
    	if(target.equals("body")) {
    		templatePath.append("placeholder");
    	} else {
    		templatePath.append("index");
    	}
    	return new ModelAndView(templatePath.toString());
    }
    
}
