package minispring.http.request;

import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HttpRequest {
  private @NotNull final HttpRequestEntity entity;
  private @NotNull final HttpRequestHeader header;
  private @Nullable final HttpRequestBody body;

  HttpRequest(@NotNull HttpRequestEntity entity, @NotNull HttpRequestHeader header, @Nullable HttpRequestBody body) {
    Assert.notNull(entity);
    Assert.notNull(header);

    this.entity = entity;
    this.header = header;
    this.body = body;
  }

  public HttpRequestEntity getEntity() {
    return this.entity;
  }

  public HttpRequestHeader getHeader() {
    return header;
  }

  public HttpRequestBody getBody() {
    return body;
  }
}
