package main.section;

import java.util.ArrayList;
import java.util.List;

import main.img.ImageData;

public class Section {
	
	private List<ImageData> images;
	private List<Section> sections;
	private String id;
	private String name;
	
	public Section() {
		sections = new ArrayList<>();
	}
	
	public List<ImageData> getImages() {
		return images;
	}
	public void setImages(List<ImageData> images) {
		this.images = images;
	}
	public List<Section> getSections() {
		return sections;
	}
	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
	public void addSection(Section section) {
		sections.add(section);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
