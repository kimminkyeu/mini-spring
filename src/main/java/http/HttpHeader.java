package http;

import java.util.HashMap;
import java.util.Map;
import com.google.common.net.HttpHeaders;
import util.HttpRequestUtils;

public class HttpHeader  {
  private final Map<String, String> headerRaw;
  public HttpHeader() {
    this.headerRaw = new HashMap<String, String>();
  }

  public void add(HttpRequestUtils.Pair pair) {
    this.headerRaw.put(pair.getKey(), pair.getValue());
  }

  public void add(String key, String value) {
    this.headerRaw.put(key, value);
  }

  public String get(String key) {
    return this.headerRaw.get(key);
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder("HttpHeader {\n");
    for (Map.Entry<String, String> entry : headerRaw.entrySet()) {
      buf.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }
    return buf.toString();
  }
}
