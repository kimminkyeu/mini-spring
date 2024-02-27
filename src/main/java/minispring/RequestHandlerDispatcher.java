package minispring;

import minispring.exception.HttpClient4xxException;
import minispring.exception.HttpException;
import minispring.exception.HttpServer5xxException;
import minispring.http.base.HttpStatus;
import minispring.http.request.HttpRequest;
import minispring.http.response.HttpResponse;
import minispring.http.response.HttpResponseBody;
import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Optional;

public class RequestHandlerDispatcher {
  public RequestHandlerDispatcher(){}

  public void dispatch(@NotNull HttpRequest request, @NotNull HttpResponse response) throws HttpException {
    Assert.notNull(request);
    Assert.notNull(response);

    Optional<RequestHandler> handlerOptional = DispatcherManagerProvider
            .getManager()
            .getMatchingRequestHandler(request.getEntity());

    if (handlerOptional.isEmpty()) {
      throw new HttpClient4xxException(
              HttpStatus.NOT_FOUND,
              "주어진 URL(" + request.getEntity().getHttpRequestUrl() + ") 을 처리할 Controller Method가 없습니다.");
    }

    RequestHandler handler = handlerOptional.get();
    Method handlerMethod = handler.getRequestHandler();
    String declaringClassName = handlerMethod.getDeclaringClass().getSimpleName();
    Object instance = BeanManagerProvider.getManager().getBean(declaringClassName);
    String[] rawArguments = handler
            .matcher(request.getEntity().getHttpRequestUrl())
            .getMatchingArgumentsInStringXXX();

    // cast objects accordingly
    Class<?>[] paramTypes = handlerMethod.getParameterTypes();
    Assert.isTrue( (paramTypes.length == rawArguments.length), "김민규",
            () -> "Controller 함수의 인자 개수와 URL에서 추출한 인자의 개수가 같아야 합니다. " +
                  "Controller에 @RequestParam 혹은 @PathVariable이 안붙은 인자가 있나요?"
    );

    Object[] castedArguments = new Object[rawArguments.length];
    for (int i=0; i<rawArguments.length; ++i) {
      castedArguments[i] = paramTypes[i].cast(rawArguments[i]);
    }

    // invoke method
    try {
      Object rv = handlerMethod.invoke(instance, castedArguments);
      if (rv != null) {
        response.setBody(new HttpResponseBody(rv.toString()));
      }
      response.setHttpStatus(HttpStatus.OK);

    } catch (Exception e) {
      throw new HttpServer5xxException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              "invoke 과정에서 문제가 발생했습니다." + e.getMessage()
      );
      // 참고 : invoke method에서 exception이 아니라 error 발생시, getCause는 null로 전달된다. --> 출력한다면 null 체크 필요.
    }
  }
}
