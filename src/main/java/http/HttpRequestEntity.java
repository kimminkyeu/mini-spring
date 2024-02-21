package http;

import exception.BadHttpRequestException;

public class HttpRequestEntity {
    private final HttpMethod method;
    private final HttpRequestUrl url;
    private final HttpVersion version;

    public HttpMethod getMethod() {
        return method;
    }

    public HttpRequestUrl getUrl() {
        return url;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpRequestEntity(HttpMethod method,
                             HttpRequestUrl url,
                             HttpVersion version) {
        this.method = method;
        this.url = url;
        this.version = version;
    }

    public HttpRequestEntity(String requestLine) throws BadHttpRequestException {
        String[] words = requestLine.split(" ");
        if (words.length != 3) {
            throw new BadHttpRequestException("First line of HttpReqeust does not contain 3 entities (Method / URL / Version)");
        }
        this.method = HttpMethod.valueOf(words[0]);
        this.url = new HttpRequestUrl(words[1]);
        this.version = new HttpVersion(words[2]);
    }
}
