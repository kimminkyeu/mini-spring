package minispring;

import minispring.http.response.HttpResponse;

import java.util.function.Consumer;

public class ResponseModifier {
  private final Consumer<HttpResponse> setter;

  public ResponseModifier(Consumer<HttpResponse> setter) {
    this.setter = setter;
  }

  public void apply(HttpResponse response) {
    this.setter.accept(response);
  }
}
