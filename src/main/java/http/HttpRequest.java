package http;

public class HttpRequest {
  private HttpRequestEntity entity;
  // private HttpRequestHeader header;
  // private HttpRequestBody body;

  HttpRequest(HttpRequestEntity entity) {
    this.entity = entity;
  }

  public HttpRequestEntity getEntity() {
    return entity;
  }
}
