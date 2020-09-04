package main.img;

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
	
	private List<ImageData> dataList;

	private DriveDao dao;
	
	public ImageDataController(DriveDao dao) {
		this.dao = dao;
		update();
	}

	public void update() {
		try {
			dataList = dao.getAllFiles();
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		} finally {
			if(dataList == null) {
				dataList = new ArrayList<>();
			}
		}
	}
	
	public List<String> getLatestImages() {
		List<String> files = new ArrayList<>();
		try {
			dataList.forEach(d -> files.add(DriveUtils.getDownloadUrl(
					d.getId())));
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		}
		return files;
	}
	
}
