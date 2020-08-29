package main.misc;

import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import main.img.ImageDataController;
import main.img.ImagePreviewController;
import main.section.SectionsController;

@Component
public class Initializator implements InitializingBean {
	
	private ImageDataController imageController;
	
	public Initializator(ImageDataController imageController) {
		this.imageController = imageController;
	}
	
	@Override
	public void afterPropertiesSet() {
		/*try {
			imageController.loadImages();
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			SectionsController.loadSections();
		} catch(IOException e) {
			e.printStackTrace();
		}
		ImagePreviewController.generatePreviews(); */
	}	
}
