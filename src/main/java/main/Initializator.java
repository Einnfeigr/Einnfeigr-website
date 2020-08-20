package main;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.img.ImageInfoController;

@Component
public class Initializator implements InitializingBean {
	
	@Autowired
	ImageInfoController imageController;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		imageController.loadImages();
	}	
}
