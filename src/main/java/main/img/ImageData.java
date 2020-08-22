package main.img;

import java.io.File;
import java.io.FileNotFoundException;

import main.misc.Util;

public class ImageData implements Comparable<ImageData> {

	private String path;
	private Long indexingTime;
	private File file;

	public String getPath() {
		return path.replace("\\", "/").replace("static", "");
	}
	
	public String getName() {
		if(file == null) {
			file = new File(Util.toAbsoluteUrl(path));
		}
		return file.getName();
	}

	public File createFile() {
		return Util.createFile(path);
	}
	
	public File getFile() {
		if(file == null) {
			file = new File(Util.toAbsoluteUrl(path));
		}
		return file;
	}
	
	public void setFile(File file) {
		this.path = Util.toRelativeUrl(file);
		try {
			if(!Util.isAbsolute(file)) {
				file = Util.getFile(file.getAbsolutePath());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
