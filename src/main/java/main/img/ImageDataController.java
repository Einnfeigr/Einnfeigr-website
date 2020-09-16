package main.img;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.drive.dao.PortfolioDriveDao;
import main.misc.Util;

public class ImageDataController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImageDataController.class);
	
	private List<ImageData> dataList;
	private List<ImageData> latest;

	private static final int LATEST_COUNT = 10;
	
	private PortfolioDriveDao dao;
	
	public ImageDataController(PortfolioDriveDao dao) {
		this.dao = dao;
		update();
	}

	public void update() {
		try {
			dataList = dao.getAllFiles();
			if(latest == null) {
				latest = new ArrayList<>();
			} else {
				latest.clear();
			}
			for(int x = 0; x < LATEST_COUNT; x++) {
				if(x >= dataList.size()) {
					break;
				}
				latest.add(dataList.get(x));
			}
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		} finally {
			if(dataList == null) {
				dataList = new ArrayList<>();
			}
		}
	}
	
	public List<ImageData> getLatestImages() {
		return latest;
	}
	
}
