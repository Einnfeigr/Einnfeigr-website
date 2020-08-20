package main.img;

import java.io.File;

public class ImageData implements Comparable<ImageData> {

	private File file;
	private Long indexingTime;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
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
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((indexingTime == null) ? 0 : indexingTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageData other = (ImageData) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (indexingTime == null) {
			if (other.indexingTime != null)
				return false;
		} else if (!indexingTime.equals(other.indexingTime))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(ImageData data) {
		return (int) (this.indexingTime-data.getIndexingTime());
	}
	
}
