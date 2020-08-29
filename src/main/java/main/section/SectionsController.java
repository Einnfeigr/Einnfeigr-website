package main.section;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import main.misc.Util;

public class SectionsController {
	
	private static HashMap<String, Section> sections;
	
	private SectionsController() {};
	
	public static Map<String, Section> getSections() {
		return sections;
	}
	
	public static Section getSection(String name) throws IOException {
		if(sections == null) {
			loadSections();
		}
		name = "/static/img/sections/"+name;
		if(!sections.containsKey(name)) {
			throw new IOException(name+" section can't be found");
		}
		return sections.get(name);
	}
	
	public static void loadSections() throws IOException {
		Section main = new Section(Util.getFile(
				"static/img/sections"));
		sections = new HashMap<>();
		addSections(main);
	}
	
	private static void addSections(Section section) {
		if(section.getSections().isEmpty()) {
			return;
		}
		section.getSections().forEach((k, v) -> {
			sections.put(k, v);
			addSections(v);
		});
	}
}