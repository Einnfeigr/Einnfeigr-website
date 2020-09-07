package main.template;

import main.template.data.TemplateData;

public class EssentialDataTemplate extends AbstractDataTemplate<TemplateData> {
	
	public EssentialDataTemplate() {
		super();
	}
	
	public EssentialDataTemplate(String templatePath) {
		super(templatePath);
	}
	
	public EssentialDataTemplate(TemplateData data) {
		super(data);
	}
	
}
