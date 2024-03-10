package minispring;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;

import minispring.exception.HttpClient4xxException;
import minispring.http.base.HttpMethod;
import minispring.http.request.HttpRequestEntity;
import minispring.util.Assert;

public class RequestHandlerStoreManager {
  private RequestHandlerStoreManager() {}
  private static RequestHandlerStoreManager instance = null;

  public static synchronized RequestHandlerStoreManager getInstance() {
    if (instance == null) {
      instance = new RequestHandlerStoreManager();
    }
    return instance;
  }

  private final RequestHandlerStore store = new RequestHandlerStore();

  public void register(@NotNull HttpMethod httpMethod, @NotNull RequestHandler requestHandler) {
    Assert.notNull(httpMethod);
    Assert.notNull(requestHandler);

    this.store.put(httpMethod, requestHandler);
  }

  public Optional<RequestHandler> getMatchingHandler(@NotNull HttpRequestEntity httpRequestEntity) throws HttpClient4xxException {
    Assert.notNull(httpRequestEntity);

    return store.get(httpRequestEntity.getHttpMethod(), httpRequestEntity.getHttpRequestUrl());
  }
}


