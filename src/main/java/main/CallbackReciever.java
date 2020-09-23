package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.album.AlbumController;
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
	@Autowired
	AlbumController albumController;
	
	@RequestMapping("api/drive/callback")
	public ResponseEntity<String> handleCallback() {
		portfolioDriveDao.clearCache();
		previewDriveDao.clearCache();
		albumController.loadAlbums();
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}
