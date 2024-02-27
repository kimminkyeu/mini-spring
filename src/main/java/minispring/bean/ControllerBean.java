package minispring.bean;

import minispring.http.request.HttpRequestUrl;
import org.jetbrains.annotations.NotNull;

public class ControllerBean extends Bean {
  public ControllerBean(@NotNull Object instance) {
    super(instance);
  }

  public void execute(HttpRequestUrl httpRequestUrl) {

  }
}
