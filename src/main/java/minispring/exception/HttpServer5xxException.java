package minispring.exception;

import minispring.http.base.HttpStatus;

// NOTE: Checked Exception으로 반드시 프로그래머가 처리하도록 명시
public final class HttpServer5xxException extends HttpException {

  public HttpServer5xxException(HttpStatus httpStatus) {
    super(httpStatus);
  }

  public HttpServer5xxException(HttpStatus httpStatus, String additionalHintForServer) {
    super(httpStatus, additionalHintForServer);
  }
}