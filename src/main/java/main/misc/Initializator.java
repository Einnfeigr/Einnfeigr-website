package main.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import main.img.ImagePreviewController;

@Component
public class Initializator implements InitializingBean {

	private final static Logger logger = 
			LoggerFactory.getLogger(Initializator.class);
	
	@Override
	public void afterPropertiesSet() {
		try {
			ImagePreviewController.generatePreviews(); 
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		}
	}	
}
