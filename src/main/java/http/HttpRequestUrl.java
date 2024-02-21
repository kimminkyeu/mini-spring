package http;

public class HttpRequestUrl {
  private final String innerUrl;

  public HttpRequestUrl(final String url) {
    this.innerUrl = url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HttpRequestUrl that = (HttpRequestUrl) o;

    return innerUrl.equals(that.innerUrl);
  }

  @Override
  public String toString() {
    return innerUrl;
  }

  @Override
  public int hashCode() {
    return innerUrl.hashCode();
  }
}