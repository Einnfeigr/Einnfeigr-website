package main.dashboard;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import main.exception.ControllerException;
import main.exception.PreviewException;
import main.img.ImagePreviewController;
import main.misc.Util;
import main.section.SectionsController;
import main.template.EssentialTemplate;
import main.template.Template;
import main.template.data.page.PageTemplateData;

@RestController
public class DashboardController {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	ImageStorageService storageService;
	
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public ModelAndView showMain() {
		ModelAndView mav =  new ModelAndView("index");
		try {
			Template template = new EssentialTemplate(
					"templates/pages/dashboard/main");
			mav.getModel().put("page", template.compile());
			mav.getModel().put("title", "Панель управления");
			return mav;
		} catch (Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
		}
	}
	
	@RequestMapping(value="/dashboard/download/{filePath}",
			method=RequestMethod.GET)
	public ResponseEntity<Resource> downloadFile(
			@PathVariable String filePath) {
		Resource file = storageService.loadAsResource(filePath);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@RequestMapping(value="/dashboard/upload", method=RequestMethod.GET)
	public ModelAndView showUploadForm() {
		return new ModelAndView("pages/dashboard/upload");
	}
	
	@RequestMapping(value="/dashboard/upload", method=RequestMethod.POST)
	public ResponseEntity<String> uploadFile(
			@RequestParam("file") MultipartFile[] files,
			@RequestParam("path") String path) {
		if(path == null) {
			path = "/img/portfolio/sections/ретушь/";
		}
		try {
			Template pageTemplate = new EssentialTemplate("templates/index");
			PageTemplateData pageData = new PageTemplateData();
			Template template = new EssentialTemplate(
					"static/text/ru/dashboard/upload/success");
			pageData.setPage(template.compile());
			for(MultipartFile file : files) {
				storageService.store(file, path);
				
			}		
			try {
				SectionsController.loadSections();

			} catch (IOException e) {
				logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			}
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION)
					.body(pageTemplate.compile());
		} catch (Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			return ResponseEntity.badRequest().body(null);
		}

	}
}
