package main.template.data;

import java.io.File;

import main.misc.Util;

public class ImageTemplateData implements TemplateData {

	String imgPath;
	String previewPath;
	
	public ImageTemplateData(File file) {
		if(!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("Given file is invalid | "+file);
		}
		imgPath = Util.toRelativeUrl(file).replace("static/", "");
		if(!imgPath.contains("preview")) {
			previewPath = imgPath.replace("img/", "img/preview/");
		} else {
			previewPath = imgPath;
		}
	}
	
}
