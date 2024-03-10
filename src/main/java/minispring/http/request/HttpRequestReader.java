package minispring.http.request;

import com.google.common.net.HttpHeaders;
import minispring.http.base.HttpHeader;
import minispring.http.base.HttpStatus;
import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;

import java.io.*;

import minispring.exception.HttpClient4xxException;
import minispring.util.HttpRequestUtils;

import static minispring.util.IOUtils.CRLF;

public final class HttpRequestReader {
  private final BufferedReader bufferedReader;

  public HttpRequestReader(@NotNull InputStream in) {
    Assert.notNull(in);

    this.bufferedReader = new BufferedReader(new InputStreamReader(in));
  }

  public HttpRequest read() throws IOException, HttpClient4xxException {
    HttpRequestEntity entity = this.readEntity();
    HttpRequestHeader header = this.readHeader();

    String contentLength = header.get(HttpHeaders.CONTENT_LENGTH);
    HttpRequestBody body = null;
    if (contentLength != null) {
       body = this.readBody(Integer.parseInt(contentLength));
    }
    return new HttpRequest(entity, header, body);
  }

  @NotNull
  private HttpRequestEntity readEntity() throws IOException, HttpClient4xxException {
    String line = bufferedReader.readLine();
    return new HttpRequestEntity(line);
  }

  @NotNull
  private HttpRequestHeader readHeader() throws IOException, HttpClient4xxException {
    String line = null;
    HttpRequestHeader header = new HttpRequestHeader();
    while ( ((line = bufferedReader.readLine()) != null) &&
            (line.isEmpty() == false) ) {
      HttpRequestUtils.Pair pair = HttpRequestUtils.getKeyValue(line, ": ");
      if (pair == null) {
        throw new HttpClient4xxException(HttpStatus.BAD_REQUEST, "Header 양식이 잘못되었습니다!");
      }
      header.put(pair);
    }
    return header;
  }

  private HttpRequestBody readBody(int contentLength) throws IOException {
    char[] buf = new char[contentLength];
    int rdSize = 0;
    while (rdSize < contentLength) {
      rdSize += bufferedReader.read(buf, rdSize, contentLength - rdSize);
    }
    return new HttpRequestBody(new String(buf));
  }
}