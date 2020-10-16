package main.misc.mav.mustache;

import java.io.IOException;

import main.misc.mav.ModelAndViewBuilder;
import main.misc.mav.Page;

public class MustachePage extends MustacheElement implements Page {

	public MustachePage title(String title) {
		builder.data("pageTitle", title);
		builder.title(title);
		return this;
	}
	
	public MustachePage(ModelAndViewBuilder builder) {
		super(builder);
	}
	
	public String getPath() {
		return templatePath;
	}
	
	@Override
	public String compile() throws IOException {
		return super.compile();
	}
	
}
