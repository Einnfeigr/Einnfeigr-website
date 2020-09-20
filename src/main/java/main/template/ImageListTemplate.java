package main.template;

import java.io.IOException;
import java.util.List;

import main.template.data.ImageTemplateData;

public class ImageListTemplate extends AbstractTemplate {
	
	private List<String> urls;
	
	ImageListTemplate() {}

	ImageListTemplate(List<String> urls) {
		this.urls = urls;
	}
	
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	@Override
	public String compile() throws IOException {
		if(urls == null || urls.size() == 0) {
			throw new IllegalArgumentException("Empty paths list");
		}
		StringBuilder sb = new StringBuilder("");
		for(String url : urls) {
			
			Template template = TemplateFactory.buildTemplate(
					 "templates/misc/img/image", new ImageTemplateData(url));
			sb.append(template.compile());
		}
		return sb.toString();
	}
	
}
