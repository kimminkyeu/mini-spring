package minispring.http.response;

import com.google.common.net.HttpHeaders;
import minispring.http.base.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import minispring.util.Assert;

import static minispring.util.IOUtils.CRLF;

public class HttpResponse {
  private @NotNull final HttpResponseEntity entity;
  private @NotNull final HttpResponseHeader header;
  private @Nullable HttpResponseBody body = null; // body might change. body can be null

  public HttpResponse(@NotNull HttpVersion version) {
    Assert.notNull(version);
    this.entity = new HttpResponseEntity(version);
    this.header = new HttpResponseHeader();
  }

  public @NotNull HttpResponseEntity getEntity() {
    Assert.notNull(entity);
    return entity;
  }
  public @NotNull HttpResponseHeader getHeader() {
    Assert.notNull(header);
    return header;
  }

  public @Nullable HttpResponseBody getBody() {
    return body;
  }

  public void setHttpStatus(@NotNull HttpStatus status) {
    Assert.notNull(status);
    this.entity.setHttpStatus(status);
  }

  public void setContentLength(int length) {
    this.header.put(HttpHeaders.CONTENT_LENGTH, Integer.toString(length));
  }

  public void setBody(@NotNull HttpResponseBody body) {
    Assert.notNull(body);

    this.body = body;
  }

  @Override
  public String toString() {
    Assert.notNull(entity);
    Assert.notNull(header);
    return (  entity.toString() + CRLF
            + header.toString() + CRLF
            + ((body == null) ? "" : body.toString()) );
  }
}
