package main.template;

import java.io.IOException;
import java.util.List;

import main.album.Album;

public class AlbumListTemplate extends AbstractTemplate {
	
	private List<Album> albums;
		
	AlbumListTemplate() {}
	
	public void setAlbums(List<Album> albums) {
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
		Template albumsTemplate = TemplateFactory.buildTemplate(
				new AlbumListTemplateData(albumsContent.toString()),
				templatePath);
		return albumsTemplate.compile();
	}
}
