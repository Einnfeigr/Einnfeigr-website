package main.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import main.misc.Util;
import main.pojo.PageTemplateData;
import main.pojo.TemplateData;
import main.pojo.TextTemplateData;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {ControllerException.class, IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		try {
			String bodyOfResponse;
			String templatePath;
			PageTemplateData pageData = new PageTemplateData();
			if(ex instanceof ControllerException) {
				pageData.setText(Util.compileTemplate("static/text/ru/error",
						new TextTemplateData() {}));
				pageData.setPath(((ControllerException) ex).getPath());
			}
			if(request.getParameter("path") == null) {
				templatePath = "templates/index";
			} else {
				templatePath = "templates/placeholder";
			}
			String pagePath = "templates/pages/error";
			@SuppressWarnings("unused")
			TemplateData data = new TemplateData() {
				String title = "Ошибка";
				String path = pageData.getPath();
				String page = Util.compileTemplate(pagePath, pageData);
			};
			bodyOfResponse = Util.compileTemplate(templatePath, data);
		    return handleExceptionInternal(ex, bodyOfResponse, 
		  	      new HttpHeaders(), HttpStatus.OK, request);
		} catch (Exception e) {
			e.printStackTrace();
			return handleExceptionInternal(ex, "что-то пошло не так", 
			  	      new HttpHeaders(), HttpStatus.OK, request);
		}
	}
}