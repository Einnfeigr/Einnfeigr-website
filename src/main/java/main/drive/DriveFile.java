package main.drive;

import java.util.List;
import java.util.Map;

public class DriveFile {

	private String id;
	private String mimeType;
	private String title;
	private List<Map<String, String>> parents;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Map<String, String>> getParents() {
		return parents;
	}
	public void setParents(List<Map<String, String>> parents) {
		this.parents = parents;
	}
	
	public String getParentId() {
		if(parents.size() > 0) {
			return parents.get(0).get("id");
		} 
		return null;
	}
	public boolean isDirectory() {
		return mimeType.equals("application/vnd.google-apps.folder");
	}
	
}
