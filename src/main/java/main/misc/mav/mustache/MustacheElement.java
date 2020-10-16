package main.misc.mav.mustache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import main.misc.Util;
import main.misc.mav.AbstractElement;
import main.misc.mav.ModelAndViewBuilder;
import main.template.TemplateFactory;

public abstract class MustacheElement extends AbstractElement {

	public MustacheElement(ModelAndViewBuilder builder) {
		super(builder);
	}

	protected String templatePath;
	protected Map<String, Object> data = new HashMap<>();
	
	public MustacheElement path(String templatePath) {
		this.templatePath = templatePath;
		return this;
	}
	
	public MustacheElement data(Object...data) {
		this.data.putAll(Util.arrayToMap(data));
		return this;
	}
	
	public MustacheElement data(Map<String, Object> data) {
		this.data.putAll(data);
		return this;
	}
	
	public String compile() throws IOException {
		return TemplateFactory.buildTemplate(templatePath, data);
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	
}
