package main.exception;

@SuppressWarnings("serial")
public class TemplateException extends RuntimeException {

	public TemplateException() {
		super();
	}
	
	public TemplateException(Throwable t) {
		super(t);
	}
	
	public TemplateException(String message) {
		super(message);
	}

}
