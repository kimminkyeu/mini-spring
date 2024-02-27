package minispring;

import java.util.*;

import minispring.http.base.HttpMethod;
import minispring.http.request.HttpRequestUrl;
import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;

public class RequestHandlerListProxy {
  private final List<RequestHandler> handlerList_GET = Collections.synchronizedList(new ArrayList<>());
  private final List<RequestHandler> handlerList_PUT = Collections.synchronizedList(new ArrayList<>());
  private final List<RequestHandler> handlerList_POST = Collections.synchronizedList(new ArrayList<>());
  private final List<RequestHandler> handlerList_DELETE = Collections.synchronizedList(new ArrayList<>());
  // TODO: add more here...

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
    return list.stream().filter(handler -> handler.matcher(url).matches()).findFirst();
  }
}