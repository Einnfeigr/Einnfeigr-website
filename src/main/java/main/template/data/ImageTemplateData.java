package main.template.data;

import java.io.File;

import main.misc.Util;

public class ImageTemplateData implements TemplateData {

	String imgPath;
	
	public ImageTemplateData(File file, String path) {
		if(!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("Given file is invalid | "+file);
		}
		imgPath = path+Util.toRelativeUrl(file).replace("static/", "");
		System.out.println(imgPath);
	}
	
}
