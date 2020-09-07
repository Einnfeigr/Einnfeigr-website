package main.template;

import java.io.IOException;
import java.util.List;

import main.album.Album;
import main.template.data.AlbumListTemplateData;

public class AlbumListTemplate extends EssentialTemplate {
	
	private List<Album> albums;
		
	public AlbumListTemplate(List<Album> albums) {
		this.albums = albums;
	}
	
	@Override
	public String compile() throws IOException {
		StringBuilder albumsContent = new StringBuilder("");
		for(Album album : albums) {
			AlbumTemplate albumTemplate = 
					new AlbumPreviewTemplate(album);
			albumsContent.append(albumTemplate.compile());
		}
		Template albumsTemplate = new EssentialTemplate();
		albumsTemplate.setTemplatePath("templates/misc/albums/albumList");
		albumsTemplate.setData(new AlbumListTemplateData(albumsContent
				.toString()));
		return albumsTemplate.compile();
	}
}
