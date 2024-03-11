package minispring.http.request;

import com.google.common.net.HttpHeaders;
import minispring.http.base.HttpHeader;
import minispring.util.HttpRequestUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class HttpRequestHeader extends HttpHeader {
  @Nullable
  public Map<String, String> getCookies() {
    String cookieRaw = this.get(HttpHeaders.COOKIE);
    if (cookieRaw != null) {
      return HttpRequestUtils.parseCookies(cookieRaw);
    }
    return null;
  }

}
