package com.example.demo.pojo;

public class PageTemplateData implements TemplateData {
	
	private String path;
	private String title;
	private String text;
	private String page;
	private TextTemplateData textData;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
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
	public TextTemplateData getTextData() {
		return textData;
	}
	public void setTextData(TextTemplateData textData) {
		this.textData = textData;
	}
	
}
