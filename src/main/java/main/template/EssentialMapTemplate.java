package main.template;

import java.util.Map;

public class EssentialMapTemplate extends AbstractDataTemplate
		<Map<String, String>> {

	public EssentialMapTemplate() {}
	
	public EssentialMapTemplate(String templatePath) {
		super(templatePath);
	}
	
	public EssentialMapTemplate(Map<String, String> data) {
		super(data);
	}

}
