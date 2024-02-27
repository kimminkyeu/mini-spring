package minispring.http.base;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import minispring.util.Assert;
import minispring.util.HttpRequestUtils;

import static minispring.util.IOUtils.CRLF;

public abstract class HttpHeader {
  protected final Map<String, String> headerRaw;
  public HttpHeader() {
    this.headerRaw = new HashMap<String, String>();
  }

  public void put(@NotNull HttpRequestUtils.Pair pair) {
    Assert.notNull(pair);
    this.headerRaw.put(pair.getKey(), pair.getValue());
  }

  public void put(@NotNull String key, @NotNull String value) {
    Assert.notNull(key);
    Assert.notNull(value);
    this.headerRaw.put(key, value);
  }


  public String get(@NotNull String key) {
    Assert.notNull(key);
    return this.headerRaw.get(key);
  }

  public boolean has(@NotNull String key) {
    Assert.notNull(key);
    return this.headerRaw.containsKey(key);
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    for (Map.Entry<String, String> entry : headerRaw.entrySet()) {
      buf.append(entry.getKey()).append(": ").append(entry.getValue()).append(CRLF);
    }
    return buf.toString();
  }
}
