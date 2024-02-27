package minispring.http.response;

import com.google.common.net.HttpHeaders;
import minispring.http.base.HttpHeader;
import minispring.http.base.HttpContentType;
import org.jetbrains.annotations.NotNull;
import minispring.util.Assert;

public class HttpResponseHeader extends HttpHeader {

  public void setContentType(@NotNull HttpContentType contentType) {
    Assert.notNull(contentType);
    this.put(HttpHeaders.CONTENT_TYPE, contentType.getTypeRaw());
  }

  @NotNull
  public String getContentType() {
    String contentType = super.get(HttpHeaders.CONTENT_TYPE);
    Assert.notNull(contentType);

    return contentType;
  }

  public void setContentLength(int length) {
    this.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(length));
  }

  // 이 함수를 호출하는 프로그래머는 Content Length가 이미 설정되어 있음을 보장해야 합니다.
  public int getContentLength() {
    String lengthString = super.get(HttpHeaders.CONTENT_LENGTH);
    Assert.notNull(lengthString);

    int length = Integer.parseInt(lengthString);
    Assert.isTrue(length > 0, "김민규", () -> "Content-length는 반드시 0보다 큰 값이 설정되어야 합니다");

    return length;
  }
}
