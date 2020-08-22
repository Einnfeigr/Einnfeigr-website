package main.misc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import main.img.ImageDataController;
import main.section.SectionsController;

@Component
public class Initializator implements InitializingBean {
	
	private ImageDataController imageController;
	
	public Initializator(ImageDataController imageController) {
		this.imageController = imageController;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		imageController.loadImages();
    	SectionsController.loadSections();
	}	
}
