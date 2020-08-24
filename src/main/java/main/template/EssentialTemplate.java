package main.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import main.exception.TemplateException;
import main.misc.Util;
import main.template.data.TemplateData;

public abstract class EssentialTemplate implements Template {

	protected String templatePath;
	protected String path;
	protected TemplateData data;
	
	public EssentialTemplate() {
		
	}
	
	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath+".mustache";
	}

	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public TemplateData getData() {
		return data;
	}

	public void setData(TemplateData data) {
		this.data = data;
	}

	public String compile() throws IOException {
	   	File template = new File(templatePath);
	   	if(data == null) {
	   		data = new TemplateData() {};
	   	}
	   	if(!template.exists()) {
	   		template = new File(Util.toAbsoluteUrl(templatePath));
	   		if(!template.exists()) {
	   			throw new FileNotFoundException(
	   					"File '"+templatePath+"' cannot be found");
	   		}
	   	}
	   	if(!template.isFile()) {
	   		throw new TemplateException(
	   				"Template '"+templatePath+"' must be a file");
	   	}
	   	MustacheFactory factory = new DefaultMustacheFactory();
	   	Mustache mustache = factory.compile(templatePath);
	   	StringWriter writer = new StringWriter();
	   	mustache.execute(writer, data).flush();
	   	return writer.toString();
	}
	
}
