package minispring.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

// https://github.com/spring-projects/spring-framework/blob/main/spring-core/src/main/java/org/springframework/util/Assert.java#L62
// NOTE: why abstract class? --> 이유가 있나?
public abstract class Assert {
  @Nullable
  private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
    return (messageSupplier != null ? messageSupplier.get() : null);
  }
  public static void isTrue(boolean expression, @NotNull String developer, @Nullable Supplier<String> messageSupplier) {
    if (!expression) {
      throw new IllegalArgumentException(developer + ": " + nullSafeGet(messageSupplier));
    }
  }

  public static void notNull(@Nullable Object object) {
    if (object == null) {
      throw new IllegalArgumentException("object must not be null");
    }
  }

  public static void notNull(@Nullable Object object, @NotNull String developer, @Nullable Supplier<String> messageSupplier) {
    if (object == null) {
      throw new IllegalArgumentException(developer + ": " + nullSafeGet(messageSupplier));
    }
  }
}
