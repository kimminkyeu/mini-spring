package minispring.http.response;

import minispring.http.base.HttpContentType;
import org.jetbrains.annotations.NotNull;
import minispring.util.Assert;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static minispring.util.IOUtils.CRLF;

public class HttpResponseSender {
  private final DataOutputStream out;
  private final HttpResponse res;

  public HttpResponseSender(@NotNull OutputStream out, @NotNull HttpResponse response) {
    Assert.notNull(out);
    Assert.notNull(response);

    this.out = new DataOutputStream(out);
    this.res = response;
  }

  public void sendAll() throws IOException {
    Assert.notNull(res);

    byte[] body = null;
    if (res.getBody() != null) {
      String raw = res.getBody().toString();
      body = raw.getBytes(StandardCharsets.UTF_8);
      res.getHeader().setContentType(HttpContentType.TEXT_HTML);
      res.setContentLength(body.length);
    }

    String headerRaw = res.getEntity() + CRLF + res.getHeader() + CRLF;
    out.writeBytes(headerRaw);
    if (body != null) {
      out.write(body, 0, body.length);
    }
    out.flush();
  }
}
