package main.img;

import java.io.File;
import java.io.FileNotFoundException;

import main.misc.Util;

public class ImageData implements Comparable<ImageData> {

	private String path;
	private Long indexingTime;
	
	public String getPath() {
		return path.replace("\\", "/").replace("static", "");
	}
	public void setPath(String path) {
		this.path = path;
	}

	public File getFile() throws FileNotFoundException {
		return new File(Util.toAbsoluteUrl(path));
	}
	public void setFile(File file) throws FileNotFoundException {
		this.path = Util.toRelativeUrl(file);
	}
	
	public Long getIndexingTime() {
		return indexingTime;
	}
	public void setIndexingTime(Long indexingTime) {
		this.indexingTime = indexingTime;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((indexingTime == null) ? 0 : indexingTime.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ImageData other = (ImageData) obj;
		if (path == null) {
			if (other.path != null) {
				return false;
			}
		} else if (!path.equals(other.path)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(ImageData data) {
		return (int) (this.indexingTime-data.getIndexingTime());
	}
	
}
