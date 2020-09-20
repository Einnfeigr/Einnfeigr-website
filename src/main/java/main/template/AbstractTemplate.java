package main.template;

import java.io.IOException;
import java.io.StringWriter;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class AbstractTemplate implements Template {

	String path;
	
	@Override
	public void setTemplatePath(String templatePath) {	
		this.path = templatePath;
	}
	
	@Override
	public String getTemplatePath() {
		return path;
	}
	
	@Override
	public String compile() throws IOException {
	   	MustacheFactory factory = new DefaultMustacheFactory();
	   	Mustache mustache = factory.compile(path);
	   	StringWriter writer = new StringWriter();
	   	mustache.execute(writer, "").flush();
	   	return writer.toString();
	}
}
