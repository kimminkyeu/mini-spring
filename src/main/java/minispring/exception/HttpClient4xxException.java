package minispring.exception;

import minispring.http.base.HttpStatus;

public class HttpClient4xxException extends HttpException {

  public HttpClient4xxException(HttpStatus httpStatus) {
    super(httpStatus);
  }

  public HttpClient4xxException(HttpStatus httpStatus, String additionalHintForServer) {
    super(httpStatus, additionalHintForServer);
  }
}