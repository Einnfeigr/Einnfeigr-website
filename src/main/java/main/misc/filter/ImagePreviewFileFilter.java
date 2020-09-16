package main.misc.filter;

import java.io.File;

import main.misc.Util;

public class ImagePreviewFileFilter implements FileFilter {
	
	@Override
	public boolean isValid(File file) {
		String extension;
		if(file.isDirectory() 
				|| (extension = Util.getExtension(file.getName())) == null) {
			return false;
		}
		String name = file.getName();
		if(name.contains("background") || name.contains("logo") 
				|| name.contains("placeholder")) {
			return false;
		}
		switch(extension) {
			case("jpeg"):
			case("jpg"):
			case("png"):
			case("webp"):
				return true;
			default:
				return false;
		}
	}
	
}
