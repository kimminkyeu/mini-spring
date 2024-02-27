package minispring.http.response;

import minispring.http.base.HttpBody;
import org.jetbrains.annotations.NotNull;

public class HttpResponseBody extends HttpBody {
  public HttpResponseBody(@NotNull String content) {
    super(content); // assert notNull inside
  }
}
