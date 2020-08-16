package main.exception;

public class TemplateException extends RuntimeException {
	
	private static final long serialVersionUID = -2671373206147434830L;

	private String path;
	
	public TemplateException() {
		super();
	}
	
	public TemplateException(Throwable t) {
		super(t);
	}
	
	public TemplateException(String message) {
		super(message);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
