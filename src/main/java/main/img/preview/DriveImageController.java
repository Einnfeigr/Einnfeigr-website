package main.img.preview;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.drive.DriveUtils;
import main.http.RequestBuilder;
import main.misc.Util;

@RestController
public class DriveImageController implements ImageController {

	private Logger logger = LoggerFactory.getLogger(DriveImageController.class);
	private Map<String, String> cache = new HashMap<>();
	private boolean isCachingUnavailable;
	
	@Override
	public ModelAndView getPreview(String size, String id) {
		return null;
	}

	@Override 
	public ModelAndView getImage(@PathVariable String id) {
		String content;
		if(isCachingUnavailable) {
			return new ModelAndView("redirect:"+
					DriveUtils.getClientDownloadUrl(id));
		}
		if(cache.containsKey(id)) {
			content = cache.get(id);
		} else {
			String url;
			try {
				url = DriveUtils.getServerDownloadUrl(id); 
				content = RequestBuilder.performGet(url);
				cache.put(id, content);	
			} catch(IOException e) {
				isCachingUnavailable = true;
				logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
				return new ModelAndView("redirect:"+
						DriveUtils.getClientDownloadUrl(id));
			}	
		}
		ModelAndView mav = new ModelAndView();
		mav.getModel().put("body", content);
		return mav;		
	}
}
