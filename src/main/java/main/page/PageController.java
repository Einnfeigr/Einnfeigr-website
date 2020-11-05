package main.page;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.album.Album;
import main.album.AlbumController;
import main.exception.ControllerException;
import main.exception.NotFoundException;
import main.exception.TemplateException;
import main.img.ImageData;
import main.img.ImageDataController;
import main.misc.Util;
import main.template.AlbumTemplate;
import main.template.Template;
import main.template.TemplateFactory;

@RestController
public class PageController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(PageController.class);
	
	private final Map<String, String> names = new HashMap<>();
	
	@Autowired
	ImageDataController dataController;
	@Autowired 
	AlbumController albumController;
	
	public PageController() {
		updateNames();
	}
	
	public void updateNames() {
		try {
			Resource resource = new ClassPathResource("pages.properties");
			BufferedReader input = new BufferedReader(new InputStreamReader(
					resource.getInputStream(), "UTF-8"));
			Properties props = new Properties();
			props.load(input);
			props.entrySet().stream().forEach(e -> names.put(
					String.valueOf(e.getKey()), String.valueOf(e.getValue())));
		} catch (IOException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		}
	}
	
    @RequestMapping(value= {"/{page}", "/"}, method= RequestMethod.GET,
    		produces = "application/json", headers = "target=body")
    public ResponseEntity<Page> getPageBody(Device device, 
    		@RequestParam(required=false) String ver,
    		@PathVariable(value="page",required=false) Optional<String> pageName
    				) throws TemplateException {
    	try {
    		String name;
    		if(pageName.isPresent()) {
    			name = pageName.get();
    		} else {
    			name = "main";
    		}
	    	Page page = new Page();
	    	Map<String, String> data = getPageData(name, isMobile(device, ver));
	    	page.setTitle(data.get("title"));
	    	page.setContent(data.get("page"));
	    	return new ResponseEntity<Page>(page, HttpStatus.OK);
    	} catch(Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
    	}
    }
    
    @RequestMapping(value= {"/{page}", "/", "/tips/{tip}"}, method= RequestMethod.GET)
    public ModelAndView getPage(Device device, 
    		@RequestParam(required=false) String ver, 
    		@PathVariable(required=false) Optional<String> page,
    		@PathVariable(required=false) Optional<String> tip) 
    				throws ControllerException {
    	try {
    		String name;
    		if(page.isPresent()) {
    			name = page.get();
    		} else if(!tip.isPresent()) {
    			name = "main";
    		} else {
    			name = "tips/"+tip.get();
    		}
	  		ModelAndView mav = new ModelAndView("index");
      		Map<String, String> data = getPageData(name, isMobile(device, ver));
      		mav.getModel().putAll(data);
	    	return mav;
    	} catch(NotFoundException e) {
    		throw e;
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
		}
    }
    
    private Map<String, String> getPageData(String page, boolean isMobile)
    		throws IOException {
    	if(!names.containsKey(page)) {
	    	logger.error("Invalid page address: "+page);
			throw new NotFoundException("Invalid page address: "+page);
    	}
    	String pagePath;
    	Template textTemplate = null;
    	Map<String, String> data = new HashMap<>();
    	if(isMobile) {
    		data.put("isMobile", "true");
    	}
    	if(page.equals("main")) {
	    	data.put("page", compileMain(data));	
    	}
    	if(page.equals("portfolio")) {
	    	textTemplate = TemplateFactory.createAlbumListTemplate(
	    			albumController.getRootAlbums());
    	}
    	if(textTemplate == null) {
    		textTemplate = TemplateFactory.buildTemplate(
    				"templates/text/ru/"+page, data);
    	}
    	pagePath = "templates/pages/"+page;
		if(!data.containsKey("page")) {
    		data.put("text", textTemplate.compile());
	    	data.put("page", TemplateFactory.buildTemplate(pagePath, data)
	    			.compile());
    	}
    	data.put("title", names.get(page));
    	return data;
    }
    
    @RequestMapping(value= "/portfolio/albums/{id}",
    		method = RequestMethod.GET, produces = "application/json",
    		headers = "target=body")
    public ResponseEntity<Page> getAlbumBody(Device device, 
    		@PathVariable() String id,
    		@RequestParam(required=false) String ver) throws TemplateException {
    	try {
	    	Page page = new Page();
	    	Album album = albumController.getAlbum(id);
	    	Template template = new AlbumTemplate(album);
	    	page.setTitle(album.getName());
	    	page.setContent(template.compile());
	    	return new ResponseEntity<Page>(page, HttpStatus.OK);
    	} catch(Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
    	}
    }
    
    @RequestMapping(value= "/portfolio/albums/{id}", 
    		method= RequestMethod.GET)
    public ModelAndView getAlbum(Device device,
    		@PathVariable() String id,
    		@RequestParam(required=false) String ver) throws TemplateException {
    	ModelAndView mav = null;
      	try {
      		mav = new ModelAndView("index");
	    	Album album = albumController.getAlbum(id);
	    	Template template = new AlbumTemplate(album);
	    	mav.getModel().put("name", album.getName());
	        mav.getModel().put("title", album.getName());
			mav.getModel().put("page", template.compile());
			mav.getModel().put("isMobile", isMobile(device, ver));
			return mav;
    	} catch (Exception e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			ControllerException exception = new ControllerException(e);
			throw exception;
		}
    }
    
    private String compileMain(Map<String, String> data) {
    	Template template;
		try {
    		Map<String, String> textData = new HashMap<>();
    		Map<String, String> pageData = data;
			List<ImageData> dataList = dataController.getLatestImages();
			if(dataList != null && dataList.size() > 0) {
				template = TemplateFactory.createImageListTemplate(dataList);
				textData.put("latest", template.compile());
			}
			StringBuilder sinceCreated = new StringBuilder();
			LocalDate creationDate = LocalDate.of(2019, 2, 8);
			Period period = Period.between(creationDate, LocalDate.now());
			int days = period.getDays();
			if(period.getMonths() > 0) {
				days += period.getMonths()*31;
			}
			if(period.getYears() > 0) {
				days += period.getYears()*31;
			}
			sinceCreated.append(days);
			switch(sinceCreated.toString().charAt(sinceCreated.length()-1)) {
				case('1'):
					sinceCreated.append(" день");
					break;
				case('2'):
				case('3'):
				case('4'):
					sinceCreated.append(" дня");
					break;
				default:
					sinceCreated.append(" дней");
					break;
			}
			textData.put("sinceCreated", sinceCreated.toString());
    		template = TemplateFactory.buildTemplate(
    				"templates/text/ru/main", textData);
    		pageData.put("text", template.compile());
    		template = TemplateFactory.buildTemplate(
    				"templates/pages/main", pageData);
    		return template.compile();
    	} catch(IOException | IllegalArgumentException e) {
    		logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
        	return null;
        }
    }
 
    private boolean isMobile(Device device, String ver) {
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
