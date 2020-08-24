package main.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import main.exception.TemplateException;
import main.img.ImageDataController;
import main.misc.Util;
import main.section.Section;
import main.template.pojo.data.TemplateData;
import main.template.pojo.data.TextTemplateData;

public class TemplateController {
	
	public static String compileTemplate(String path) throws IOException {
		return compileTemplate(path, new TemplateData() {});
	}
	
	public static String compileTemplate(String path, TemplateData data)
			throws IOException {
	   	path += ".mustache";
	   	File template = new File(path);
	   	if(!template.exists()) {
	   		template = new File(Util.toAbsoluteUrl(path));
	   		if(!template.exists()) {
	   			throw new FileNotFoundException("File '"+path+"' cannot be found");
	   		}
	   	}
	   	if(!template.isFile()) {
	   		throw new TemplateException("Template '"+path+"' must be a file");
	   	}
	   	MustacheFactory factory = new DefaultMustacheFactory();
	   	Mustache mustache = factory.compile(path);
	   	StringWriter writer = new StringWriter();
	   	mustache.execute(writer, data).flush();
	   	return writer.toString();
	 }
	 
	 public static String compileLatestLoaded(String path) throws IOException {
	   	List<File> files = ImageDataController.getLatestImages();
	   	if(files == null) {
	   		return null;
	   	}
	   	StringBuilder sb = new StringBuilder();
	   	for(File file : files) {
	   		if(file.isDirectory() || !file.exists()) {
	   			continue;
	   		}
	   		@SuppressWarnings("unused")
	   		TemplateData data = new TemplateData() {
	   			String imgPath = path+Util.toRelativeUrl(file)
	   					.replace("\\", "/").replace("static/", "");
	   		};
	   		sb.append(compileTemplate("templates/misc/img/image", data));
	   	}
	   	return sb.toString();
	} 
		
	public static String compileSection(Section section, String path)
			throws IOException {
		if(section == null) {
			return null;
		}
		String imagesText = compileSectionImages(section.getImages(), path);
		@SuppressWarnings("unused")
		TemplateData data = new TextTemplateData() {
			String images = imagesText;
			String name = section.getName();
		};
		return compileTemplate(
				"templates/misc/sections/section", data);
	}
	
	private static String compileSectionImages(List<File> images, String path) {
		StringBuilder text = new StringBuilder("");
		images.forEach(i -> {
			try {
				TemplateData data = new TextTemplateData() {
					@SuppressWarnings("unused")
					String imgPath = path+Util.toRelativeUrl(i.getAbsolutePath())
					.replace("\\", "/").replace("static/", "");
				};
				text.append(compileTemplate("templates/misc/img/image", data));
			} catch(IOException e) {
				text.append("something went wrong");
				e.printStackTrace();
			}
		});
		return text.toString();
	}
	
	public static String compileSections(Map<String, Section> sections, String path)
			throws IOException {
		StringBuilder text = new StringBuilder("");
		StringBuilder previewText = new StringBuilder("");
		StringBuilder sectionsText = new StringBuilder("");
		for(Entry<String, Section> entry : sections.entrySet()) {
			Section section = entry.getValue();
			for(int x = 0; x < 3; x++) {
				final int i = x;
				if(section.getImages().size() <= x) {
					break;
				}
				TemplateData data = new TextTemplateData() {
					@SuppressWarnings("unused")
					String imgPath = (path+Util.toRelativeUrl(
							section.getImages().get(i).getAbsolutePath())
							.replace("static", ""))
							.replace("\\", "/")
							.replace("//", "/");
				};
				previewText.append(
						compileTemplate("templates/misc/sections/preview", data));
			}
			@SuppressWarnings("unused")
			TemplateData data = new TemplateData() {
				String name = section.getName();
				String previews = previewText.toString();
				String title = Util.toUpperCase(section.getName());
			};
			sectionsText.append(
					compileTemplate("templates/misc/sections/section", data));
			previewText.delete(0, previewText.length());
		}
		@SuppressWarnings("unused")
		TemplateData data = new TemplateData() {
			String sections = sectionsText.toString();
		};
		text.append(compileTemplate("templates/misc/sections/sections", data));
		return text.toString();
	}
}
