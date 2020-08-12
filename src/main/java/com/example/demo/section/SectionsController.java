package com.example.demo.section;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.example.demo.Util;

public class SectionsController {
	
	private static HashMap<String, Section> sections;
	
	private SectionsController() {};
	
	public static Section getSection(String name) throws IOException {
		if(sections == null) {
			loadSections();
		}
		return sections.get(name);
	}
	
	private static void loadSections() throws IOException {
		Section main = new Section(new File(Util.toAbsoluteUrl("static/images/sections/")));
		sections = new HashMap<>();
		addSections(main);
	}
	
	private static void addSections(Section section) {
		if(section.getSections().isEmpty()) {
			return;
		}
		System.out.println(section.getSections().values().size());
		section.getSections().values().forEach(s -> {
			sections.put(section.getPath(), section);
			addSections(s);
		});
	}
	
}
