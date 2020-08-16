package main;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.exception.TemplateException;
import main.pojo.PageTemplateData;
import main.pojo.TextTemplateData;
import main.section.Section;
import main.section.SectionsController;

@RestController
public class PageController {
	
    @RequestMapping(value= {"/", "/portfolio", "/retouch", "/about"}, method= RequestMethod.GET)
    ModelAndView getPage(Device device, HttpServletRequest request) throws TemplateException {
    	ModelAndView mav = null;
    	PageTemplateData data = new PageTemplateData();
  		String path;
  		mav = createModelAndView(device, request);
    	if(request.getParameter("path") != null ) {
    		data.setPath(request.getParameter("path"));
    	}
    	try {  
      		String requestUrl = request.getRequestURI();
      		while(requestUrl.endsWith("/") && requestUrl.length() > 1) {
      			requestUrl = requestUrl.substring(0, requestUrl.length()-1);
      		}
      		switch(requestUrl) {
	    		case("/"):
	    			path = "./";
		        	if(data.getPath() == null) {
		            	data.setPath(path);
		            }
	    			data = loadPage(data, "static/text/ru/main.mustache", 
	    					"templates/pages/main.mustache");
    				data.setTitle("Главная");	
	    			break;
	    		case("/portfolio"):	    
	    			path = "../";
		        	if(data.getPath() == null) {
		            	data.setPath(path);
		            }
		        	SectionsController.loadSections();
	    			data.setText(SectionsController.compileSections(data.getPath()));
		        	data = loadPage(data, null, 
    						"templates/pages/portfolio.mustache");
    				data.setTitle("Портфолио");
	    			break;
	    		case("/retouch"):
	    			path = "../";		       
	    			if(data.getPath() == null) {
		            	data.setPath(path);
		            }
	    			data = loadPage(data, "static/text/ru/retouch.mustache", 
	    					"templates/pages/retouch.mustache");
    				data.setTitle("Ретушь");
	    			break;
	    		case("/about"):
	    			path = "../";
		        	if(data.getPath() == null) {
		            	data.setPath(path);
		            }
	    			data = loadPage(data, "static/text/ru/about.mustache", 
	    					"templates/pages/about.mustache");
    				data.setTitle("Обо всем");
	    			break;
	    		default: 
	    			throw new IOException("URL: "+requestUrl);
	    	}
	        mav.getModel().put("path", path);
	        mav.getModel().put("title", data.getTitle());
			mav.getModel().put("page", data.getPage());
		} catch (Exception e) {
			e.printStackTrace();
			TemplateException exception = new TemplateException(e);
			exception.setPath(data.getPath());
			throw exception;
		}
        return mav;	
    }
    
    @RequestMapping(value= "portfolio/sections/{section}", method= RequestMethod.GET)
    ModelAndView getSection(@PathVariable("section") String sectionName,
    		Device device, HttpServletRequest request) throws TemplateException {
    	ModelAndView mav = null;
    	PageTemplateData data = new PageTemplateData();
  		String path;
    	try {
	  		mav = createModelAndView(device, request);
	    	if(request.getParameter("path") != null ) {
	    		path = request.getParameter("path");
	    	} else {
	    		path = "../../../";
	    	}
	    	data.setPath(path);
	    	data.setTitle(sectionName);
	        String title = Util.UrlToUpperCase(data.getTitle());
	    	Section section = SectionsController.getSection(sectionName);
	    	data.setPage(SectionsController.compileSection(section, path));
	    	mav.getModel().put("name", section.getName());
	        mav.getModel().put("path", path);
	        mav.getModel().put("title", title);
			mav.getModel().put("page", data.getPage());
			return mav;
    	} catch (Exception e) {
			e.printStackTrace();
			TemplateException exception = new TemplateException(e);
			exception.setPath(data.getPath());
			throw exception;
		}
    }
    
    private PageTemplateData loadPage(PageTemplateData data, String textPath, String pagePath) throws IOException {
        if(data == null || pagePath == null) {
        	throw new NullPointerException("Null passed as argument");
        }
    	if(textPath != null) {
    		textPath = Util.toAbsoluteUrl(textPath);
    		if(data.getTextData() == null) {
    			data.setTextData(new TextTemplateData() {});
    		}
    		data.setText(Util.compileTemplate(textPath, data.getTextData()));
    	}
    	pagePath = Util.toAbsoluteUrl(pagePath);
    	data.setPage(Util.compileTemplate(pagePath, data));
        return data;
    }
    
    private ModelAndView createModelAndView(Device device,
    		HttpServletRequest request) {
    	StringBuilder ver = null;
    	StringBuilder templatePath = new StringBuilder("");
    	if(request.getParameter("ver") != null) {
    		ver = new StringBuilder("");
    		ver.append(request.getParameter("ver"));
    	}
    	if(device.isNormal() && ver == null 
    			|| ver.toString().equals("desktop")) {
    		templatePath.append("desktop/");
	  	} else if(device.isMobile() && ver == null 
    			|| ver.toString().equals("mobile")) {
    		templatePath.append("mobile/");
    	} else if(device.isTablet() && ver == null 
    			|| ver.toString().equals("tablet")) {
    		templatePath.append("tablet/");
    	}
    	if(request.getParameter("target") != null) {
    		if(request.getParameter("target").equals("body")) {
    			templatePath.append("placeholder");
    		} else {
    			templatePath.append("index");
    		}
    	} else {
    		templatePath.append("index");
    	}
    	return new ModelAndView(templatePath.toString());
    }
}
