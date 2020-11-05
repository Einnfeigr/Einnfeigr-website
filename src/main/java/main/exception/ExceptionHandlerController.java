package main.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import main.template.Template;
import main.template.TemplateFactory;
import main.template.data.PageTemplateData;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {
			ControllerException.class,
			IllegalArgumentException.class,
			IllegalStateException.class })
	public ResponseEntity<Object> handleConflict(RuntimeException ex,
			WebRequest request) {
		try {
			Template template;
			String bodyOfResponse;
			String responseTemplatePath = "templates/index";
			Map<String, String> map = new HashMap<>();
			if(ex instanceof ControllerException) {
				String textPath;
				if(ex instanceof NotFoundException) {
					textPath = "templates/text/ru/error/notFound";
				} else {
					textPath = "templates/text/ru/error/error";
				}
				template = TemplateFactory.buildTemplate(textPath);
				map.put("text", template.compile());
			}
			if(request.getParameter("path") == null) {
				responseTemplatePath = "templates/index";
			} else {
				responseTemplatePath = "templates/placeholder";
			} 
			String pagePath = "templates/pages/error/error";
			template = TemplateFactory.buildTemplate(pagePath, map);
			PageTemplateData responseData = new PageTemplateData();
			responseData.setTitle("Ошибка");
			responseData.setPage(template.compile());
			template = TemplateFactory.buildTemplate(responseTemplatePath,
					responseData.toMap());
			bodyOfResponse = template.compile();
		    return handleExceptionInternal(ex, bodyOfResponse, 
		  	      new HttpHeaders(), HttpStatus.OK, request);
		} catch (Exception e) {
			e.printStackTrace();
			return handleExceptionInternal(ex, "что-то пошло не так", 
			  	      new HttpHeaders(), HttpStatus.OK, request);
		}
	}
}