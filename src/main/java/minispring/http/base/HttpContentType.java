package minispring.http.base;

import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;

public enum HttpContentType {
  ALL("*/*"),
  APPLICATION_ATOM_XML("application/atom+xml"),
  APPLICATION_CBOR("application/cbor"),
  APPLICATION_JSON("application/json"),
  APPLICATION_JSON_UTF8("application/json;charset=utf8"),
  APPLICATION_PDF("application/pdf"),
  APPLICATION_STREAM_JSON("application/stream+json"),
  APPLICATION_XML("application/xml"),
  IMAGE_GIF("image/gif"),
  IMAGE_JPEG("image/jpeg"),
  IMAGE_PNG("image/png"),
  MULTIPART_FORM_DATA("multipart/form-data;charset=utf8"),
  TEXT_HTML("text/html;charset=utf8"),
  TEXT_CSS("text/css;charset=utf8"),
  TEXT_MARKDOWN("text/markdown;charset=utf8"),
  TEXT_PLAIN("text/plain;charset=utf8");

  final String typeRaw;
  HttpContentType(@NotNull String typeRaw) {
    Assert.notNull(typeRaw);
    this.typeRaw = typeRaw;
  }

  public String getTypeRaw() {
    return typeRaw;
  }
}
