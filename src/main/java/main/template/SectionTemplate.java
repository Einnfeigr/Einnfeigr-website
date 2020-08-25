package main.template;

import java.io.File;
import java.io.IOException;
import java.util.List;

import main.section.Section;
import main.template.data.ImageTemplateData;
import main.template.data.SectionTemplateData;

public class SectionTemplate extends EssentialTemplate {
	
	protected String imageTemplatePath = "templates/misc/img/image";
	
	protected SectionTemplate() {
		setTemplatePath("templates/misc/sections/section");
	}
	
	public SectionTemplate(Section section) throws IOException {
		setTemplatePath("templates/misc/sections/section");
		SectionTemplateData data = new SectionTemplateData();
		data.setName(section.getName());
		data.setImages(parseImages(section, section.getImages().size()));
		setData(data);
	}	
	
	protected String parseImages(Section section, int count) throws IOException {
		StringBuilder images = new StringBuilder();
		List<File> imagesList = section.getImages();
		for(int x = 0; x < count; x++) {
			if(x >= imagesList.size()) {
				break;
			}
			Template template = new EssentialTemplate();
			template.setTemplatePath(imageTemplatePath);
			template.setData(new ImageTemplateData(imagesList.get(x)));
			images.append(template.compile());
		}
		return images.toString();
	}
	
}
