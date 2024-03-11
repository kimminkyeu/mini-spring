package webserver;

import java.io.*;
import java.net.Socket;

import minispring.DispatcherServlet;
import minispring.exception.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import minispring.http.response.HttpResponseBody;
import minispring.http.base.HttpVersion;
import minispring.http.request.HttpRequest;
import minispring.http.request.HttpRequestReader;
import minispring.http.response.HttpResponse;
import minispring.http.response.HttpResponseSender;

public class RequestHandlerThread extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerThread.class);
    private final Socket connection;

    public RequestHandlerThread(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    // TODO: 왜 InputStrem, OutputStream만 try-resource 안에 넣는 건지, Closeable에 대해 알아보기
    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try ( InputStream in = connection.getInputStream();
              OutputStream out = connection.getOutputStream() ) {
            HttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_11);
            try {
                HttpRequest httpRequest = new HttpRequestReader(in).read();
                new DispatcherServlet().dispatch(httpRequest, httpResponse);
            } catch (HttpException httpException) {
                httpResponse.setHttpStatus(httpException.getHttpStatus());
                httpResponse.setBody(new HttpResponseBody(httpException.getHttpStatus().getStatusMessage()));
            } finally {
                HttpResponseSender responseSender = new HttpResponseSender(out, httpResponse);
                responseSender.sendAll();
            }
        } catch (IOException ioException) {
            log.error("Fatal error from server : {}", ioException.getMessage());
        }
    }
}