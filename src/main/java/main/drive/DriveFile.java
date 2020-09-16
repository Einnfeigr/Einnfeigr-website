package main.drive;

import java.util.List;
import java.util.Map;

public class DriveFile {

	private String id;
	private String title;
	private String mimeType;
	private Boolean isDirectory;
	private List<Map<String, String>> parents;
	private List<DriveFile> children;
	
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
	public List<DriveFile> getChildren() {
		if(!isDirectory) { 
			throw new IllegalStateException("File is not folder");
		}
		return children;
	}
	public void setChildren(List<DriveFile> children) {
		if(!isDirectory) { 
			throw new IllegalStateException("File is not folder");
		}
		this.children = children;
	}
	public String getParentId() {
		if(parents != null && parents.size() > 0) {
			return parents.get(0).get("id");
		} 
		return null;
	}
	public boolean isDirectory() {
		if(isDirectory == null) {
			isDirectory = mimeType.equals("application/vnd.google-apps.folder");
		}
		return isDirectory;
	}
	
}
