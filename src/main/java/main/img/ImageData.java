package main.img;

public class ImageData implements Comparable<ImageData> {

	private String id;
	private String title;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String name) {
		this.title = name;
	}
	
	@Override
	public int compareTo(ImageData data) {
		int result = 0;
		int size;
		String dataTitle = data.getTitle();
		String thisTitle = this.getTitle();
		if(dataTitle.length() < thisTitle.length()) {
			size = data.getTitle().length();
		} else {
			size = this.getTitle().length();
		}
		for(int x = 0; x < size; x++) {
			if(dataTitle.charAt(x) < thisTitle.charAt(x)) {
				result = -1;
			} else if(dataTitle.charAt(x) > thisTitle.charAt(x)) {
				result = 1;
			} else {
				result = 0;
			}
		}
		if(result == 0 && dataTitle.length() != thisTitle.length()) {
			if(dataTitle.length() > thisTitle.length()) {
				result = -1;
			} else {
				result = 1;
			}
		}
		return result;
	}

	
}
