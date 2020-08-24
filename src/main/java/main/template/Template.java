package main.template;

import java.io.IOException;

import main.template.data.TemplateData;

public interface Template {
	
	public String compile() throws IOException;
	
	public TemplateData getData();
	public void setData(TemplateData data);
	
	public String getTemplatePath();
	public void setTemplatePath(String path);
	
	public String getPath();
	public void setPath(String path);
}
