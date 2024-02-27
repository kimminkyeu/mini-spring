package minispring;

import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import minispring.util.Assert;

public abstract class BeanManagerProvider {

  private static BeanManagerSingleton instance = null;

  public static synchronized BeanManagerSingleton getManager() {
    if (instance == null) {
      instance = new BeanManagerSingleton();
    }
    return instance;
  }

  public static class BeanManagerSingleton {
    private final Map<String, Object> beanTable = new ConcurrentHashMap<>(); // for thread safe

    private BeanManagerSingleton() {}
    public void registerBean(final @NotNull String name,
                             final @NotNull Object instance) {
      Assert.notNull(name);
      Assert.notNull(instance);
      Assert.isTrue((this.beanTable.containsKey(name) == false),
              "김민규", () -> "동일한 name이 이미 존재합니다.");

      this.beanTable.put(name, instance);
    }

    @NotNull // 호출 프로그래머의 책임
    public Object getBean(@NotNull String name) {
      Assert.notNull(name);
      Object result = this.beanTable.get(name);

      Assert.notNull(result);
      return result;
    }
  }
}


