package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.drive.DriveDao;

@RestController
public class CallbackReciever {

	@Autowired
	DriveDao driveDao;
	
	@RequestMapping("api/drive/callback")
	public void handleCallback() {
		driveDao.resetCache();
	}
}
