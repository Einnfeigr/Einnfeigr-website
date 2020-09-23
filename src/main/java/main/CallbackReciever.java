package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.drive.dao.PortfolioDriveDao;
import main.drive.dao.PreviewDriveDao;
import main.img.preview.DriveImageController;

@RestController
public class CallbackReciever {

	private Logger logger = LoggerFactory.getLogger(DriveImageController.class);
	
	@Autowired
	PortfolioDriveDao portfolioDriveDao;
	
	@Autowired
	PreviewDriveDao previewDriveDao;
	
	@RequestMapping("api/drive/callback")
	public void handleCallback() {
		portfolioDriveDao.clearCache();
		previewDriveDao.clearCache();
	}
}
