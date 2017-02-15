package pt.ulisboa.tecnico.softeng.broker.domain.exception;

public class BrokerException extends RuntimeException {
  public BrokerException() {
    super();
  }

  public BrokerException(String message) {
    super(message);
  }
}
