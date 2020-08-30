package main.template;

import java.io.IOException;
import java.util.List;

import main.img.ImageData;
import main.misc.Util;
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
		data.setId(section.getId());
		data.setImages(parseImages(section, section.getImages().size()));
		setData(data);
	}	
	
	protected String parseImages(Section section, int count) 
			throws IOException {
		StringBuilder images = new StringBuilder();
		
		List<ImageData> imagesList = section.getImages();
		for(int x = count-1; x >= 0; x--) {
			Template template = new EssentialTemplate();
			template.setTemplatePath(imageTemplatePath);
			String url;
			if(x >= imagesList.size()) {
				url = "/img/placeholder.jpg";
			} else {
				url = Util.getDownloadUrl(imagesList.get(x));
			}
			template.setData(new ImageTemplateData(url));
			images.append(template.compile());
		} 
		return images.toString(); 
	}
	
}
