package main.template;

import java.io.IOException;

import main.album.Album;

public class AlbumPreviewTemplate extends AlbumTemplate {

	public AlbumPreviewTemplate(Album album) throws IOException {
		super(album);
		data.remove("images");
		data.put("id", album.getId());
		data.put("name", album.getName());
		data.put("previews", parseImages(album, 3));
		imageTemplatePath = "templates/misc/albums/preview";
	}
	
}
