package minispring;

import java.util.*;

import minispring.http.base.HttpMethod;
import minispring.http.request.HttpRequestUrl;
import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;

public class RequestHandlerStore {
  private final List<RequestHandler> handlerList_GET = new ArrayList<>();
  private final List<RequestHandler> handlerList_PUT = new ArrayList<>();
  private final List<RequestHandler> handlerList_POST = new ArrayList<>();
  private final List<RequestHandler> handlerList_DELETE = new ArrayList<>();
  // NOTE: Map을 쓰지 않은 이유 : Method key의 개수가 정해져 있기 때문. (한정된 Key 범위)
  //       Map을 쓰면, GET PUT POST같은 key값을 따로 검증해야 한다.
  // TODO: add more here...

  // put 은 동시 접근을 원칙적으로 막아야 합니다.
  public void put(@NotNull HttpMethod httpMethod, @NotNull RequestHandler requestHandler) throws IllegalArgumentException {
    Assert.notNull(httpMethod);
    Assert.notNull(requestHandler);

    switch (httpMethod) {
      case GET:
        handlerList_GET.add(requestHandler);
        break;
      case POST:
        handlerList_POST.add(requestHandler);
        break;
      case PUT:
        handlerList_PUT.add(requestHandler);
        break;
      case DELETE:
        handlerList_DELETE.add(requestHandler);
        break;
      default:
        throw new IllegalArgumentException("아직 구현이 되지 않았습니다");
    }
  }

  public Optional<RequestHandler> get(@NotNull HttpMethod httpMethod, @NotNull HttpRequestUrl url) {
    Assert.notNull(httpMethod);
    Assert.notNull(url);

    List<RequestHandler> list = null;
    switch (httpMethod) {
      case GET:
        list = handlerList_GET;
        break;
      case POST:
        list = handlerList_POST;
        break;
      case PUT:
        list = handlerList_PUT;
        break;
      case DELETE:
        list = handlerList_DELETE;
        break;
      default:
        return Optional.empty();
    }
    return list.stream().filter(handler -> handler.isMatching(url)).findFirst();
  }
}