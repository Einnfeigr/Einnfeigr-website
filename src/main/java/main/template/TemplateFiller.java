package main.template;

import java.util.HashMap;
import java.util.Map;

public class TemplateFiller {

	private String path;
	private Map<String, String> params;
	
	TemplateFiller(String path) {
		this.path = path;
		params = new HashMap<>();
	}
	
	public void param(String key, String value) {
		params.put(key, value);
	}
	
	public Template build() {
		EssentialTemplate<Map<String, String>> template = new EssentialTemplate<>(); 
		template.setData(params);
		template.setTemplatePath(path);
		return template;
	}
}
