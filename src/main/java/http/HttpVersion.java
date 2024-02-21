package http;

import exception.BadHttpRequestException;

public class HttpVersion {
  public enum HttpVersionType {
    VERSION_0_9, // 0.9
    VERSION_1_0, // 1.0
    VERSION_1_1, // 1.1
    VERSION_2_0, // 2.0
    VERSION_3_0, // 3.0
  }

  private final HttpVersionType versionType;

  public HttpVersion(final String version) throws BadHttpRequestException {
    switch (version) {
      case ("HTTP/0.9"):
        this.versionType = HttpVersionType.VERSION_0_9;
        break;
      case ("HTTP/1.0"):
        this.versionType = HttpVersionType.VERSION_1_0;
        break;
      case ("HTTP/1.1"):
        this.versionType = HttpVersionType.VERSION_1_1;
        break;
      case ("HTTP/2.0"):
        this.versionType = HttpVersionType.VERSION_2_0;
        break;
      case ("HTTP/3.0"):
        this.versionType = HttpVersionType.VERSION_3_0;
        break;
      default: throw new BadHttpRequestException("Wrong http-version");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HttpVersion that = (HttpVersion) o;

    return versionType == that.versionType;
  }

  @Override
  public int hashCode() {
    return versionType.hashCode();
  }

  @Override
  public String toString() {
    return "HttpVersion{" +
            "versionType=" + versionType +
            '}';
  }
}
