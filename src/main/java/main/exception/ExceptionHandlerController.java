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
			String bodyOfResponse;
			String responseTemplatePath;
			Map<String, Object> map = new HashMap<>();
			if(ex instanceof ControllerException) {
				String textPath;
				if(ex instanceof NotFoundException) {
					textPath = "templates/text/ru/error/notFound";
				} else {
					textPath = "templates/text/ru/error/error";
				}
				map.put("text", TemplateFactory.buildTemplate(textPath));
			}
			if(request.getParameter("path") == null) {
				responseTemplatePath = "templates/index";
			} else {
				responseTemplatePath = "templates/placeholder";
			}
			String pagePath = "templates/pages/error/error";
			PageTemplateData responseData = new PageTemplateData();
			responseData.setTitle("Ошибка");
			responseData.setPage(TemplateFactory.buildTemplate(pagePath, map));
			bodyOfResponse = TemplateFactory.buildTemplate(responseTemplatePath,
					responseData.toMap());
		    return handleExceptionInternal(ex, bodyOfResponse, 
		  	      new HttpHeaders(), HttpStatus.OK, request);
		} catch (Exception e) {
			e.printStackTrace();
			return handleExceptionInternal(ex, "что-то пошло не так", 
			  	      new HttpHeaders(), HttpStatus.OK, request);
		}
	}
}