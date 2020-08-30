package main.misc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import main.img.ImagePreviewController;

@Component
public class Initializator implements InitializingBean {
	
	@Override
	public void afterPropertiesSet() {
		ImagePreviewController.generatePreviews(); 
	}	
}
