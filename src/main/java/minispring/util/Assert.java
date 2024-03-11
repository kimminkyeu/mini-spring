package minispring.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

public abstract class Assert {
  public static void isTrue(boolean expression, @NotNull String developer, @NotNull String message) {
    if (!expression) {
      throw new IllegalArgumentException(developer + ": " + message);
    }
  }

  public static void notNull(@Nullable Object object) {
    if (object == null) {
      throw new IllegalArgumentException("object must not be null");
    }
  }

  public static void notNull(@Nullable Object object, @NotNull String developer, @NotNull String message) {
    if (object == null) {
      throw new IllegalArgumentException(developer + ": " + message);
    }
  }
}
