package main.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.album.Album;
import main.drive.dao.PortfolioDriveDao;
import main.img.ImageData;
import main.template.data.ImageTemplateData;

public class AlbumTemplate extends EssentialTemplate {
	
	protected String imageTemplatePath = "templates/misc/img/image";
	
	protected AlbumTemplate() {
		setTemplatePath("templates/misc/albums/album.mustache");
	}
	
	public AlbumTemplate(Album album) throws IOException {
		Map<String, String> data = new HashMap<>();
		setTemplatePath("templates/misc/albums/album.mustache");
		data.put("name", album.getName());
		data.put("id", album.getId());
		data.put("images", parseImages(album));
		List<Album> albums = album.getAlbums();
		if(albums != null && albums.size() > 0) {
			Template template = TemplateFactory.createAlbumListTemplate(
					album.getAlbums());
			data.put("albums", template.compile());
		}
		if(album.getParent() != null 
				&& !album.getParent().equals(PortfolioDriveDao.getRootId())) {
			data.put("back", "/portfolio/albums/"+album.getParent());
		}
		if(album.getDescription() != null) {
			data.put("descrption", album.getDescription());
		}
		setData(data);
	}	
	
	protected String parseImages(Album album) throws IOException {
		return parseImages(album, album.getImages().size());
	}
	
	protected String parseImages(Album album, int count) 
			throws IOException {
		StringBuilder images = new StringBuilder();
		List<ImageData> imagesList = album.getImages();
		for(int x = count-1; x >= 0; x--) {
			String url;
			if(x >= imagesList.size()) {
				url = "/img/placeholder.png";
			} else {
				url = "/image/"+(imagesList.get(x).getId());
			}
			Template template = TemplateFactory.buildTemplate(
					imageTemplatePath, new ImageTemplateData(url).toMap());
			images.append(template.compile());
		} 
		return images.toString(); 
	}
	
}
