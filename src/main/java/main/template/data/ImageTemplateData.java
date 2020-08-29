package main.template.data;

import java.io.File;

public class ImageTemplateData implements TemplateData {

	String imgPath;
	String previewPath;
	
	public ImageTemplateData(File file) {
		imgPath = file.toString();
		previewPath = imgPath;
	}
	
}
