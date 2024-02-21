package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.HttpRequest;
import http.HttpRequestReader;
import exception.BadHttpRequestException;

// 소스를 들어가보지 않아도 쓰레드임을 알 수 있도록 판단.
public class RequestHandlerThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandlerThread.class);
    private final Socket connection;

    public RequestHandlerThread(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    // Ref: https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        // NOTE: 왜 InputStrem, OutputStream만 try절 안에 넣는 걸까?
        //  추축 1. 특정 객체는 close를 자동으로 못한다...? (fileReader도 여기서 해야 했어음)
        try ( InputStream socketIn = connection.getInputStream();
              OutputStream socketOut = connection.getOutputStream(); ) {
            // InputStream 과 InputStreamReader 두 Class가 나눠진 이유를 좀 알아보자. // 둘다 쌩짜 read를 지원한다.
            InputStreamReader inputStreamReader = new InputStreamReader(socketIn);
            // BufferedReader = readline 을 위한 버퍼링을 가진 클래스 (libft의 getline)
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // Http 파싱해서 데이터화. (NOTE: 정말 bufferdReader를 생성자에 넣는게 필요한가?)
            HttpRequest httpRequest = new HttpRequestReader(bufferedReader).deserialize();
            // create response stream (fd)
            DataOutputStream dos = new DataOutputStream(socketOut);
            // NOTE: single Connection이 종료되는 시점.  (Socket fd 닫히는 시점인가?)
            byte[] body = Files.readAllBytes(new File("./webapp" + httpRequest.getEntity().getUrl().toString()).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (BadHttpRequestException e) {
            log.error(e.getMessage());
            // ...
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}