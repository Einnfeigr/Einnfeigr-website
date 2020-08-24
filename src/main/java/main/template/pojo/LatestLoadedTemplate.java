package main.template.pojo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import main.template.pojo.data.PageTemplateData;
import main.template.pojo.data.TemplateData;

public class LatestLoadedTemplate extends EssentialTemplate {

	private List<File> images;
	
	public LatestLoadedTemplate(String path, PageTemplateData data) {
		setData(data);
	}
	
	public LatestLoadedTemplate(String path) {
		setData(new PageTemplateData());
	}

	public List<File> getImages() {
		return images;
	}

	public void setImages(List<File> images) {
		this.images = images;
	}

	@Override
	public String compile() throws IOException {
		return super.compile();
	}
	
}
