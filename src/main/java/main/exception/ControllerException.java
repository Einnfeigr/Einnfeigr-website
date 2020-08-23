package main.exception;

@SuppressWarnings("serial")
public class ControllerException extends RuntimeException {
	
	private String path;
	
	public ControllerException() {
		super();
	}
	
	public ControllerException(Throwable t) {
		super(t);
	}
	
	public ControllerException(String message) {
		super(message);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
