package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.

        /* NOTE: try catch에서 server socket 자원을 안전하기 반환하기 위한 문법 { try (...) }
           Ref: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html */
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            // NOTE: accept가 null을 반환하는 경우가 뭐지?
            while ((connection = listenSocket.accept()) != null) {
                RequestHandlerThread requestHandlerThread = new RequestHandlerThread(connection);
                requestHandlerThread.start(); // --> start thread (요청마다 쓰레드를 생성한다...?)
            }
        }
    }
}
