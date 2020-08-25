package main.template;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import main.section.Section;
import main.template.data.SectionsTemplateData;

public class SectionsTemplate extends EssentialTemplate {
	
	private Map<String, Section> sections;
		
	public SectionsTemplate(Map<String, Section> sections) {
		this.sections = sections;
	}
	
	@Override
	public String compile() throws IOException {
		StringBuilder sectionsContent = new StringBuilder("");
		for(Entry<String, Section> entry : sections.entrySet()) {
			SectionTemplate sectionTemplate = new SectionPreviewTemplate(
					entry.getValue());
			sectionsContent.append(sectionTemplate.compile());
		}
		Template sectionsTemplate = new EssentialTemplate();
		sectionsTemplate.setTemplatePath("templates/misc/sections/sections");
		sectionsTemplate.setData(new SectionsTemplateData(sectionsContent
				.toString()));
		return sectionsTemplate.compile();
	}
}
