package minispring;

import minispring.annotation.PathVariable;
import minispring.annotation.RequestMapping;
import minispring.annotation.RequestParam;
import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class RequestMappingAnnotationToRegexUtil {

  private static String getBaseUrlXXX(String url) {
    StringBuilder str = new StringBuilder();
    int i = 0;
    while ((i < url.length()) && (url.charAt(i) != '{')) {
      str.append(url.charAt(i++));
    }
    return str.toString();
  }

  private static String[] getPathVariablesFromUrlXXX(String url) throws IllegalArgumentException {
    ArrayList<String> vars = new ArrayList<String>();
    StringBuilder stringBuilder = null;
    boolean bracketStarted = false;

    for (int i=0; i<url.length(); ++i) {
      char c = url.charAt(i);

      if ((c == '{' && bracketStarted)
              || (c == '}' && !bracketStarted)) {
        throw new IllegalArgumentException("wrong format of url");
      }
      if (bracketStarted && c != '}') {
        stringBuilder.append(c);
      }
      if (c == '{') {
        bracketStarted = true;
        stringBuilder = new StringBuilder();
      } else if (c == '}') {
        vars.add(stringBuilder.toString());
        bracketStarted = false;
      }
    }
    return vars.toArray(new String[vars.size()]);
  }

  // Based on assertion that given method has @RequestMapping annotation.
  public static String convertReqeustMappingToRegexXXX(@NotNull Method method) {
    Assert.notNull(method);

    RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
    Assert.notNull(requestMappingAnnotation);

    String pathRaw = requestMappingAnnotation.path();
    StringBuilder regex = new StringBuilder(getBaseUrlXXX(pathRaw));
    String[] pathVars = getPathVariablesFromUrlXXX(pathRaw);

    int pathVarsCount = 0;
    boolean firstParamHandled = false;
    for ( Annotation[] annos : method.getParameterAnnotations() ) {
      for ( Annotation anno : annos ) {
        if ( anno.annotationType().equals(PathVariable.class) ) {
          String pathRegex = "[\\w-;,/.\\?%@&]+"; // = 제외
          String pathName = pathVars[pathVarsCount++];
          regex.append( "(" + pathRegex + ")" );
        } else if ( anno.annotationType().equals(RequestParam.class) ) {
          String paramName = ((RequestParam) anno).name();
          String paramRegex = "[\\w-;,.\\?%@&=]+"; // slash(/) 제외
          if (!firstParamHandled) {
            regex.append( "\\?" + "(" + paramName + "=?" + paramRegex + ")" );
            firstParamHandled = true;
          } else {
            regex.append( "&(" + paramName + "=?" + paramRegex + ")" );
          }
        }
      }
    }
    return regex.toString();
  }
}
