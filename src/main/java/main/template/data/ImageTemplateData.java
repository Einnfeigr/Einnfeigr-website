package main.template.data;

import java.io.File;

import main.misc.Util;

public class ImageTemplateData implements TemplateData {

	String imgPath;
	String previewPath;
	
	public ImageTemplateData(File file, String path) {
		if(!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("Given file is invalid | "+file);
		}
		imgPath = path+Util.toRelativeUrl(file).replace("static/", "");
		if(!imgPath.contains("preview")) {
			previewPath = imgPath.replace("img/", "img/preview/");
		} else {
			previewPath = imgPath;
		}
	}
	
}
