package minispring.http.base;

import org.jetbrains.annotations.NotNull;
import minispring.util.Assert;

public enum HttpVersion {
  HTTP_09("HTTP/0.9"), // 0.9
  HTTP_10("HTTP/1.0"), // 1.0
  HTTP_11("HTTP/1.1"), // 1.1
  HTTP_20("HTTP/2.0"), // 2.0
  HTTP_30("HTTP/3.0"); // 3.0

  String versionRaw;

  HttpVersion(String version) {
    this.versionRaw = version;
  }

  @NotNull
  public static HttpVersion fromString(final @NotNull String version) throws IllegalArgumentException {
    Assert.notNull(version);
    switch (version) {
      case ("HTTP/0.9"):
        return HTTP_09;
      case ("HTTP/1.0"):
        return HTTP_10;
      case ("HTTP/1.1"):
        return HTTP_11;
      case ("HTTP/2.0"):
        return HTTP_20;
      case ("HTTP/3.0"):
        return HTTP_30;
      default:
        throw new IllegalArgumentException("unknown http version enum");
    }
  }

  public boolean isSameVersionAs(HttpVersion other) {
    return (this.versionRaw.equals(other.versionRaw));
  }

  @Override
  public String toString() {
    return this.versionRaw;
  }
}

