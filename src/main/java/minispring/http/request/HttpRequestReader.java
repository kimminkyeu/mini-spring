package minispring.http.request;

import org.jetbrains.annotations.NotNull;

import java.io.*;

import minispring.exception.HttpClient4xxException;
import minispring.util.HttpRequestUtils;

import static minispring.util.IOUtils.CRLF;

// NOTE: 그런데... Reader Close는 누가 책임지나?
public final class HttpRequestReader extends BufferedReader {
  public HttpRequestReader(InputStream in) {
    super(new InputStreamReader(in));
  }

  @Override
  public int read(char @NotNull [] cbuf, int off, int len) throws IOException {
    return super.read(cbuf, off, len);
  }

  @Override
  public void close() throws IOException {
    super.close();
  }

  public HttpRequest deserializeAll() throws IOException, HttpClient4xxException {
    HttpRequestEntity entity = this.readEntity();
    HttpRequestHeader header = this.readHeader();
    HttpRequestBody body = this.readBody();
    return new HttpRequest(entity, header, body);
  }

  @NotNull // entity가 null이거나 method, url, version 이 없는 경우 BadHttpRequestException 발생.
  public HttpRequestEntity readEntity() throws IOException, HttpClient4xxException {
    String line = super.readLine();
    return new HttpRequestEntity(line);
  }

  @NotNull // header가 null이거나 필수 헤더가 없는 경우 HttpRequestException 발생.
  public HttpRequestHeader readHeader() throws IOException, HttpClient4xxException {
    String line = null;
    HttpRequestHeader header = new HttpRequestHeader();
    while ( ((line = super.readLine()) != null) &&
            (line.equals(CRLF)) ) {
      HttpRequestUtils.Pair pair = HttpRequestUtils.getKeyValue(line, ": ");
      if (pair != null) {
        header.put(pair);
        // TODO: 필수 헤더 검증 해야 합니다. 지금은 HttpRequestException이 발생안해요.
      }
    }
    return header;
  }

  public HttpRequestBody readBody() throws IOException, HttpClient4xxException {
    return new HttpRequestBody("Hello World!");
  }
}