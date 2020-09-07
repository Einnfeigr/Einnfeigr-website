package main.template;

import java.io.IOException;

public interface Template {
	
	public String getTemplatePath();
	public void setTemplatePath(String templatePath);
	
	public String compile() throws IOException;
}
