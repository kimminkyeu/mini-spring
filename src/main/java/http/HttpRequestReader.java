package http;

import com.google.common.net.HttpHeaders;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import webserver.RequestHandlerThread;
import exception.BadHttpRequestException;
import util.HttpRequestUtils;

// NOTE: Reader Close는 누가 책임지나?
public class HttpRequestReader {
  private final BufferedReader bufferedReader;
  private static final String CRLF = "\r\n";

  public HttpRequestReader(BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
  }

  public HttpRequest deserialize() throws IOException, BadHttpRequestException {
    HttpRequestEntity entity = this.getEntityFromReader();
    // HttpReqeustHeader header = this.getHeaderFromReader();
    // HttpReqeustBody body = this.getBodyFromReader(); --> nullable
    return new HttpRequest(entity /*, header, body */);
  }

  @NotNull // NOTE: entity가 null이거나 method, url, version 이 없는 경우 BadHttpRequestException 발생.
  private HttpRequestEntity getEntityFromReader() throws IOException, BadHttpRequestException {
    String line = bufferedReader.readLine();
    return new HttpRequestEntity(line);
  }

  @NotNull // NOTE: header가 null이거나 필수 헤더가 없는 경우 BadHttpRequestException 발생.
  private HttpHeader getHeaderFromReader() throws IOException, BadHttpRequestException {
    String line = null;
    HttpHeader header = new HttpHeader();
    while ( ((line = bufferedReader.readLine()) != null) &&
            (line.equals(CRLF)) ) {
      HttpRequestUtils.Pair pair = HttpRequestUtils.getKeyValue(line, ": ");
      if (pair != null) {
        header.add(pair);
      }
    }
    return header;
  }

  private void getBodyFromReader() { /* TODO: implementation required */}
}