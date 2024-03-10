package minispring;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import minispring.util.Assert;

public class BeanManager {
  private BeanManager() {}
  private static BeanManager instance = null;
  public static synchronized BeanManager getInstance() {
    if (instance == null) {
      instance = new BeanManager();
    }
    return instance;
  }

  private final Map<String, Object> beanTable = new HashMap<>();

  public void register(final @NotNull Object instance) {
    Assert.notNull(instance);
    String name = instance.getClass().getSimpleName();

    Assert.isTrue((this.beanTable.containsKey(name) == false), "김민규", "동일한 name이 이미 존재합니다.");
    this.beanTable.put(name, instance);
  }

  @NotNull
  public Object getBean(@NotNull String name) {
    Assert.notNull(name);
    Object result = this.beanTable.get(name);

    Assert.notNull(result);
    return result;
  }
}


