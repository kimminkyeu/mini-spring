package minispring.http.base;

public enum HttpStatus {
  // 1xx Informational
  CONTINUE(100, "Continue"),
  SWITCHING_PROTOCOLS(101, "Switching Protocols"),
  // TODO: add more...

  // 2xx Success
  OK(200, "OK"),
  CREATED(201, "Created"),
  ACCEPTED(202, "Accepted"),
  NO_CONTENT(204, "No Content"),
  // TODO: add more...

  // 3xx Redirection
  MULTIPLE_CHOICES(300, "Multiple Choices"),
  MOVED_PERMANENTLY(301, "Moved Permanently"),
  FOUND(302, "Found"),
  // TODO: add more ...

  // 4xx Client Error
  BAD_REQUEST(400, "Bad Request"),
  UNAUTHORIZED(401, "Unauthorized"),
  NOT_FOUND(404, "Not Found"),
  // TODO: add more ...

  // 5xx Server Error
  INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
  NOT_IMPLEMENTED(501, "Not Implemented");
  // TODO: add more ...

  final int value;
  final String message;

  HttpStatus(int value, String message) {
    this.value = value;
    this.message = message;
  }

  public String getStatusMessage() {
    return this.message;
  }

  public int getValue() {
    return this.value;
  }

  public boolean isError() {
    return ((400 <= this.value) && (this.value <= 599));
  }

  public boolean isSameCodeAs(HttpStatus other) {
    return (this.value == other.value);
  }

  @Override
  public String toString() {
    return value + " " + message;
  }
}