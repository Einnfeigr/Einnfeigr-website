package main.template;

import java.io.IOException;
import java.util.List;

import main.album.Album;
import main.drive.DriveUtils;
import main.img.ImageData;
import main.template.data.ImageTemplateData;
import main.template.data.AlbumTemplateData;

public class AlbumTemplate extends EssentialTemplate {
	
	protected String imageTemplatePath = "templates/misc/img/image";
	
	protected AlbumTemplate() {
		setTemplatePath("templates/misc/albums/album");
	}
	
	public AlbumTemplate(Album album) throws IOException {
		setTemplatePath("templates/misc/albums/album");
		AlbumTemplateData data = new AlbumTemplateData();
		data.setName(album.getName());
		data.setId(album.getId());
		data.setImages(parseImages(album, album.getImages().size()));
		setData(data);
	}	
	
	protected String parseImages(Album album, int count) 
			throws IOException {
		StringBuilder images = new StringBuilder();
		List<ImageData> imagesList = album.getImages();
		for(int x = count-1; x >= 0; x--) {
			Template template = new EssentialTemplate();
			template.setTemplatePath(imageTemplatePath);
			String url;
			if(x >= imagesList.size()) {
				url = "/img/placeholder.png";
			} else {
				url = DriveUtils.getDownloadUrl(imagesList.get(x).getId());
			}
			template.setData(new ImageTemplateData(url));
			images.append(template.compile());
		} 
		return images.toString(); 
	}
	
}
