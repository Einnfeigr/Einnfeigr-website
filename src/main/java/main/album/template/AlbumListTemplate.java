package main.album.template;

import java.io.IOException;
import java.util.List;

import main.album.Album;
import main.template.AbstractTemplate;
import main.template.EssentialDataTemplate;
import main.template.Template;

public class AlbumListTemplate extends AbstractTemplate {
	
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
		Template albumsTemplate = new EssentialDataTemplate(
				new AlbumListTemplateData(albumsContent.toString()));
		albumsTemplate.setTemplatePath("templates/misc/albums/albumList");
		return albumsTemplate.compile();
	}
}
