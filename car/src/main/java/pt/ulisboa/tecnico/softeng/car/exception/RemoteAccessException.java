package pt.ulisboa.tecnico.softeng.car.exception;

public class RemoteAccessException extends RuntimeException {
	public RemoteAccessException() {
		super();
	}

	public RemoteAccessException(String message) {
		super(message);
	}
}
