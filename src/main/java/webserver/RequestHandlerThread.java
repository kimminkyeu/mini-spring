package webserver;

import java.io.*;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import minispring.DispatcherManagerProvider;
import minispring.exception.HttpClient4xxException;
import minispring.exception.HttpServer5xxException;
import minispring.http.response.HttpResponseBody;
import minispring.http.base.HttpStatus;
import minispring.http.base.HttpVersion;
import minispring.http.request.HttpRequest;
import minispring.http.request.HttpRequestReader;
import minispring.http.response.HttpResponse;
import minispring.http.response.HttpResponseSender;
import minispring.util.Assert;

public class RequestHandlerThread extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerThread.class);
    private final Socket connection;

    public RequestHandlerThread(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    // TODO: 왜 InputStrem, OutputStream만 try-resource 안에 넣는 건지, Closeable에 대해 알아보기
    // Thread Ref: https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try ( InputStream socketIn = connection.getInputStream();
              OutputStream socketOut = connection.getOutputStream() ) {
            startHttp11Stream(socketIn, socketOut);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void startHttp11Stream(@NotNull InputStream in, @NotNull OutputStream out) throws IOException {
        Assert.notNull(in);
        Assert.notNull(out);

        HttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_11);

        try {
            HttpRequestReader requestReader = new HttpRequestReader(in);
            HttpRequest httpRequest = requestReader.deserializeAll();
            DispatcherManagerProvider.getManager().dispatch(httpRequest, httpResponse);

        } catch (HttpClient4xxException e) {
            log.error(e.getMessage());
            HttpStatus status = e.getHttpStatus();
            switch (status) {
                case BAD_REQUEST:
                    // TODO: ...
                    break;
                default:
                    // TODO: ...
            }
            httpResponse.setHttpStatus(e.getHttpStatus());
            httpResponse.setBody(new HttpResponseBody(status.getMessage()));

        } catch (HttpServer5xxException e) {
            httpResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            httpResponse.setBody(new HttpResponseBody("Woops! Server has been crashed!"));
            e.printStackTrace();

        } catch (Exception e) { // TODO: 다른 runtime exeption은?
            log.error("명시적으로 처리하지 못한 Exception이 발생했습니다. 통보 요망!!");
            e.printStackTrace();

        } finally {
            HttpResponseSender responseSender = new HttpResponseSender(out, httpResponse);
            responseSender.sendAll();
        }
    }
}