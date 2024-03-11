package minispring;

import minispring.http.request.HttpRequest;
import minispring.http.request.HttpRequestUrl;
import minispring.util.Assert;
import minispring.util.RequestMappingAnnotationToRegexUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler {
  private final Pattern regexPattern;
  private final Method handlerMethod;

  public RequestHandler(@NotNull Method handlerMethod) {
    Assert.notNull(handlerMethod);

    this.handlerMethod = handlerMethod;
    String regex = RequestMappingAnnotationToRegexUtil.convertReqeustMappingToRegexXXX(handlerMethod);
    this.regexPattern = Pattern.compile(regex);
  }

  public Method getHandlerMethod() {
    return this.handlerMethod;
  }

  public Boolean isMatching(@NotNull HttpRequestUrl url) {
    Assert.notNull(url);

    return this.regexPattern.matcher(url.toString()).matches();
  }

  @Nullable
  public String[] getMatchingArgumentsInStringXXX(@NotNull HttpRequestUrl url) {
    Matcher matcher = this.regexPattern.matcher(url.toString());
    Assert.isTrue(matcher.matches(), "김민규",
            "이 함수는 Regex가 match인 경우에만 호출되어야 합니다.");

    List<String> paramList = new ArrayList<>();

    for (int i=1; i<=matcher.groupCount(); ++i) {
      String v = matcher.group(i);
      boolean isRequestParam = v.contains("="); // key=value | key=''  둘다 처리용.
      String[] split = v.split("=");
      if ( split.length == 1 && !isRequestParam ) { // then it is PathVariable
        paramList.add(split[0]);
      } else if ( isRequestParam ) { // then it is RequestParam
        paramList.add(
                split.length == 2 ? split[1] : null // allow null value (ex. [ foo?name= ] )
        );
      } else {
        return null; // TODO : 일단 이렇게 처리하고, 추후 고치기
      }
    }
    return paramList.toArray(new String[0]);
  }
}
