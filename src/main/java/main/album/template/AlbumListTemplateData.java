package main.album.template;

import main.template.data.TemplateData;

public class AlbumListTemplateData implements TemplateData {

	String albums;
	
	public AlbumListTemplateData(String albums) {
		this.albums = albums;
	}
	
}
