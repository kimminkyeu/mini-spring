package controller;

import com.google.common.net.HttpHeaders;
import minispring.RequestHandler;
import minispring.ResponseModifier;
import minispring.annotation.PathVariable;
import minispring.annotation.RequestMapping;
import minispring.RequestHandlerStoreManager;
import minispring.http.base.HttpContentType;
import minispring.http.base.HttpMethod;
import minispring.http.response.HttpResponseBody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

// @Controller
public class FileController {

  public FileController() throws NoSuchMethodException {
    Method getStaticFile = FileController.class.getMethod("getStaticFile", String.class);
      RequestHandlerStoreManager.getInstance().register(
              getStaticFile.getAnnotation(RequestMapping.class).method(),
              new RequestHandler(getStaticFile));
  }

  @RequestMapping(path = "/webapp/{filePath}", method = HttpMethod.GET)
  public ResponseModifier getStaticFile(@PathVariable String filePath) {
    return new ResponseModifier((response) -> {
      Path path = new File("./webapp" + "/" + filePath).toPath();
      try {
        String mimeType = Files.probeContentType(path);
        String body = new String(Files.readAllBytes(path));
        if (mimeType != null) {
          response.getHeader().put(HttpHeaders.CONTENT_TYPE, mimeType);
        } else {
          response.getHeader().put(HttpHeaders.CONTENT_TYPE, HttpContentType.TEXT_PLAIN.getTypeRaw());
        }
        response.setBody(new HttpResponseBody(new String(Files.readAllBytes(path))));
      } catch (IOException e) {
        throw new RuntimeException(e); // TODO: ?
      }
    });
  }
}
