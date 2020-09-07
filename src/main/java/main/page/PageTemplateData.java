package main.page;

import java.util.Map;

import main.template.data.TemplateData;

public class PageTemplateData implements TemplateData {
	
	private String title;
	private String text;
	private String page;
	private Map<String, String> textData;
	private Boolean isMobile;
	
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
	
}
