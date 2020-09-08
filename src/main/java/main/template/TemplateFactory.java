package main.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.album.Album;
import main.img.ImageData;
import main.template.data.TemplateData;

public class TemplateFactory {

	private TemplateFactory() {}
	
	//TODO refactor
	public static <T> Template buildTemplate(List<T> list) {
		if(list == null || list.size() < 1) {
			throw new IllegalArgumentException("List is empty");
		}
		T obj = list.get(0);
		if(obj instanceof ImageData) {
			return buildImageListTemplate((List<ImageData>)list);
		} else if(obj instanceof Album) {
			return buildAlbumListTemplate((List<Album>)list);
		} else {
			throw new IllegalArgumentException("Wrong type");
		}
	}
	
	private static Template buildImageListTemplate(List<ImageData> dataList) {
		ImageListTemplate template = new ImageListTemplate();
		List<String> urls = new ArrayList<>();
		dataList.forEach(d -> urls.add(d.getUrl()));
		template.setUrls(urls);
		return template;
	}
	
	private static Template buildAlbumListTemplate(List<Album> albums) {
		AlbumListTemplate template = new AlbumListTemplate();
		template.setAlbums(albums);
		return fillTemplate(template, "templates/misc/albums/albumList");
	}
	
	public static Template buildTemplate(Map<String, String> data,
			String templatePath) {
		EssentialTemplate<Map<String, String>> template = 
				new EssentialTemplate<>();
		template.setData(data);
		return fillTemplate(template, templatePath);
	}
	
	public static Template buildTemplate(TemplateData data,
			String templatePath) {
		EssentialTemplate<TemplateData> template = new EssentialTemplate<>();
		template.setData(data);
		return fillTemplate(template, templatePath);
	}
	
	public static Template buildTemplate(String templatePath) {
		Template template = new AbstractTemplate();
		return fillTemplate(template, templatePath);
	}
	
	private static Template fillTemplate(Template template,
			String templatePath) {
		template.setTemplatePath(formatPath(templatePath));
		return template;
	}
	
	private static String formatPath(String path) {
		if(!path.contains(".mustache")) {
			path += ".mustache";
		}
		return path;
	}
	
}
