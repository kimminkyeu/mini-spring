package minispring;

import com.google.common.net.HttpHeaders;
import minispring.annotation.RequestBody;
import minispring.exception.HttpClient4xxException;
import minispring.exception.HttpException;
import minispring.http.base.HttpContentType;
import minispring.http.base.HttpStatus;
import minispring.http.request.HttpRequest;
import minispring.http.response.HttpResponse;
import minispring.http.response.HttpResponseBody;
import minispring.util.Assert;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class DispatcherServlet {
  public DispatcherServlet() {}

  public void dispatch(@NotNull HttpRequest request, @NotNull HttpResponse response) throws HttpException {
    Assert.notNull(request);
    Assert.notNull(response);

    Optional<RequestHandler> handlerOptional = RequestHandlerStoreManager.getInstance()
            .getMatchingHandler(request.getEntity());
    if (handlerOptional.isEmpty()) {
      throw new HttpClient4xxException(
              HttpStatus.NOT_FOUND,
              "주어진 URL(" + request.getEntity().getHttpRequestUrl() + ") 을 처리할 Controller가 없습니다.");
    }

    RequestHandler handler = handlerOptional.get();
    Method handlerMethod = handler.getHandlerMethod();
    String declaringClassName = handlerMethod.getDeclaringClass().getSimpleName();
    Object instance = BeanManager.getInstance().getBean(declaringClassName);

    try {
      Object[] arguments = generateArgumentToPassXXX(request, handler);
      Object returnValue = handlerMethod.invoke(instance, arguments);
      response.setHttpStatus(HttpStatus.OK); // defaults to ok
      if (returnValue != null) {
        setReturnValueToResponse(response, returnValue); // responseSetter로 인해 ok에서 변경 가능.
      }
    } catch (InvocationTargetException e) {
      Throwable exceptionFromInvokedMethod = e.getTargetException();
      if (exceptionFromInvokedMethod instanceof HttpException) {
        throw ((HttpException) exceptionFromInvokedMethod);
      } else {
        exceptionFromInvokedMethod.printStackTrace();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // TODO: 로직 싹 리팩토링 하기. + 방식도 싹 갈아엎을 것. Regex를 쓰는게 과연 맞을까?
  private Object[] generateArgumentToPassXXX(@NotNull HttpRequest request, @NotNull RequestHandler handler) {
    Assert.notNull(request);
    Assert.notNull(handler);

    Object[] rawArguments = null;
    if ( (handler.getHandlerMethod().getParameterCount() == 1) && // 일단 단일 파라미터만 지원
         ((handler.getHandlerMethod().getParameters())[0].getAnnotation(RequestBody.class) != null)
    ) {
      rawArguments = new String[1]; // request에 body가 없으면 null 넣어주는게 맞다. 이건 외부 데이터이기 때문이다.
      rawArguments[0] = (request.getBody() != null)
              ? (request.getBody().toString())
              : (null);
    } else if ( (handler.getHandlerMethod().getParameterCount() == 1) && // 일단 단일 파라미터만 지원
         ((handler.getHandlerMethod().getParameters())[0].getType().equals(HttpRequest.class)) // request 통으로 넘겨주기.
    ) {
      rawArguments = new Object[1]; // request에 body가 없으면 null 넣어주는게 맞다. 이건 외부 데이터이기 때문이다.
      rawArguments[0] = request;
    } else {
      rawArguments = handler.getMatchingArgumentsInStringXXX(request.getEntity().getHttpRequestUrl());
    }


    Class<?>[] paramTypes = handler.getHandlerMethod().getParameterTypes();
    Assert.isTrue( (paramTypes.length == rawArguments.length), "김민규",
            "Controller 함수의 인자 개수와 URL에서 추출한 인자의 개수가 같아야 합니다. " +
                    "Controller에 @RequestParam 혹은 @PathVariable이 안 붙은 인자가 있나요?"
    );

    Object[] castedArguments = new Object[rawArguments.length];
    for (int i=0; i<rawArguments.length; ++i) {
      castedArguments[i] = paramTypes[i].cast(rawArguments[i]);
    }
    return castedArguments;
  }

  public void setReturnValueToResponse(@NotNull HttpResponse response, @NotNull Object returnValue) throws IOException {
    Assert.notNull(response);
    Assert.notNull(returnValue);

    if (returnValue instanceof ResponseModifier) {
      ((ResponseModifier) returnValue).apply(response);
    } else { // convert object to json
      ObjectMapper objectMapper = new ObjectMapper();
      response.getHeader().put(HttpHeaders.CONTENT_TYPE, HttpContentType.APPLICATION_JSON.getTypeRaw());
      response.setBody(new HttpResponseBody(objectMapper.writeValueAsString(returnValue)));
    }
  }
}
