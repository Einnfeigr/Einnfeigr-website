package com.example.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exception.TemplateException;
import com.example.demo.pojo.PageTemplateData;
import com.example.demo.pojo.TextTemplateData;
import com.example.demo.section.SectionsController;

@RestController
public class PageController {
	
    @RequestMapping(value= {"/", "/portfolio", "/retouch", "/about"}, method= RequestMethod.GET)
    ModelAndView getPage(HttpServletRequest request) throws TemplateException {
    	ModelAndView mav = null;
    	PageTemplateData data = new PageTemplateData();
  		String path;
  		if(request.getParameter("target") != null) {
    		if(request.getParameter("target").equals("body")) {
    			mav = new ModelAndView("placeholder");
    		}
        	if(request.getParameter("path") != null ) {
        		data.setPath(request.getParameter("path"));
        	}
    	} else {
    		mav = new ModelAndView("index");
    	}
    	try {  
      		String requestUrl = request.getRequestURI();
      		if(requestUrl.endsWith("/") && requestUrl.length() > 1) {
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
	    			data.setText(SectionsController.compileSection(SectionsController.getSection("ретушь"), path));
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
		} catch (IOException e) {
			e.printStackTrace();
			TemplateException exception = new TemplateException(e.getMessage());
			exception.setPath(data.getPath());
			throw exception;
		}
        return mav;	
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
}
