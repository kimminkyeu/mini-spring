package minispring.annotation;

import minispring.http.base.HttpContentType;
import minispring.http.base.HttpMethod;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
  String path();
  HttpMethod method();
}
