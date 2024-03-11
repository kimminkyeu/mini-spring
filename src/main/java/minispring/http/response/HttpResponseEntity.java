package minispring.http.response;

import minispring.http.base.HttpStatus;
import minispring.http.base.HttpVersion;
import org.jetbrains.annotations.NotNull;
import minispring.util.Assert;

public class HttpResponseEntity {
  private final HttpVersion httpVersion;
  private HttpStatus httpStatus; // enum class

  public HttpResponseEntity(@NotNull HttpVersion version) {
    Assert.notNull(version);
    this.httpVersion = version;
  }

  public void setHttpStatus(@NotNull HttpStatus status) {
    Assert.notNull(status);
    this.httpStatus = status;
  }

  @Override
  public String toString() {
    Assert.notNull(httpVersion);
    Assert.notNull(httpStatus);

    return this.httpVersion.toString() + " " + this.httpStatus.toString();
  }
}

