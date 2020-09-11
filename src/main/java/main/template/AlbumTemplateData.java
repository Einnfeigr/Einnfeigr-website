package main.template;

import main.template.data.TemplateData;

public class AlbumTemplateData implements TemplateData {

	String name;
	String images;
	String id;
	String albums;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getAlbums() {
		return albums;
	}
	
	public void setAlbums(String albums) {
		this.albums = albums;
	}
}
