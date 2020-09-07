package main.template.data.page;

import main.template.data.AlbumTemplateData;

public class AlbumPreviewTemplateData extends AlbumTemplateData {

	String previews;

	@Override
	public void setImages(String images) {
		this.previews = images;
	}
	
}
