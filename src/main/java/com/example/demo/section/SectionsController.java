package com.example.demo.section;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.example.demo.Util;
import com.example.demo.pojo.TemplateData;
import com.example.demo.pojo.TextTemplateData;

public class SectionsController {
	
	private static HashMap<String, Section> sections;
	
	private SectionsController() {};
	
	public static Section getSection(String name) throws IOException {
		if(sections == null) {
			loadSections();
		}
		name = "static\\images\\sections\\"+name;
		if(!sections.containsKey(name)) {
			throw new IOException(name+" section can't be found");
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
		section.getSections().forEach((k, v) -> {
			sections.put(k, v);
			addSections(v);
		});
	}
	
	public static String compileSection(Section section, String path) throws IOException {
		if(section == null) {
			return null;
		}
		String imagesText = compileImages(section.getImages(), path);
		@SuppressWarnings("unused")
		TemplateData data = new TextTemplateData() {
			String images = imagesText;
		};
		return Util.compileTemplate(Util.toAbsoluteUrl("templates/misc/sections/section.mustache"), data);
	}
	
	private static String compileImages(List<File> images, String path) {
		StringBuilder text = new StringBuilder("");
		images.forEach(i -> {
			try {
				TemplateData data = new TextTemplateData() {
					@SuppressWarnings("unused")
					String imgPath = path+Util.toRelativeUrl(i.getAbsolutePath()).replace("static", "");
				};
				text.append(Util.compileTemplate(Util.toAbsoluteUrl("templates/misc/sections/image.mustache"), data));
			} catch(IOException e) {
				text.append("something went wrong");
				e.printStackTrace();
			}
		});
		return text.toString();
	}

	public static String compileSections(String path) throws IOException {
		StringBuilder text = new StringBuilder("");
		StringBuilder previewText = new StringBuilder("");
		for(Entry<String, Section> entry : sections.entrySet()) {
			Section section = entry.getValue();
			for(int x = 0; x < 3; x++) {
				final int i = x;
				TemplateData data = new TextTemplateData() {
					@SuppressWarnings("unused")
					String imgPath = path+Util.toRelativeUrl(section.getImages().get(i).getAbsolutePath()).replace("static", "");
				};
				previewText.append(Util.compileTemplate(Util.toAbsoluteUrl("templates/misc/sections/preview.mustache"), data));
			}
		}
		@SuppressWarnings("unused")
		TemplateData data = new TemplateData() {
			String previews = previewText.toString();
		};
		text.append(Util.compileTemplate(Util.toAbsoluteUrl("templates/misc/sections/sections.mustache"), data));
		return text.toString();
	}
}
