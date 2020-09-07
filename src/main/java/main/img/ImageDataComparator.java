package main.img;

import java.util.Comparator;

public class ImageDataComparator implements Comparator<ImageData> {

	@Override
	public int compare(ImageData arg0, ImageData arg1) {
		int result = 0;
		int size;
		String arg0Title = arg0.getTitle();
		String arg1Title = arg1.getTitle();
		if(arg1Title.length() < arg0Title.length()) {
			size = arg1Title.length();
		} else {
			size = arg0Title.length();
		}
		for(int x = 0; x < size; x++) {
			if(arg1Title.charAt(x) < arg0Title.charAt(x)) {
				result = -1;
			} else if(arg1Title.charAt(x) > arg0Title.charAt(x)) {
				result = 1;
			} else {
				result = 0;
			}
		}
		if(result == 0 && arg1Title.length() != arg0Title.length()) {
			if(arg1Title.length() > arg0Title.length()) {
				result = -1;
			} else {
				result = 1;
			}
		}
		return result;
	}

}
