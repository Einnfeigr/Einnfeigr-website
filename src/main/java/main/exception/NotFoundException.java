package main.exception;

@SuppressWarnings("serial")
public class NotFoundException extends ControllerException {

	public NotFoundException(String message) {
		super(message);
	}

}
