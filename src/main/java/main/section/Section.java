package main.section;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.misc.Util;

public class Section {
	
	private List<File> images;
	private HashMap<String, Section> sections;
	private String path;
	private String name;

	public Section(File directory) throws IOException {
		if(!directory.isDirectory()) {
			throw new IOException(directory.getAbsolutePath()+" is not a directory");
		}
		if(!directory.exists()) {
			throw new FileNotFoundException(directory.getAbsolutePath()+" does not exists");
		}
		images = new ArrayList<>();
		sections = new HashMap<>();
		for(File file : directory.listFiles()) {
			if(file.isDirectory()) {
				sections.put(Util.toRelativeUrl(file.getAbsolutePath()), new Section(file));
			} else {
				try {
					String extension = file.getName().split("\\.")[1];
					switch(extension) {
						case("jpeg"):
						case("jpg"):
						case("png"):
						case("gif"):
							images.add(file);
					}
				} catch(IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}
		path = Util.toRelativeUrl(directory.getAbsolutePath());
		setName(path.replace("\\", "/").replace("static/img/portfolio/sections/", ""));
	}
	
	public List<File> getImages() {
		return images;
	}

	public HashMap<String, Section> getSections() {
		return sections;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
