package main.template;

import java.io.IOException;
import java.io.StringWriter;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class AbstractDataTemplate<T> extends AbstractTemplate {
	
	public AbstractDataTemplate() {}
	
	public AbstractDataTemplate(String templatePath) {
		super(templatePath);
	}

	public AbstractDataTemplate(T data) {
		setData(data);
	}
	
	T data;
	
	public void setData(T data) {
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
	
	@Override
	public String compile() throws IOException {
	   	MustacheFactory factory = new DefaultMustacheFactory();
	   	Mustache mustache = factory.compile(templatePath);
	   	StringWriter writer = new StringWriter();
	   	mustache.execute(writer, data).flush();
	   	return writer.toString();
	}
}
