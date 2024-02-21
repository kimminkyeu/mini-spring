package http;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

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

  @NotNull
  private HttpRequestEntity getEntityFromReader() throws IOException, BadHttpRequestException {
    String line = bufferedReader.readLine();
    return new HttpRequestEntity(line);
  }

  private boolean isEndOfHeader(String line) {
    return ((line != null) && (line.equals(CRLF)));
  }

  private void getHeaderFromReader() throws IOException, BadHttpRequestException {
    String line;
    while ( ((line = bufferedReader.readLine()) != null) &&
            (line.equals(CRLF)) ) {
      HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
      // ...
    }
  }

  private void getBodyFromReader() { /* TODO: implementation required */}
}