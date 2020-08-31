package main.template.data;

public class ImageTemplateData implements TemplateData {

	String imgPath;
	String previewPath;
	
	public ImageTemplateData(String path) {
		previewPath = path.replace("\\", "/");
		imgPath = previewPath;
	}
	
}
