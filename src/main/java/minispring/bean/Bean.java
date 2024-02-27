package minispring.bean;


import org.jetbrains.annotations.NotNull;

public abstract class Bean {
  protected final Object instance;

  public Bean(@NotNull final Object instance) {
    this.instance = instance;
  }
}

