package main.drive.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreviewMeta {

	private Map<String, Size> sizes;
	
	public PreviewMeta(List<String> sizes) {
		this.sizes = new HashMap<>();
		sizes.forEach(s -> {
			this.sizes.put(s, new Size());
		});
	}
	
}

class Size {
	
	private Dimensions dimensions;
	private List<String> ids;
	
	public Size() {
		dimensions = new Dimensions();
		ids = new ArrayList<>();
	}
	
	public void addId(String id) {
		ids.add(id);
	}
	
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	public Dimensions getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(Dimensions dimensions) {
		this.dimensions = dimensions;
	}
	
}

class Dimensions {
	
	private Integer width;
	private Integer height;
	
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	public Integer getWidth() {
		return width;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	public Integer getHeight() {
		return height;
	}
}