package main.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.album.Album;
import main.img.ImageData;
import main.template.data.TemplateData;

public class TemplateFactory {

	private TemplateFactory() {}
	
	public static Template createImageListTemplate(List<ImageData> dataList) {
		List<String> urls = new ArrayList<>();
		dataList.forEach(d -> urls.add(d.getUrl()));
		return new ImageListTemplate(urls);
	}
	
	public static Template createAlbumListTemplate(List<Album> albums) {
		return fillTemplate(new AlbumListTemplate(albums),
				"templates/misc/albums/albumList");
	}
	
	public static Template buildTemplate(String path, Map<String,String> data) {
		return fillTemplate(new EssentialTemplate<>(data), path);
	}
	
	public static Template buildTemplate(String path, TemplateData data) {
		return fillTemplate(new EssentialTemplate<>(data), path);
	}
	
	public static Template buildTemplate(String templatePath) {
		return fillTemplate(new AbstractTemplate(), templatePath);
	}
	
	private static Template fillTemplate(Template template, String path) {
		if(!path.contains(".mustache")) {
			path += ".mustache";
		}
		template.setTemplatePath(path);
		return template;
	}
	
}
