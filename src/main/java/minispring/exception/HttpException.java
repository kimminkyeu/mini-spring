package minispring.exception;

import minispring.http.base.HttpStatus;

// NOTE: Checked Exception으로 반드시 프로그래머가 처리하도록 명시하는게 좋겟다.
public abstract class HttpException extends Exception {
  private final HttpStatus httpStatus;

  protected HttpException(HttpStatus httpStatus) {
    super(httpStatus.getStatusMessage());
    this.httpStatus = httpStatus;
  }

  protected HttpException(HttpStatus httpStatus, String additionalHintForServer) {
    super(httpStatus.getStatusMessage() + " : " + additionalHintForServer);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
