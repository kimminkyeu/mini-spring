package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.FileController;
import controller.UserController;
import minispring.BeanManagerProvider;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        // NOTE: (1) init controller instance
        BeanManagerProvider.getManager().registerBean("UserController", new UserController());
        BeanManagerProvider.getManager().registerBean("FileController", new FileController());

        // NOTE: (2) create connection
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);
            Socket connection;
            // NOTE: ??? accept가 null을 반환하는 경우가 뭐지 ???
            while ((connection = listenSocket.accept()) != null) {
                RequestHandlerThread requestHandlerThread = new RequestHandlerThread(connection);
                requestHandlerThread.start();
            }
        }
    }
}
