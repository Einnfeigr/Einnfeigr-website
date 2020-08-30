package main.template;

import java.io.IOException;

import main.section.Section;
import main.template.data.page.SectionPreviewTemplateData;

public class SectionPreviewTemplate extends SectionTemplate {

	public SectionPreviewTemplate(Section section) throws IOException {
		imageTemplatePath = "templates/misc/sections/preview";
		SectionPreviewTemplateData data = new SectionPreviewTemplateData();
		data.setId(section.getId());
		data.setName(section.getName());
		data.setImages(parseImages(section, 3));
		setData(data);
	}
	
}
