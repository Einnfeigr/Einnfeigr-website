package main.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import main.template.TemplateController;
import main.template.data.TemplateData;
import main.template.data.page.PageTemplateData;
import main.template.data.text.TextTemplateData;

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
			String templatePath;
			PageTemplateData pageData = new PageTemplateData();
			TextTemplateData textData = new TextTemplateData() {};
			if(ex instanceof ControllerException) {
				if(ex instanceof NotFoundException) {
					pageData.setText(TemplateController.compileTemplate(
							"static/text/ru/error/notFound", textData));
				} else {
					pageData.setText(TemplateController.compileTemplate(
							"static/text/ru/error/error", textData));
				}
				pageData.setPath(((ControllerException) ex).getPath());
			}
			if(request.getParameter("path") == null) {
				templatePath = "templates/index";
			} else {
				templatePath = "templates/placeholder";
			}
			String pagePath;
			if(ex instanceof NotFoundException) {
				pagePath = "templates/pages/error/notFound";
			} else {
				pagePath = "templates/pages/error/error";
			}
			@SuppressWarnings("unused")
			TemplateData data = new TemplateData() {
				String title = "Ошибка";
				String path = pageData.getPath();
				String page = TemplateController.compileTemplate(pagePath, pageData);
			};
			bodyOfResponse = TemplateController.compileTemplate(templatePath, data);
		    return handleExceptionInternal(ex, bodyOfResponse, 
		  	      new HttpHeaders(), HttpStatus.OK, request);
		} catch (Exception e) {
			e.printStackTrace();
			return handleExceptionInternal(ex, "что-то пошло не так", 
			  	      new HttpHeaders(), HttpStatus.OK, request);
		}
	}
}