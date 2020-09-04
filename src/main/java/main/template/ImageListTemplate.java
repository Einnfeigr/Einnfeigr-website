package main.template;

import java.io.IOException;
import java.util.List;

import main.template.data.ImageTemplateData;

public class ImageListTemplate extends EssentialTemplate {
	
	private List<String> paths;
	
	public ImageListTemplate(List<String> paths) {
		this.paths = paths;
	}

	public List<String> getImages() {
		return paths;
	}

	public void setImages(List<String> paths) {
		this.paths = paths;
	}

	@Override
	public String compile() throws IOException {
		if(paths == null || paths.size() == 0) {
			throw new IllegalArgumentException("Empty paths list");
		}
		StringBuilder sb = new StringBuilder("");
		for(String path : paths) {
			Template template = new EssentialTemplate();	
			template.setTemplatePath("templates/misc/img/image");
			template.setData(new ImageTemplateData(path.toString()));
			sb.append(template.compile());
		}
		return sb.toString();
	}
	
}
