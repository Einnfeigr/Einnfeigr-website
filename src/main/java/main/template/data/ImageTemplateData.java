package main.template.data;

import java.util.HashMap;
import java.util.Map;

public class ImageTemplateData implements TemplateData {

	String imgPath;
	String previewPath;
	
	public ImageTemplateData(String path) {
		previewPath = path.replace("\\", "/");
		imgPath = previewPath;
	}
	
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put("imgPath", imgPath);
		map.put("previewPath", previewPath);
		return map;
	}
	
}
