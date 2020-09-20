package main.img.preview;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import main.drive.DriveUtils;
import main.http.RequestBuilder;
import main.misc.Util;

@RestController
public class DriveImageController implements ImageController {

	private Logger logger = LoggerFactory.getLogger(DriveImageController.class);
	private Map<String, String> cache = new HashMap<>();
	
	@Override
	public byte[] getPreview(String size, String id) {
		return null;
	}

	@Override 
	public byte[] getImage(@PathVariable String id) {
		String content;
		if(cache.containsKey(id)) {
			content = cache.get(id);
		} else {
			String url;
			try {
				url = DriveUtils.getServerDownloadUrl(id); 
				content = RequestBuilder.performGet(url);
				cache.put(id, content);	
				return content.getBytes(Charset.defaultCharset()); 
			} catch(IOException e) {
				logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			}	
		}
		return null;
	}
}
