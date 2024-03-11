package minispring.http.request;

import minispring.http.base.HttpBody;
import org.jetbrains.annotations.NotNull;

public final class HttpRequestBody extends HttpBody {
  public HttpRequestBody(@NotNull String content) {
    super(content);
  }
}
