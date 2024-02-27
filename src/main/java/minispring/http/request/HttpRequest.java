package minispring.http.request;

public final class HttpRequest {
  private HttpRequestEntity entity;
   private HttpRequestHeader header;
   private HttpRequestBody body;

  HttpRequest(HttpRequestEntity entity, HttpRequestHeader header, HttpRequestBody body) {
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
