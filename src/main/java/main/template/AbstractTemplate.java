package main.template;

import java.io.IOException;
import java.io.StringWriter;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class AbstractTemplate implements Template {

	String templatePath;
	
	@Override
	public void setTemplatePath(String templatePath) {	
		this.templatePath = templatePath;
	}
	
	@Override
	public String getTemplatePath() {
		return templatePath;
	}
	
	@Override
	public String compile() throws IOException {
	   	MustacheFactory factory = new DefaultMustacheFactory();
	   	Mustache mustache = factory.compile(templatePath);
	   	StringWriter writer = new StringWriter();
	   	mustache.execute(writer, "").flush();
	   	return writer.toString();
	}
}
