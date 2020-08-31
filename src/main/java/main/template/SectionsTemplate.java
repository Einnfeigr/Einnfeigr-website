package main.template;

import java.io.IOException;
import java.util.List;

import main.section.Section;
import main.template.data.SectionsTemplateData;

public class SectionsTemplate extends EssentialTemplate {
	
	private List<Section> sections;
		
	public SectionsTemplate(List<Section> sections) {
		this.sections = sections;
	}
	
	@Override
	public String compile() throws IOException {
		StringBuilder sectionsContent = new StringBuilder("");
		for(Section section : sections) {
			SectionTemplate sectionTemplate = 
					new SectionPreviewTemplate(section);
			sectionsContent.append(sectionTemplate.compile());
		}
		Template sectionsTemplate = new EssentialTemplate();
		sectionsTemplate.setTemplatePath("templates/misc/sections/sections");
		sectionsTemplate.setData(new SectionsTemplateData(sectionsContent
				.toString()));
		return sectionsTemplate.compile();
	}
}
