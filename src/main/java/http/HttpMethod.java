package http;

import java.io.Serializable;

// 참고 1 : https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpMethod.html
// 참고 2 : https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/http/HttpMethod.java
// Serializable, Comparable 두개가 명시적으로 붙은 이유는?
public class HttpMethod {
  public enum HttpMethodType {
    GET,
    HEAD,
    PUT,
    POST,
    DELETE,
    OPTIONS,
    TRACE,
    PATCH
  }
  private final HttpMethodType type;

  public static HttpMethod valueOf(String method) {
    return new HttpMethod(HttpMethodType.valueOf(method));
  }

  public HttpMethod(HttpMethodType type) {
    this.type = type;
  }
}