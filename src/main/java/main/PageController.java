package main;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.exception.ControllerException;
import main.exception.TemplateException;
import main.img.ImageDataController;
import main.misc.Util;
import main.pojo.PageTemplateData;
import main.pojo.TemplateData;
import main.pojo.TextTemplateData;
import main.section.Section;
import main.section.SectionsController;

@RestController
public class PageController {
	
	@Autowired
	private ImageDataController imageDataController;
	
	public PageController() {}
	
    @RequestMapping(value= {"/", "/portfolio", "/retouch", "/about", 
    		"/contacts"}, method= RequestMethod.GET)
    public ModelAndView getPage(Device device, HttpServletRequest request) 
    		throws ControllerException {
    	ModelAndView mav = null;
    	PageTemplateData data = new PageTemplateData();
  		String path;
    	try {
	  		mav = createModelAndView(device, request, data);
	    	if(request.getParameter("path") != null ) {
	    		data.setPath(request.getParameter("path"));
	    	}
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
		        	try {
		        		List<String> paths = imageDataController.getImagePaths(path);
			        	@SuppressWarnings("unused")
			        	TextTemplateData mainTextData = new TextTemplateData() {
			        		String latestLoaded = compileLatestLoaded(paths);
			        	};
			        	data.setTextData(mainTextData);
		        	} catch(IOException e) {
		        		e.printStackTrace();
		        	}
		        	data = loadPage(data, "static/text/ru/main", 
	    					"templates/pages/main");
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
    						"templates/pages/portfolio");
    				data.setTitle("Портфолио");
	    			break;
	    		case("/retouch"):
	    			path = "../";		       
	    			if(data.getPath() == null) {
		            	data.setPath(path);
		            }
	    			final String tPath = data.getPath();
    				@SuppressWarnings("unused")
	    			TextTemplateData retouchTextData = new TextTemplateData() {
						String path = tPath;
	    			};
	    			data.setTextData(retouchTextData);
	    			data = loadPage(data, "static/text/ru/retouch", 
	    					"templates/pages/retouch");
    				data.setTitle("Ретушь");
	    			break;
	    		case("/about"):
	    			path = "../";
		        	if(data.getPath() == null) {
		            	data.setPath(path);
		            }
	    			data = loadPage(data, "static/text/ru/about", 
	    					"templates/pages/about");
    				data.setTitle("Обо всем");
	    			break;
	    		case("/contacts"):
	    			path = "../";
		        	if(data.getPath() == null) {
		            	data.setPath(path);
		            }
	    			data = loadPage(data, "static/text/ru/contacts", 
	    					"templates/pages/contacts");
    				data.setTitle("Обо всем");
	    			break;
	    		default: 
	    			throw new IOException("URL: "+requestUrl);
	    	}
	        mav.getModel().put("path", path);
	        mav.getModel().put("title", data.getTitle());
			mav.getModel().put("page", data.getPage());
			mav.getModel().put("isMobile", data.getIsMobile());
	    	return mav;
		} catch (Exception e) {
			e.printStackTrace();
			ControllerException exception = new ControllerException(e);
			if(data != null) {
				exception.setPath(data.getPath());
			} else {
				//TODO refactor | getPath method 
				exception.setPath("./");
			}
			throw exception;
		}
    }
    
    @RequestMapping(value= "/portfolio/sections/{section}", method= RequestMethod.GET)
    public ModelAndView getSection(@PathVariable("section") String sectionName,
    		Device device, HttpServletRequest request) throws TemplateException {
    	ModelAndView mav = null;
    	PageTemplateData data = new PageTemplateData();
  		String path;
    	try {
	  		mav = createModelAndView(device, request, data);
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
			TemplateException exception = new TemplateException(e);
			exception.setPath(data.getPath());
			throw exception;
		}
    }
    
    private String compileLatestLoaded(List<String> images) throws IOException {
    	StringBuilder sb = new StringBuilder();
    	for(String image : images) {
    		@SuppressWarnings("unused")
    		TemplateData data = new TemplateData() {
    			String imgPath = image;
    		};
    		sb.append(Util.compileTemplate("templates/misc/img/image", data));
    	}
    	return sb.toString();
    }
    
    private PageTemplateData loadPage(PageTemplateData data, String textPath, String pagePath) throws IOException {
        if(data == null || pagePath == null) {
        	throw new NullPointerException("Null passed as argument");
        }
    	if(textPath != null) {
    		if(data.getTextData() == null) {
    			data.setTextData(new TextTemplateData() {});
    		}
    		data.setText(Util.compileTemplate(textPath, data.getTextData()));
    	}
    	data.setPage(Util.compileTemplate(pagePath, data));
        return data;
    }
    
    private ModelAndView createModelAndView(Device device,
    		HttpServletRequest request, PageTemplateData data) {
    	StringBuilder ver = new StringBuilder("");
    	StringBuilder templatePath = new StringBuilder("");
    	if(request.getParameter("ver") != null) {
    		ver.append(request.getParameter("ver"));
    	}
    	if(device.isNormal() && ver.toString().equals("") 
    			|| ver.toString().equals("desktop")) {
    		data.setIsMobile(null);
	  	} else if((device.isMobile() && ver.toString().equals(""))
	  			|| (device.isTablet() && ver.toString().equals(""))
    			|| ver.toString().equals("mobile")) {
    		data.setIsMobile(true);
	  	} else {
	  		throw new NullPointerException("Cannot specify device! ver:"+ver);
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
