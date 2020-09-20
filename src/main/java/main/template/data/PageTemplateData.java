package main.template.data;

import java.util.HashMap;
import java.util.Map;

public class PageTemplateData implements TemplateData {
	
	private Map<String, String> textData;
	private Boolean isMobile;
	private String title;
	private String text;
	private String page;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title.trim();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public Map<String, String> getTextData() {
		return textData;
	}
	public void setTextData(Map<String, String> textData) {
		this.textData = textData;
	}
	public Boolean isMobile() {
		return isMobile;
	}
	public void setMobile(Boolean isMobile) {
		this.isMobile = isMobile;
	}
	
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put("title", title);
		map.put("text", text);
		map.put("page", page);
		if(isMobile != null) {
			map.put("isMobile", "true");
		}
		return map;
	}
	
}
