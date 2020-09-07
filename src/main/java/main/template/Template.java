package main.template;

import java.io.IOException;

public interface Template {
	
	public String getTemplatePath();
	public void setTemplatePath(String path);
	
	public String compile() throws IOException;
}
