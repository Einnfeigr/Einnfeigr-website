package main.img;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import main.drive.DriveDao;
import main.misc.Util;

public class ImageDataController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImageDataController.class);
	
	List<ImageData> images;

	@Autowired
	private DriveDao dao;
	
	public ImageDataController() {
		update();
	}

	private void update() {
		try {
			images = dao.getAllFiles();
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		} finally {
			if(images == null) {
				images = new ArrayList<>();
			}
		}
	}
	
	public List<File> getLatestImages() {
		List<File> files = new ArrayList<>();
		try {
			images.forEach(d -> files.add(new File(
					"https://drive.google.com/uc?id="+d.getId()
					+"&export=download")));
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		}
		return files;
	}
	
	public void loadImages() throws IOException { 
		/* List<ImageData> indexedImages;
		File file = new File(
				Util.toAbsoluteUrl("static/img/portfolio/sections"));
		indexedImages = parseImagesData(file);
		if(indexedImages != null) {
			logger.info("indexed "+indexedImages.size()+" images");
		} else {
			logger.warn("indexed 0 images");
		} */
	}
	
}
