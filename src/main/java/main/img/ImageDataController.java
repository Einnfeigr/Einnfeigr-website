package main.img;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.drive.DriveDao;
import main.drive.DriveUtils;
import main.misc.Util;

public class ImageDataController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImageDataController.class);
	
	private List<ImageData> images;

	private DriveDao dao;
	
	public ImageDataController(DriveDao dao) {
		this.dao = dao;
		update();
	}

	public void update() {
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
			images.forEach(d -> files.add(
					new File(DriveUtils.getDownloadUrl(d))));
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		}
		return files;
	}
	
}
