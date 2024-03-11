package minispring.http.request;

import minispring.exception.HttpClient4xxException;
import minispring.http.base.HttpMethod;
import minispring.http.base.HttpStatus;
import minispring.http.base.HttpVersion;

public final class HttpRequestEntity {
    private final HttpMethod method;
    private final HttpRequestUrl url;
    private final HttpVersion version;

    public HttpMethod getHttpMethod() {
        return method;
    }

    public HttpRequestUrl getHttpRequestUrl() {
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

    public HttpRequestEntity(String httpRequestFirstLine) throws HttpClient4xxException {
        if (httpRequestFirstLine == null || httpRequestFirstLine.isEmpty()) {
            throw new HttpClient4xxException(
                    HttpStatus.BAD_REQUEST,
                    "first line of http request is null or empty"
            );
        }
        String[] words = httpRequestFirstLine.split(" ");
        if (words.length != 3) {
            throw new HttpClient4xxException(
                    HttpStatus.BAD_REQUEST,
                    "http request doesn't contain one of the following entity (Method, URL, Version)");
        }
        this.method = HttpMethod.valueOf(words[0]);
        this.url = new HttpRequestUrl(words[1]);
        this.version = HttpVersion.fromString(words[2]);
    }
}
