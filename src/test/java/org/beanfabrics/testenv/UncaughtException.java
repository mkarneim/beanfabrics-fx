package org.beanfabrics.testenv;

public class UncaughtException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public UncaughtException() {
    super();
  }

  public UncaughtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public UncaughtException(String message, Throwable cause) {
    super(message, cause);
  }

  public UncaughtException(String message) {
    super(message);
  }

  public UncaughtException(Throwable cause) {
    super(cause);
  }

}
