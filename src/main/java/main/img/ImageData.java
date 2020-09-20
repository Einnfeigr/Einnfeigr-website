package main.img;

import main.MediaData;
import main.drive.DriveUtils;

public class ImageData implements MediaData {

	private String id;
	private String title;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String name) {
		this.title = name;
	}
	public String getUrl() {
		return DriveUtils.getDownloadUrl(id);
	}
}
