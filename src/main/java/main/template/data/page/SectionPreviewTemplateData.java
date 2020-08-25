package main.template.data.page;

import main.template.data.SectionTemplateData;

public class SectionPreviewTemplateData extends SectionTemplateData {

	String previews;

	@Override
	public void setImages(String images) {
		this.previews = images;
	}
	
}
