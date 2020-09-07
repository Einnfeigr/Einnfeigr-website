package main.template;

public abstract class AbstractTemplate implements Template {

	String templatePath;
	
	public AbstractTemplate() {}
	
	public AbstractTemplate(String templatePath) {
		this.templatePath = templatePath;
	}
	
	@Override
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
	@Override
	public String getTemplatePath() {
		return templatePath;
	}
	
}
