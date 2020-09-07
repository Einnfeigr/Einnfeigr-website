package main.album;

import java.util.ArrayList;
import java.util.List;

import main.img.ImageData;

public class Album {
	
	private List<ImageData> images;
	private List<Album> albums;
	private String id;
	private String name;
	
	public Album() {
		albums = new ArrayList<>();
	}
	
	public List<ImageData> getImages() {
		return images;
	}
	public void setImages(List<ImageData> images) {
		this.images = images;
	}
	public List<Album> getAlbums() {
		return albums;
	}
	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
	public void addAlbum(Album album) {
		albums.add(album);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
