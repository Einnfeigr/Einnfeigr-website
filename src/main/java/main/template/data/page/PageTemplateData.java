package main.template.data.page;

import main.template.data.TemplateData;
import main.template.data.text.TextTemplateData;

public class PageTemplateData implements TemplateData {
	
	private String path;
	private String title;
	private String text;
	private String page;
	private TextTemplateData textData;
	private Boolean isMobile;
	
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
	public Boolean isMobile() {
		return isMobile;
	}
	public void setMobile(Boolean isMobile) {
		this.isMobile = isMobile;
	}
	
}
