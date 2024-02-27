package minispring;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;

import minispring.exception.HttpClient4xxException;
import minispring.exception.HttpException;
import minispring.http.base.HttpMethod;
import minispring.http.request.HttpRequest;
import minispring.http.request.HttpRequestEntity;
import minispring.http.response.HttpResponse;
import minispring.util.Assert;

// TODO: Thread 동기화 문제 대비할 것.
public abstract class DispatcherManagerProvider {
  private static DispatcherManagerSingleton instance = null;
  public static synchronized DispatcherManagerSingleton getManager() {
    if (instance == null) {
      instance = new DispatcherManagerSingleton();
    }
    return instance;
  }

  public static class DispatcherManagerSingleton {
    private final RequestHandlerListProxy requestHandlerListProxy = new RequestHandlerListProxy();
    private final RequestHandlerDispatcher dispatcher = new RequestHandlerDispatcher();

    private DispatcherManagerSingleton() {} // singleton instance

    public void registerToRequestHandlerTable(HttpMethod httpMethod, RequestHandler handlerProxy) {
      this.requestHandlerListProxy.put(httpMethod, handlerProxy);
    }

    public void dispatch(@NotNull HttpRequest req, @NotNull HttpResponse res) throws HttpException {
      this.dispatcher.dispatch(req, res);
    }

    public Optional<RequestHandler> getMatchingRequestHandler(@NotNull HttpRequestEntity httpRequestEntity) throws HttpClient4xxException {
      Assert.notNull(httpRequestEntity); // null 인자는 프로그래머 책임이다.
      return requestHandlerListProxy.get(httpRequestEntity.getHttpMethod(), httpRequestEntity.getHttpRequestUrl());
    }
  }
}


