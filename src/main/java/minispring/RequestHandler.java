package minispring;

import minispring.http.request.HttpRequestUrl;
import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler {
  private final Pattern regexPattern;
  private final Method requestHandler;

  public RequestHandler(@NotNull Method requestHandler) {
    Assert.notNull(requestHandler);
    this.requestHandler = requestHandler;
    String regex = RequestMappingAnnotationToRegexUtil.convertReqeustMappingToRegexXXX(requestHandler);
    this.regexPattern = Pattern.compile(regex);
  }

  public HandlerMatcher matcher(@NotNull HttpRequestUrl url) {
    Assert.notNull(url);
    Matcher matcher = this.regexPattern.matcher(url.toString());
    return new HandlerMatcher(matcher);
  }

  public Method getRequestHandler() {
    return this.requestHandler;
  }

  public static class HandlerMatcher {
    private final Matcher matcher;

    public HandlerMatcher(@NotNull Matcher matcher) {
      Assert.notNull(matcher);
      this.matcher = matcher;
    }

    public boolean matches() {
      return this.matcher.matches();
    }

    @Nullable
    public String[] getMatchingArgumentsInStringXXX() {
      Assert.isTrue(this.matcher.matches(),
              "김민규", () -> "매칭을 한번 더 해야 group으로 접근 가능해서, Assert로 감싸 한번 더 호출하였습니다."
      );

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
}
