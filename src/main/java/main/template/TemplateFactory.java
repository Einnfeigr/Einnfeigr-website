package main.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.album.Album;
import main.img.ImageData;
import main.misc.Util;

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

	public static String buildTemplate(String path, String...data) 
			throws IOException {
		return fillTemplate(new EssentialTemplate(Util.arrayToMap(data)), path)
				.compile();
	}
	
	public static String buildTemplate(String path, Map<String, Object> data) 
			throws IOException {
		return fillTemplate(new EssentialTemplate(data), path).compile();
	}

	public static String buildTemplate(String templatePath) throws IOException {
		return fillTemplate(new EssentialTemplate(), templatePath).compile();
	}
	
	private static Template fillTemplate(Template template, String path) {
		if(!path.contains(".mustache")) {
			path += ".mustache";
		}
		if(!path.contains("templates/")) {
			path = "templates/"+path;
		}
		template.setTemplatePath(path);
		return template;
	}
	
}
