package main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.exception.ControllerException;
import main.exception.NotFoundException;
import main.exception.TemplateException;
import main.img.ImageDataController;
import main.misc.Util;
import main.section.Section;
import main.section.SectionsController;
import main.template.EssentialTemplate;
import main.template.ImageListTemplate;
import main.template.SectionTemplate;
import main.template.SectionsTemplate;
import main.template.Template;
import main.template.data.page.PageTemplateData;
import main.template.data.page.SectionPageTemplateData;
import main.template.data.text.MainTextTemplateData;
import main.template.data.text.TextTemplateData;

@RestController
public class PageController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(PageController.class);
	
    @RequestMapping(value= {"/{page}", "/"}, method= RequestMethod.GET,
    		produces = "application/json", headers = "target=body")
    public ResponseEntity<Page> getPageBody(Device device, 
    		@RequestParam(required=false) String ver,
    		@PathVariable(value="page",required=false) Optional<String> pageName)
    				throws TemplateException {
    	try {
    		String name;
    		if(pageName.isPresent()) {
    			name = pageName.get();
    		} else {
    			name = "main";
    		}
	    	Page page = new Page();
	    	PageTemplateData data = getPageData(name, device, ver);
	    	page.setTitle(data.getTitle());
	    	page.setContent(data.getPage());
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
      		PageTemplateData data = getPageData(name, device, ver);
	        mav.getModel().put("title", data.getTitle());
			mav.getModel().put("page", data.getPage());
			mav.getModel().put("isMobile", data.isMobile());
	    	return mav;
    	} catch(NotFoundException e) {
    		throw e;
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
		}
    }
    
    private PageTemplateData getPageData(String page, Device device, String ver) 
    		throws IOException {
    	PageTemplateData data = new PageTemplateData();
    	data.setMobile(isMobile(data, device, ver));
    	switch(page) {
			case("main"):
	        	data = loadPage(compileMain(data), "static/text/ru/main", 
						"templates/pages/main");
				data.setTitle("Главная");	
				break;
			case("portfolio"):
				Template sections = new SectionsTemplate(
						SectionsController.getSections());
				data.setText(sections.compile());
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
				throw new NotFoundException("Invalid page address: "+page);
    	} 	
    	return data;
    }
    
    @RequestMapping(value= "/portfolio/sections/{section}",
    		method = RequestMethod.GET, produces = "application/json",
    		headers = "target=body")
    public ResponseEntity<Page> getSectionBody(Device device, 
    		@PathVariable("section") String sectionName,
    		@RequestParam(required=false) String ver) throws TemplateException {
    	try {
	    	Page page = new Page();
	    	Template template = new SectionTemplate(
	    			SectionsController.getSection(sectionName));	    	
	    	page.setTitle(Util.toUpperCase(sectionName));
	    	page.setContent(template.compile());
	    	return new ResponseEntity<Page>(page, HttpStatus.OK);
    	} catch(Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
    	}
    }
    
    @RequestMapping(value= "/portfolio/sections/{section}", 
    		method= RequestMethod.GET)
    public ModelAndView getSection(Device device,
    		@PathVariable("section") String sectionName,
    		@RequestParam(required=false) String ver) throws TemplateException {
    	ModelAndView mav = null;
    	PageTemplateData data = new SectionPageTemplateData();
      	try {
      		mav = new ModelAndView("index");
      		data.setMobile(isMobile(data, device, ver));
	    	data.setTitle(Util.toUpperCase(sectionName));
	    	Section section = SectionsController.getSection(sectionName);
	    	Template template = new SectionTemplate(section);
	    	data.setPage(template.compile());
	    	mav.getModel().put("name", section.getName());
	        mav.getModel().put("title", data.getTitle());
			mav.getModel().put("page", data.getPage());
			return mav;
    	} catch (Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
		}
    }
    
    private PageTemplateData compileMain(PageTemplateData data) {
    	try {
    		List<File> latest = ImageDataController.getLatestImages();
    		Template template = new ImageListTemplate(latest);
    		String images = template.compile();
    		TextTemplateData mData = new MainTextTemplateData(images);
        	data.setTextData(mData);
    	} catch(IOException | IllegalArgumentException e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
    	}
    	return data;
    }
    
    private PageTemplateData compileRetouch(PageTemplateData data) {
		TextTemplateData rData = new TextTemplateData();
		data.setTextData(rData);
		return data;
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
    		Template template = new EssentialTemplate(textPath);
    		template.setData(data.getTextData());
    		data.setText(template.compile());
    	}
    	Template template = new EssentialTemplate(pagePath);
    	template.setData(data);
    	data.setPage(template.compile());
        return data;
    }
    
    private boolean isMobile(
    		PageTemplateData data, Device device, String ver) {
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
