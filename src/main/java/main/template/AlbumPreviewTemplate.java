package main.template;

import java.io.IOException;

import main.album.Album;
import main.template.data.page.AlbumPreviewTemplateData;

public class AlbumPreviewTemplate extends AlbumTemplate {

	public AlbumPreviewTemplate(Album album) throws IOException {
		imageTemplatePath = "templates/misc/albums/preview";
		AlbumPreviewTemplateData data = new AlbumPreviewTemplateData();
		data.setId(album.getId());
		data.setName(album.getName());
		data.setImages(parseImages(album, 3));
		setData(data);
	}
	
}
