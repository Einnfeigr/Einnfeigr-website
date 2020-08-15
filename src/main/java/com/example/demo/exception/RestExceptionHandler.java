package com.example.demo.exception;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.Util;
import com.example.demo.pojo.PageTemplateData;
import com.example.demo.pojo.TemplateData;
import com.samskivert.mustache.MustacheException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {TemplateException.class, IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		try {
			String bodyOfResponse;
			String templatePath;
			PageTemplateData pageData = new PageTemplateData();
			if(ex instanceof TemplateException) { 
				pageData.setPath(((TemplateException) ex).getPath());
			} else {
				throw new IOException(ex);
			}
			if(request.getParameter("path") == null) {
				templatePath = Util.toAbsoluteUrl("templates/index.mustache");
			} else {
				templatePath = Util.toAbsoluteUrl("templates/placeholder.mustache");
			}
			String pagePath = Util.toAbsoluteUrl("templates/pages/error.mustache");
			@SuppressWarnings("unused")
			TemplateData data = new TemplateData() {
				String title = "Ошибка";
				String path = pageData.getPath();
				String page = Util.compileTemplate(pagePath, pageData);
			};
			bodyOfResponse = Util.compileTemplate(templatePath, data);
		    return handleExceptionInternal(ex, bodyOfResponse, 
		  	      new HttpHeaders(), HttpStatus.OK, request);
		} catch (IOException | MustacheException e) {
			e.printStackTrace();
		}
		return null;
	}
}