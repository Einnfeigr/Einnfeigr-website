package main.template;

import java.io.File;
import java.io.IOException;
import java.util.List;

import main.template.data.ImageTemplateData;

public class ImageListTemplate extends EssentialTemplate {

	private List<File> images;
	
	public ImageListTemplate(List<File> images) {
		this.images = images;
	}

	public List<File> getImages() {
		return images;
	}

	public void setImages(List<File> images) {
		this.images = images;
	}

	@Override
	public String compile() throws IOException {
		StringBuilder sb = new StringBuilder("");
		for(File image : images) {
			Template template = new EssentialTemplate() {};	
			template.setTemplatePath("templates/misc/img/image");
			template.setData(new ImageTemplateData(image, path));
			sb.append(template.compile());
		}
		return sb.toString();
	}
	
}
