package main.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.album.Album;

public class AlbumListTemplate extends AbstractTemplate {
	
	private List<Album> albums;
		
	AlbumListTemplate() {}
	
	AlbumListTemplate(List<Album> albums) {
		this.albums = albums;
	}
	
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
		Map<String, String> map = new HashMap<>();
		map.put("albums", albumsContent.toString());
		Template albumsTemplate = TemplateFactory.buildTemplate(path, map);
		return albumsTemplate.compile();
	}
}
