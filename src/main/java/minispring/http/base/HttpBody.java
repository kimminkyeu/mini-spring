package minispring.http.base;

import org.jetbrains.annotations.NotNull;
import minispring.util.Assert;

// TODO: string 기반으로 하는게 맞을까? bytestream이 더 낫지 않을까?
public abstract class HttpBody {
  private @NotNull String raw;

  public HttpBody(@NotNull String content) {
    Assert.notNull(content);
    this.raw = content;
  }

  public boolean isEmpty() {
    return this.raw.isEmpty();
  }

  /*
  public int getContentLength() {
    // TODO: isEmpty를 사용해도 괜찮을 듯?
    int length = this.raw.getBytes(StandardCharsets.UTF_8).length;
    Assert.isTrue(length > 0, "김민규", () -> "Content-length는 반드시 0보다 큰 값이 설정되어야 합니다");

    return length;
  }
  */

  @Override
  public String toString() {
    return raw;
  }
}
